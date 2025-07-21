package com.teampatch.feature.routine.certification

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.teampatch.core.common.takeIfNull
import com.teampatch.core.designsystem.R.drawable.ic_camera_memory
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.CornerRadius999
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP240
import com.teampatch.core.designsystem.theme.DP28
import com.teampatch.core.designsystem.theme.DP40
import com.teampatch.core.designsystem.theme.DP48
import com.teampatch.core.designsystem.theme.DP6
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.theme.SP22
import com.teampatch.core.designsystem.theme.SP24
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.routine.R
import com.teampatch.feature.routine.certification.model.RoutineCertificationEvent
import com.teampatch.feature.routine.certification.model.RoutineCertificationUiState
import com.teampatch.feature.routine.toStringFormat
import java.time.LocalTime

@Composable
internal fun RoutineCertificationScreenWithViewModel(
    onBackRequest: () -> Unit,
    viewModel: RoutineCertificationViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState: RoutineCertificationUiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoutineCertificationScreen(
        onBackRequest = onBackRequest,
        onCertificationImageChange = viewModel::updateCertificationImage,
        uiState = uiState
    )

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    RoutineCertificationEvent.InitLoadError -> {
                        Toast.makeText(
                            context,
                            R.string.routine_certification_toast_init_data_load_error,
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackRequest()
                    }
                }
            }
    }
}

@Composable
internal fun RoutineCertificationScreen(
    onBackRequest: () -> Unit,
    onCertificationImageChange: (Uri) -> Unit,
    uiState: RoutineCertificationUiState,
) {
    val context: Context = LocalContext.current
    val density: Density = LocalDensity.current
    val photoPicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri == null) {
                    Log.d("RoutineCertificationScreen", "No media selected")
                    return@rememberLauncherForActivityResult
                }
                onCertificationImageChange(uri)
            }
        )
    val isBottomButtonEnabled by remember(uiState) {
        derivedStateOf { uiState.certificationImage != null }
    }
    var cameraPositionOffset by remember { mutableStateOf(Offset.Zero) }
    val cameraPositionDpOffset by remember(cameraPositionOffset) {
        derivedStateOf {
            with(density) {
                DpOffset(cameraPositionOffset.x.toDp(), cameraPositionOffset.y.toDp())
            }
        }
    }

    Box(contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                title = { Text(stringResource(R.string.routine_certification_appbar_title)) }
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DP240)
                    .background(G1)
                    .noRippleClickable {
                        photoPicker.launch(pickVisualMediaRequest)
                    }
            ) {
                uiState.certificationImage?.let { certificationImage ->
                    AsyncImage(
                        model = certificationImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }.takeIfNull {
                    Image(
                        painter = painterResource(ic_camera_memory),
                        contentDescription = stringResource(R.string.routine_certification_image_camera_description),
                        modifier = Modifier.onGloballyPositioned {
                            it.parentCoordinates?.positionInParent()
                                ?.let { cameraPositionOffset = it }
                        }
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(y = cameraPositionDpOffset.y + DP8)
                            .fillMaxWidth()
                            .heightIn(min = DP48)
                            .padding(horizontal = DP28)
                            .drawBehind {
                                val startPointHeight: Float = -24.dp.toPx()
                                val splitWidth: Float = size.width / 2
                                val lineHeight: Float = 12.dp.toPx()
                                val zeroFloat: Float = 0F
                                drawPath(
                                    path = Path().apply {
                                        moveTo(splitWidth, startPointHeight)
                                        lineTo(splitWidth + lineHeight, zeroFloat)
                                        lineTo(splitWidth - lineHeight, zeroFloat)
                                        close()
                                    },
                                    color = MainGreen
                                )
                                drawRoundRect(
                                    color = MainGreen,
                                    size = size.copy(height = size.height),
                                    cornerRadius = CornerRadius999
                                )
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.routine_certification_text_photo_input_information_message),
                            color = WH,
                            fontSize = SP22,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
            Text(
                text = uiState.routineTitle,
                color = BL,
                fontSize = SP24,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = DP40)
            )
            Text(
                text = uiState.routineTime.toStringFormat(context),
                color = MainGreen,
                fontSize = SP20,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = DP6)
            )
        }
        DefaultButton(
            onClick = {},
            enabled = isBottomButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DP20)
                .padding(bottom = DP8)
        ) {
            Text(stringResource(R.string.routine_certification_button_complete))
        }
    }
}

private val pickVisualMediaRequest: PickVisualMediaRequest =
    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

@Preview
@Composable
private fun RoutineCertificationScreenPreview() {
    HarmonyTheme {
        RoutineCertificationScreen(
            onBackRequest = {},
            onCertificationImageChange = {},
            uiState = RoutineCertificationUiState(
                routineTitle = "공원 산책 가서 비둘기 사진 찍기",
                routineTime = LocalTime.now()
            )
        )
    }
}