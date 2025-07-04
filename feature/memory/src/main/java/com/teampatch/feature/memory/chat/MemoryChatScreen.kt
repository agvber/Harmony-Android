package com.teampatch.feature.memory.chat

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.teampatch.core.designsystem.R.drawable.ic_close_memory_card
import com.teampatch.core.designsystem.R.drawable.ic_profile_image_harmony
import com.teampatch.core.designsystem.R.drawable.img_test_memory_card
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP0
import com.teampatch.core.designsystem.theme.DP1
import com.teampatch.core.designsystem.theme.DP10
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP120
import com.teampatch.core.designsystem.theme.DP156
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP252
import com.teampatch.core.designsystem.theme.DP28
import com.teampatch.core.designsystem.theme.DP280
import com.teampatch.core.designsystem.theme.DP40
import com.teampatch.core.designsystem.theme.DP54
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.FloatingButtonEnterVisibilityAnimation
import com.teampatch.core.designsystem.theme.FloatingButtonExitVisibilityAnimation
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.Green2
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PaddingContentHorizontal
import com.teampatch.core.designsystem.theme.RoundedCornerShape999
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.previewPlaceholder
import com.teampatch.core.domain.model.user.Role
import com.teampatch.feature.memory.R
import com.teampatch.feature.memory.chat.model.MemoryChatEvent
import com.teampatch.feature.memory.chat.model.MemoryChatUiState
import java.time.LocalDate

@Composable
internal fun MemoryChatScreenWithViewModel(
    onCloseRequest: () -> Unit,
    onReplyChat: (memoryCardId: String) -> Unit,
    viewModel: MemoryChatViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState: MemoryChatUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MemoryChatScreen(
        onCloseRequest = onCloseRequest,
        onReplyChat = { onReplyChat(uiState.id) },
        uiState = uiState
    )

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    MemoryChatEvent.LoadError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_data_load_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}

@Composable
private fun MemoryChatScreen(
    onCloseRequest: () -> Unit,
    onReplyChat: () -> Unit,
    uiState: MemoryChatUiState
) {
    val chatScrollState: ScrollState = rememberScrollState()
    val isScrollable: Boolean by remember {
        derivedStateOf {
            chatScrollState.canScrollForward && chatScrollState.canScrollBackward
        }
    }
    val isFABVisible: Boolean by remember(chatScrollState) {
        derivedStateOf {
            if (!isScrollable) return@derivedStateOf uiState.role == Role.VIP
            uiState.role == Role.VIP &&
                    chatScrollState.canScrollForward &&
                    !chatScrollState.isScrollInProgress
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WH)
    ) {
        AppBar(
            title = { Text(text = uiState.title) },
            actions = {
                IconButton(
                    onClick = onCloseRequest,
                    modifier = Modifier.padding(end = DP20)
                ) {
                    Icon(
                        painter = painterResource(ic_close_memory_card),
                        contentDescription = stringResource(R.string.button_close_content_description)
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = chatScrollState)
                .background(G1)
                .padding(horizontal = PaddingContentHorizontal)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DP24)
            ) {
                Text(
                    text = with(uiState.date) {
                        stringResource(
                            R.string.text_memory_card_date_format,
                            year,
                            monthValue,
                            dayOfMonth
                        )
                    },
                    color = G5,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape999)
                        .background(G2)
                        .padding(vertical = DP10, horizontal = DP40)
                )
            }

            Row {
                IconButton(
                    onClick = {}
                ) {
                    Image(
                        painter = painterResource(ic_profile_image_harmony),
                        contentDescription = stringResource(R.string.image_profile_content_description),
                        modifier = Modifier
                            .size(DP54)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = DP12, top = DP28)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = DP12)
                            .then(QuestionSpeechBubbleModifier)
                    ) {
                        AsyncImage(
                            model = uiState.imageUrl,
                            contentDescription = stringResource(R.string.image_question_content_description),
                            placeholder = previewPlaceholder(img_test_memory_card),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(DP252, DP156)
                        )
                    }
                    Box(modifier = QuestionSpeechBubbleModifier) {
                        Text(
                            text = uiState.question,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = BL
                        )
                    }
                }
            }
            Box(
                modifier = AnswerSpeechBubbleModifier
                    .align(Alignment.End)
            ) {
                Text(
                    text = uiState.answer ?: "",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = BL
                )
            }
            Box(modifier = Modifier.height(DP120))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = isFABVisible,
            enter = FloatingButtonEnterVisibilityAnimation,
            exit = FloatingButtonExitVisibilityAnimation,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = DP8)
        ) {
            DefaultButton(
                onClick = onReplyChat,
                shape = RoundedCornerShape999,
                contentPaddingValues = PaddingValues(
                    vertical = DP20,
                    horizontal = DP40
                )
            ) {
                Text(stringResource(R.string.button_chat_retry))
            }
        }
    }
}

private val SpeechBubbleRoundedCornerShape: RoundedCornerShape =
    RoundedCornerShape(topStart = DP0, topEnd = DP20, bottomEnd = DP20, bottomStart = DP20)

private val AnswerSpeechBubbleRoundedCornerShape: RoundedCornerShape =
    RoundedCornerShape(topStart = DP20, topEnd = DP0, bottomEnd = DP20, bottomStart = DP20)

private val QuestionSpeechBubbleModifier: Modifier = Modifier
    .background(WH, SpeechBubbleRoundedCornerShape)
    .border(width = DP1, color = G3, SpeechBubbleRoundedCornerShape)
    .padding(DP16)
    .widthIn(max = DP280)

private val AnswerSpeechBubbleModifier: Modifier = Modifier
    .padding(top = DP20)
    .background(Green2, AnswerSpeechBubbleRoundedCornerShape)
    .border(
        width = DP1,
        color = MainGreen,
        shape = AnswerSpeechBubbleRoundedCornerShape
    )
    .padding(DP16)
    .widthIn(max = DP280)

@Preview
@Composable
private fun MemoryChatScreenVipPreview() {
    HarmonyTheme {
        MemoryChatScreen(
            onCloseRequest = {},
            onReplyChat = {},
            uiState = MemoryChatUiState(
                title = "다은이 태어난 날",
                question = "다은이를 분만실에서 처음 봤을 때 어떤 느낌이 들었나요?",
                imageUrl = null,
                answer = "너무 사랑스러웠단다. 내 소중한 손녀 딸을 보고 싶었거든 어쩌구 저쩌구 그래서 울산 병원에서 어쩌구 저쩌구",
                date = LocalDate.now(),
                role = Role.VIP
            )
        )
    }
}

@Preview
@Composable
private fun MemoryChatScreenMemberPreview() {
    HarmonyTheme {
        MemoryChatScreen(
            onCloseRequest = {},
            onReplyChat = {},
            uiState = MemoryChatUiState(
                title = "다은이 태어난 날",
                question = "다은이를 분만실에서 처음 봤을 때 어떤 느낌이 들었나요?",
                imageUrl = null,
                answer = "너무 사랑스러웠단다. 내 소중한 손녀 딸을 보고 싶었거든 어쩌구 저쩌구 그래서 울산 병원에서 어쩌구 저쩌구",
                date = LocalDate.now(),
                role = Role.MEMBER
            )
        )
    }
}