package com.teampatch.feature.memory.registration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.teampatch.core.common.findActivity
import com.teampatch.core.common.requestRadioAudioPermission
import com.teampatch.core.designsystem.R.drawable.ic_camera_memory
import com.teampatch.core.designsystem.R.drawable.ic_close_memory_card
import com.teampatch.core.designsystem.R.drawable.ic_harmony_talk
import com.teampatch.core.designsystem.R.drawable.ic_voice_memorycard
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.DefaultButtonColor
import com.teampatch.core.designsystem.component.SpeechBubble
import com.teampatch.core.designsystem.component.TypeWriterText
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP156
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.memory.R
import com.teampatch.feature.memory.registration.model.MemoryRegistrationEvent
import com.teampatch.feature.memory.registration.model.MemoryRegistrationUiState
import com.teampatch.feature.memory.registration.model.RecordState

@Composable
internal fun MemoryRegistrationRoute(
    onDismissRequest: () -> Unit,
    onMemoryStorePageRequest: () -> Unit,
    viewModel: MemoryRegistrationViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val activity: Activity? = context.findActivity()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState: MemoryRegistrationUiState = viewModel.uiState

    if (!uiState.isLoading) {
        MemoryRegistrationScreen(
            onDismissRequest = onDismissRequest,
            onMemoryStorePageRequest = onMemoryStorePageRequest,
            onSuccessRecord = viewModel::uploadMemoryCardAnswer,
            uiState = uiState
        )
    }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                MemoryRegistrationEvent.LoadError ->
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_data_load_error),
                        Toast.LENGTH_SHORT
                    ).show()

                MemoryRegistrationEvent.RecordingError ->
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_audio_recording_error),
                        Toast.LENGTH_SHORT
                    ).show()

                MemoryRegistrationEvent.RecordingPermissionDeniedError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_audio_permission_request),
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.requestRadioAudioPermission()
                }

                MemoryRegistrationEvent.NetworkError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_network_error),
                        Toast.LENGTH_LONG
                    ).show()
                    onDismissRequest()
                }
            }
        }
    }
}

@Composable
internal fun MemoryRegistrationScreen(
    onDismissRequest: () -> Unit,
    onMemoryStorePageRequest: () -> Unit,
    onSuccessRecord: (String) -> Unit,
    uiState: MemoryRegistrationUiState,
) {
    val context: Context = LocalContext.current
    val speechRecognizer: SpeechRecognizer =
        remember { SpeechRecognizer.createSpeechRecognizer(context) }
    var recordState: RecordState by remember { mutableStateOf(RecordState.INIT) }

    val speechRecognizerIntent: Intent = remember { buildSpeechRecognizerIntent(context) }
    val recognitionListener: RecognitionListener = remember {
        buildRecognitionListener { result ->
            val speechText = result.toString().drop(1).dropLast(1)
            onSuccessRecord(speechText)
            speechRecognizer.stopListening()
            speechRecognizer.cancel()
            speechRecognizer.destroy()
            recordState = RecordState.COMPLETE
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        speechRecognizer.stopListening()
        speechRecognizer.cancel()
        speechRecognizer.destroy()
        recordState = RecordState.INIT
    }

    Scaffold(
        topBar = {
            AppBar(
                title = {
                    Text(
                        text = uiState.title,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(max = 240.dp)
                    )
                },
                actions = {
                    Image(
                        painter = painterResource(ic_close_memory_card),
                        contentDescription = "close",
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .noRippleClickable(onClick = onDismissRequest)
                    )
                }
            )
        },
        bottomBar = {
            DefaultButton(
                onClick = {
                    when (recordState) {
                        RecordState.INIT -> {
                            runCatching {
                                speechRecognizer.setRecognitionListener(recognitionListener)
                                speechRecognizer.startListening(speechRecognizerIntent)
                            }
                                .onSuccess {
                                    recordState = RecordState.RECORDING
                                }
                                .onFailure {
                                    recordState = RecordState.INIT
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_audio_recording_error),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }

                        RecordState.RECORDING -> {
                            runCatching { speechRecognizer.stopListening() }
                                .onSuccess { recordState = RecordState.COMPLETE }
                                .onFailure {
                                    recordState = RecordState.INIT
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_audio_recording_error),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }

                        RecordState.COMPLETE -> {
                            onMemoryStorePageRequest()
                        }
                    }
                },
                color = DefaultButtonColor(
                    containerColor = when (recordState) {
                        RecordState.RECORDING -> BL
                        else -> MainGreen
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                Text(
                    text = when (recordState) {
                        RecordState.INIT -> stringResource(R.string.btn_communication_start)
                        RecordState.RECORDING -> stringResource(R.string.btn_communication_end)
                        RecordState.COMPLETE -> stringResource(R.string.btn_communication_complete)
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(G1)
            ) {
                if (uiState.imageUrl == null) {
                    Image(
                        painter = painterResource(ic_camera_memory),
                        contentDescription = "camera"
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = uiState.imageUrl
                        ),
                        contentDescription = "image",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            SpeechBubble(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = DP156)
            ) {
                TypeWriterText(
                    text = when (recordState) {
                        RecordState.INIT -> {
                            stringResource(R.string.text_speechbuble_init)
                        }

                        RecordState.RECORDING -> {
                            uiState.questions.getOrNull(uiState.questionProgressIndex) ?: ""
                        }

                        RecordState.COMPLETE -> {
                            stringResource(R.string.text_speechbuble_complete)
                        }
                    }
                )
            }
            Image(
                painter = painterResource(ic_harmony_talk),
                contentDescription = "icon",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            if (recordState == RecordState.RECORDING) {
                Image(
                    painter = painterResource(ic_voice_memorycard),
                    contentDescription = "recording",
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

private const val LANGUAGE_VALUE = "ko-KR"

private fun buildSpeechRecognizerIntent(context: Context): Intent =
    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANGUAGE_VALUE)
    }

private fun buildRecognitionListener(
    onResult: (ArrayList<String>) -> Unit,
): RecognitionListener = object : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {
        Log.d("SpeechRecognizer", "onReadyForSpeech params:$params")
    }

    override fun onBeginningOfSpeech() {
        Log.d("SpeechRecognizer", "onBeginningOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.d("SpeechRecognizer", "sound rms level: $rmsdB")
    }

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        Log.d("SpeechRecognizer", "onEndOfSpeech")
    }

    override fun onError(error: Int) {
        Log.e("SpeechRecognizer", "onError error:$error")
    }

    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let {
            onResult(it)
        } ?: Log.d("SpeechRecognizer", "onResults results is null")
    }

    override fun onPartialResults(partialResults: Bundle?) {
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }
}

@Preview
@Composable
private fun MemoryRegistrationScreenPreview() {
    HarmonyTheme {
        MemoryRegistrationScreen(
            onDismissRequest = {},
            onMemoryStorePageRequest = {},
            onSuccessRecord = {},
            uiState = MemoryRegistrationUiState()
        )
    }
}