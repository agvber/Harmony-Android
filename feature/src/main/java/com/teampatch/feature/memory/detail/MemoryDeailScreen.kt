package com.teampatch.feature.memory.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.teampatch.core.R.drawable.ic_camera_memory
import com.teampatch.core.R.drawable.ic_more_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.TagCard
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP0
import com.teampatch.core.designsystem.theme.DP1
import com.teampatch.core.designsystem.theme.DP10
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP240
import com.teampatch.core.designsystem.theme.DP28
import com.teampatch.core.designsystem.theme.DP40
import com.teampatch.core.designsystem.theme.DP6
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.domain.model.user.Role
import com.teampatch.feature.R
import com.teampatch.feature.memory.detail.model.MemoryDetailEvent
import com.teampatch.feature.memory.detail.model.MemoryDetailUiState
import java.time.LocalDate

@Composable
internal fun MemoryDetailScreenWithViewModel(
    onBackRequest: () -> Unit,
    onDetailPageRequest: (String) -> Unit,
    viewModel: MemoryDetailViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState: MemoryDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MemoryDetailScreen(
        onBackRequest = onBackRequest,
        onDetailPageRequest = { onDetailPageRequest(uiState.id) },
        uiState = uiState
    )

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    MemoryDetailEvent.LoadError ->
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_load_error),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryDetailScreen(
    onBackRequest: () -> Unit,
    onDetailPageRequest: () -> Unit,
    uiState: MemoryDetailUiState
) {
    val mainScrollState = rememberScrollState()
    var isSettingsBottomSheetShow by remember { mutableStateOf(false) }

    if (isSettingsBottomSheetShow) {
        SettingsBottomSheet(
            onDismissRequest = { isSettingsBottomSheetShow = false }
        )
    }

    Scaffold(
        topBar = {
            BackButtonAppBar(onBackRequest = onBackRequest, actions = {
                if (uiState.role == Role.VIP) {
                    IconButton(
                        onClick = { isSettingsBottomSheetShow = true },
                        modifier = Modifier.padding(end = DP16)
                    ) {
                        Icon(
                            painter = painterResource(ic_more_question),
                            contentDescription = stringResource(R.string.image_more_description)
                        )
                    }
                }
            })
        },
        bottomBar = {
            DefaultButton(
                onClick = onDetailPageRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DP20, end = DP20, bottom = DP8)
            ) {
                Text(stringResource(R.string.btn_look_all_answer))
            }
        },
    ) { scaffoldPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
                .verticalScroll(mainScrollState)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DP240)
                    .background(G1)
            ) {
                uiState.imageUrl?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = stringResource(R.string.image_memory_content_description),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                    ?: Image(
                        painter = painterResource(ic_camera_memory),
                        contentDescription = stringResource(R.string.image_memory_content_description)
                    )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DP28)
            ) {
                Text(
                    text = uiState.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = BL
                )
                Text(
                    text = with(uiState.writtenDateTime) {
                        stringResource(
                            R.string.text_memory_card_date_format,
                            year,
                            monthValue,
                            dayOfMonth
                        )
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = G5,
                    modifier = Modifier.padding(top = DP6)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DP12),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = DP16)
                ) {
                    uiState.tags.forEach { text ->
                        TagCard({ Text(text) })
                    }
                }
            }
            HorizontalDivider(
                thickness = DP12,
                color = G1,
                modifier = Modifier.padding(vertical = DP20)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DP20)
                    .background(G1, RoundedCornerShape(DP10))
                    .padding(DP24)
            ) {
                Text(
                    text = uiState.description,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = G5
                )
            }
            Box(Modifier.height(DP40))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = WH,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = DP0,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = null,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = DP20)
        ) {
            Text(
                text = stringResource(R.string.text_retry_settings),
                style = BottomSheetTextStyle,
                modifier = BottomSheetTextModifier,
            )
            HorizontalDivider(thickness = DP1, color = G3)
            Text(
                text = stringResource(R.string.text_delete_settings),
                style = BottomSheetTextStyle,
                modifier = BottomSheetTextModifier,
            )
        }
    }
}

private val BottomSheetTextStyle = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Medium,
    textAlign = TextAlign.Center,
)

private val BottomSheetTextModifier = Modifier
    .fillMaxWidth()
    .padding(vertical = DP20)

@Preview
@Composable
private fun MemoryDetailScreenVipPreview() {
    HarmonyTheme {
        MemoryDetailScreen(
            onBackRequest = {},
            onDetailPageRequest = {},
            uiState = MemoryDetailUiState(
                id = "1",
                title = "손자",
                description = "내 고향 부산에 가보고 싶다. 부산의 바다가 가끔 그립더라고. 나중에 꼭 다 같이 부산에 가서 고기도 먹고 맛있는 거 많이 많이 먹고 싶다. ^^",
                writtenDateTime = LocalDate.now(),
                tags = setOf("가족", "기억", "축하"),
                imageUrl = null,
                role = Role.VIP
            )
        )
    }
}

@Preview
@Composable
private fun MemoryDetailScreenMemberPreview() {
    HarmonyTheme {
        MemoryDetailScreen(
            onBackRequest = {},
            onDetailPageRequest = {},
            uiState = MemoryDetailUiState(
                id = "1",
                title = "손자",
                description = "내 고향 부산에 가보고 싶다. 부산의 바다가 가끔 그립더라고. 나중에 꼭 다 같이 부산에 가서 고기도 먹고 맛있는 거 많이 많이 먹고 싶다. ^^",
                writtenDateTime = LocalDate.now(),
                tags = setOf("가족", "기억", "축하"),
                imageUrl = null,
                role = Role.MEMBER
            )
        )
    }
}