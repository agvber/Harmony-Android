package com.teampatch.feature.question.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.teampatch.core.common.getOrNull
import com.teampatch.core.designsystem.R.drawable.ic_chevron_question
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.RoundButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeQuestions
import com.teampatch.core.domain.model.user.Role
import com.teampatch.feature.question.R
import com.teampatch.feature.question.main.model.QuestionSideEffect
import com.teampatch.feature.question.main.model.QuestionUiState
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun QuestionMainScreenWithViewModel(
    questionDetailPageRequest: (String) -> Unit,
    answerPageRequest: (String) -> Unit,
    questionExpandPageRequest: () -> Unit,
    questionMainViewModel: QuestionMainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by questionMainViewModel.questionUiState

    if (!uiState.isLoading) {
        QuestionMainScreen(
            questionDetailPageRequest = questionDetailPageRequest,
            answerPageRequest = answerPageRequest,
            questionExpandPageRequest = questionExpandPageRequest,
            uiState = uiState
        )
    }

    LaunchedEffect(Unit) {
        questionMainViewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is QuestionSideEffect.LoadError -> {
                    Toast.makeText(context, "데이터를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show()
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
) {
    val questions = uiState.question.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
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
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
        }
    ) { scaffoldPaddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPaddingValues)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .background(G1)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .background(WH, RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = "#${questions.getOrNull(0)?.number}",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = G4,
                            modifier = Modifier
                                .padding(top = 20.dp, start = 28.dp, end = 28.dp)
                        )
                        Text(
                            text = questions.getOrNull(0)?.title ?: "",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            color = BL,
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 14.dp, start = 28.dp, end = 28.dp)
                        )
                        RoundButton(
                            onClick = {
                                val id = questions.getOrNull(0)?.id ?: return@RoundButton
                                when (uiState.user.role) {
                                    Role.VIP -> answerPageRequest(id)
                                    Role.MEMBER -> questionDetailPageRequest(id)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                                .heightIn(min = 48.dp)
                        ) {
                            Text(
                                text = when (uiState.user.role) {
                                    Role.VIP -> stringResource(R.string.btn_answer_vip)
                                    Role.MEMBER -> stringResource(R.string.btn_answer_member)
                                },
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.text_title_question),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        color = BL
                    )
                    Text(
                        text = stringResource(R.string.text_detail_question),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = MainGreen,
                        modifier = Modifier
                            .noRippleClickable(onClick = questionExpandPageRequest)
                    )
                }
            }

            items(questions.itemCount) { index ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(G1, RoundedCornerShape(10.dp))
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                        .noRippleClickable {
                            val id = questions.getOrNull(index)?.id ?: return@noRippleClickable
                            questionDetailPageRequest(id)
                        }
                ) {
                    Text(
                        text = questions.getOrNull(index)?.title ?: "",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = BL,
                        modifier = Modifier.widthIn(max = 240.dp)
                    )
                    Icon(
                        painter = painterResource(ic_chevron_question),
                        contentDescription = "chevron",
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
            uiState = QuestionUiState(question = flowOf(PagingData.from(FakeQuestions().get())))
        )
    }
}