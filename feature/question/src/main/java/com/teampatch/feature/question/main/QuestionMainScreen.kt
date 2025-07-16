package com.teampatch.feature.question.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teampatch.core.designsystem.R.drawable.ic_chevron_question
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.RoundButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP14
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP240
import com.teampatch.core.designsystem.theme.DP28
import com.teampatch.core.designsystem.theme.DP4
import com.teampatch.core.designsystem.theme.DP48
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PaddingContentHorizontal
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.theme.SP22
import com.teampatch.core.designsystem.theme.SP24
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeQuestions
import com.teampatch.core.domain.model.question.Question
import com.teampatch.core.domain.model.user.Role
import com.teampatch.feature.question.R
import com.teampatch.feature.question.main.model.QuestionMainEvent
import com.teampatch.feature.question.main.model.QuestionUiState

@Composable
internal fun QuestionMainScreenWithViewModel(
    questionDetailPageRequest: (String) -> Unit,
    answerPageRequest: (String) -> Unit,
    questionExpandPageRequest: () -> Unit,
    questionMainViewModel: QuestionMainViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val uiState: QuestionUiState by questionMainViewModel.uiState.collectAsStateWithLifecycle()
    val questions: List<Question> by questionMainViewModel.questions.collectAsStateWithLifecycle()

    if (!uiState.isLoading) {
        QuestionMainScreen(
            questionDetailPageRequest = questionDetailPageRequest,
            answerPageRequest = answerPageRequest,
            questionExpandPageRequest = questionExpandPageRequest,
            uiState = uiState,
            questions = questions
        )
    }

    LaunchedEffect(Unit) {
        questionMainViewModel.event.collect { sideEffect ->
            when (sideEffect) {
                is QuestionMainEvent.LoadError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.question_main_toast_init_data_load_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
internal fun QuestionMainScreen(
    questionDetailPageRequest: (String) -> Unit,
    answerPageRequest: (String) -> Unit,
    questionExpandPageRequest: () -> Unit,
    uiState: QuestionUiState,
    questions: List<Question>
) {

    Column {
        AppBar(
            navigation = {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = BL)) {
                            append(stringArrayResource(R.array.text_title_appbar)[0])
                        }
                        withStyle(style = SpanStyle(color = MainGreen)) {
                            append(stringArrayResource(R.array.text_title_appbar)[1])
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = SP22,
                    modifier = Modifier
                        .padding(horizontal = PaddingContentHorizontal)
                )
            }
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(DP12),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(G1)
                        .padding(vertical = DP16, horizontal = PaddingContentHorizontal)
                        .background(WH, RoundedCornerShape10)
                ) {
                    Text(
                        text = stringResource(
                            R.string.question_main_text_count,
                            questions.getOrNull(0)?.number ?: 0
                        ),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = SP18,
                        color = G4,
                        modifier = Modifier
                            .padding(top = DP20, start = DP28, end = DP28)
                    )
                    Text(
                        text = questions.getOrNull(0)?.title ?: "",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = SP24,
                        color = BL,
                        modifier = Modifier
                            .padding(top = DP4, bottom = DP14, start = DP28, end = DP28)
                    )
                    RoundButton(
                        onClick = {
                            questions.getOrNull(0)?.id?.let { id ->
                                when (uiState.role) {
                                    Role.VIP -> answerPageRequest(id)
                                    Role.MEMBER -> questionDetailPageRequest(id)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = DP20, end = DP20, bottom = DP12)
                            .heightIn(min = DP48)
                    ) {
                        Text(
                            text = when (uiState.role) {
                                Role.VIP -> stringResource(R.string.question_main_button_answer_write)
                                Role.MEMBER -> stringResource(R.string.question_main_button_answer_read)
                            },
                            fontSize = SP20
                        )
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DP20, end = DP20, top = DP24, bottom = DP12)
                ) {
                    Text(
                        text = stringResource(R.string.question_main_text_title),
                        fontWeight = FontWeight.Medium,
                        fontSize = SP22,
                        color = BL
                    )
                    Text(
                        text = stringResource(R.string.question_main_button_question_detail),
                        fontWeight = FontWeight.Medium,
                        fontSize = SP18,
                        color = MainGreen,
                        modifier = Modifier
                            .noRippleClickable(onClick = questionExpandPageRequest)
                    )
                }
            }
            items(items = questions, key = { it.id }) { currentItem ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DP20)
                        .background(G1, RoundedCornerShape10)
                        .padding(vertical = DP16, horizontal = DP24)
                        .noRippleClickable { questionDetailPageRequest(currentItem.id) }
                ) {
                    Text(
                        text = currentItem.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = SP20,
                        color = BL,
                        modifier = Modifier.widthIn(max = DP240)
                    )
                    Icon(
                        painter = painterResource(ic_chevron_question),
                        contentDescription = stringResource(R.string.question_main_image_right_chevron),
                        tint = G3
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuestionMainScreenPreview() {
    HarmonyTheme {
        QuestionMainScreen(
            questionExpandPageRequest = { },
            answerPageRequest = {},
            questionDetailPageRequest = {},
            uiState = QuestionUiState(),
            questions = FakeQuestions().get()
        )
    }
}