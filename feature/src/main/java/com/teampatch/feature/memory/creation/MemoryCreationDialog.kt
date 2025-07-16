package com.teampatch.feature.memory.creation

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.teampatch.core.R.drawable.ic_camera_memory_card
import com.teampatch.core.R.drawable.ic_close_memory_card
import com.teampatch.core.R.drawable.ic_date_memory_card
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP1
import com.teampatch.core.designsystem.theme.DP10
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP14
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP192
import com.teampatch.core.designsystem.theme.DP2
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP32
import com.teampatch.core.designsystem.theme.DP40
import com.teampatch.core.designsystem.theme.DP52
import com.teampatch.core.designsystem.theme.DP68
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.theme.SP24
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.R
import com.teampatch.feature.memory.creation.model.MemoryCreationEvent
import com.teampatch.feature.memory.creation.model.MemoryCreationUiState

@Composable
fun MemoryCreationDialog(onDismissRequest: () -> Unit) {
    MemoryCardCreationDialogWithViewModel(onDismissRequest = onDismissRequest)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemoryCardCreationDialogWithViewModel(
    onDismissRequest: () -> Unit,
    viewModel: MemoryCardCreationViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val lifecycle: LifecycleOwner = LocalLifecycleOwner.current
    val uiState: MemoryCreationUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Dialog(onDismissRequest) {
        MemoryCardCreationContent(
            onDismissRequest = { viewModel.clearState(); onDismissRequest() },
            onCompleteRequest = viewModel::addMemoryCard,
            onChangeImage = viewModel::updateImage,
            onDateChange = viewModel::updateDate,
            onTitleChange = viewModel::updateTitleText,
            uiState = uiState
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycle.lifecycle)
            .collect { event ->
                when (event) {
                    MemoryCreationEvent.MemoryCreationError -> Toast.makeText(
                        context,
                        context.getString(R.string.memory_creation_toast_error_memory_upload),
                        Toast.LENGTH_SHORT
                    ).show()

                    MemoryCreationEvent.MemoryCreationSuccess -> onDismissRequest()
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryCardCreationContent(
    onDismissRequest: () -> Unit,
    onCompleteRequest: () -> Unit,
    onChangeImage: (Uri) -> Unit,
    onDateChange: (millis: Long) -> Unit,
    onTitleChange: (String) -> Unit,
    uiState: MemoryCreationUiState
) {
    val datePickerState: DatePickerState = rememberDatePickerState()
    val isBottomButtonEnable: Boolean by remember(uiState) { derivedStateOf { uiState.checkNextProcess() } }
    var isDatePickerDialogShow: Boolean by remember { mutableStateOf(false) }

    val photoPicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri == null) return@rememberLauncherForActivityResult
                onChangeImage(uri)
            }
        )

    if (isDatePickerDialogShow) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerDialogShow = false },
            confirmButton = {
                Text(
                    text = stringResource(R.string.date_picker_text_ok),
                    modifier = Modifier
                        .padding(start = DP16, bottom = DP12, end = DP12)
                        .noRippleClickable {
                            datePickerState.selectedDateMillis?.let { onDateChange(it) }
                            isDatePickerDialogShow = false
                        }
                )
            },
            dismissButton = {
                Text(
                    text = stringResource(R.string.date_picker_text_cancel),
                    modifier = Modifier.noRippleClickable { isDatePickerDialogShow = false }
                )
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape10
            )
            .padding(top = DP24, bottom = DP14, start = DP16, end = DP16)
    ) {
        Text(
            text = stringResource(R.string.memory_creation_text_title),
            color = BL,
            fontSize = SP18,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(DP192)
                .padding(top = DP24)
                .clip(RoundedCornerShape10)
                .background(G1, RoundedCornerShape10)
                .noRippleClickable { photoPicker.launch(pickVisualMediaRequest) }
        ) {
            uiState.imageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = stringResource(R.string.memory_creation_description_memory_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Image(
                painter = painterResource(ic_camera_memory_card),
                contentDescription = stringResource(R.string.memory_creation_description_empty_memory_image)
            )
            Canvas(Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = G2,
                    style = Stroke(
                        width = DP2.toPx(),
                        pathEffect = pathEffect,
                        cap = StrokeCap.Round
                    ),
                    cornerRadius = CornerRadius(x = DP10.toPx())
                )
                BorderStroke(width = DP2, color = G2)
            }
        }
        Text(
            text = stringResource(R.string.memory_creation_label_input_memory_title),
            color = BL,
            fontSize = SP18,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = DP24)
        )
        DefaultTextField(
            value = uiState.title,
            onValueChange = onTitleChange,
            hint = { Text(text = stringResource(R.string.memory_creation_hint_input_memory_date)) },
            modifier = Modifier.padding(top = DP8)
        )
        Text(
            text = stringResource(R.string.memory_creation_label_input_memory_date),
            color = BL,
            fontSize = SP18,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = DP32, bottom = DP8)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = DP52)
                .background(color = WH, shape = RoundedCornerShape10)
                .border(width = DP1, color = G2, shape = RoundedCornerShape10)
                .padding(horizontal = DP20)
                .noRippleClickable { isDatePickerDialogShow = true }
        ) {
            Image(
                painter = painterResource(ic_date_memory_card),
                contentDescription = stringResource(R.string.memory_creation_description_input_date_image)
            )
            Text(
                text = with(uiState.date) {
                    stringResource(
                        R.string.memory_creation_textfield_date_format,
                        year,
                        monthValue,
                        dayOfMonth
                    )
                },
                color = BL,
                fontSize = SP20,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = DP20)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = DP40)
                .fillMaxWidth()
                .heightIn(min = DP68)
                .background(
                    color = if (isBottomButtonEnable) MainGreen else G2,
                    shape = RoundedCornerShape10
                )
                .noRippleClickable(enabled = isBottomButtonEnable, onClick = onCompleteRequest)
        ) {
            Text(
                text = stringResource(R.string.memory_creation_button_upload),
                color = WH,
                fontSize = SP24,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = DP16, horizontal = DP24)
    ) {
        Image(
            painter = painterResource(ic_close_memory_card),
            contentDescription = stringResource(R.string.memory_creation_description_close_image),
            modifier = Modifier.noRippleClickable(onClick = onDismissRequest)
        )
    }
}

private val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
private val pickVisualMediaRequest: PickVisualMediaRequest =
    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

@Preview
@Composable
private fun MemoryCardCreationDialogPreview() {
    HarmonyTheme {
        MemoryCardCreationContent(
            onDismissRequest = {},
            onCompleteRequest = {},
            onChangeImage = {},
            onDateChange = {},
            onTitleChange = {},
            uiState = MemoryCreationUiState()
        )
    }
}