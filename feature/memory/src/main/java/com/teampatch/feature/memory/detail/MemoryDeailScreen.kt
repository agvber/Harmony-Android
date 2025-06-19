package com.teampatch.feature.memory.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.R.drawable.ic_camera_memory
import com.teampatch.core.designsystem.R.drawable.ic_more_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.MemoryInfoView
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.model.MemoryCard
import com.teampatch.feature.memory.R
import com.teampatch.feature.memory.detail.model.MemoryDetailUiState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun MemoryDetailScreenWithViewModel(modifier: Modifier = Modifier) {

}

@Composable
private fun MemoryDetailScreen(
    onBackRequest: () -> Unit,
    onShowConversation: () -> Unit,
    onRestartConversation: () -> Unit,
    uiState: MemoryDetailUiState
) {
    var answerEditMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                actions = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(36.dp)
                            .noRippleClickable {
                                showBottomSheet = true
                            }
                    ) {
                        Image(
                            painter = painterResource(ic_more_question),
                            contentDescription = "more"
                        )
                    }
                }
            )
        },
        bottomBar = {
            DefaultButton(
                onClick = { onShowConversation() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                Text(stringResource(R.string.btn_look_all_answer))
            }
        },

        modifier = Modifier
            .background(WH)
    ) { scaffoldPaddingValues ->
        val memory = uiState.memoryCard

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(G1)
            ) {
                Image(
                    painter = painterResource(ic_camera_memory),
                    contentDescription = "camera"
                )
            }

            Spacer(
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            MemoryInfoView(
                modifier = Modifier.fillMaxWidth(),
                title = memory.writerTitle,
                description = memory.dateTime.toString(),
                circleTexts = listOf(
                    memory.writerName,
                    memory.dateTime.toFormattedString(),
                    memory.text
                )
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 32.dp, top = 24.dp)
                    .background(G1, RoundedCornerShape(10.dp))
                    .padding(24.dp)
            ) {
                Text(
                    text = memory.text,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = G5
                )
            }
        }
    }
}

fun LocalDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("M월")
    return this.format(formatter)
}

@Preview
@Composable
private fun MemoryDetailScreenPreview() {
    HarmonyTheme {
        MemoryDetailScreen(
            onBackRequest = {},
            onRestartConversation = {},
            onShowConversation = {},
            uiState = MemoryDetailUiState(
                memoryCard = MemoryCard(
                    id = "1",
                    writerTitle = "손자",
                    writerName = "김민준",
                    text = "다은아 다은아 헌집 줄게 새집 다오...",
                    imageUrl = "",
                    dateTime = LocalDateTime.of(2024, 5, 4, 15, 0)
                )
            )
        )
    }
}