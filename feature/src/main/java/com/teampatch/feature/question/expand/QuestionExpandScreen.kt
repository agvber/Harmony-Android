package com.teampatch.feature.question.expand

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.teampatch.core.common.getOrNull
import com.teampatch.core.designsystem.R.drawable.ic_chevron_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeQuestions
import com.teampatch.feature.R
import com.teampatch.feature.question.expand.model.QuestionExpandSideEffect
import com.teampatch.feature.question.expand.model.QuestionExpandUiState
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun QuestionExpandRoute(
    onBackRequest: () -> Unit,
    questionDetailPageRequest: (String) -> Unit,
    viewModel: QuestionExpandViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState: QuestionExpandUiState by viewModel.questionExpandUiState

    if (!uiState.isLoading) {
        QuestionExpandScreen(
            onBackRequest = onBackRequest,
            questionDetailPageRequest = questionDetailPageRequest,
            uiState = uiState
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is QuestionExpandSideEffect.LoadError ->
                    Toast.makeText(context, "데이터를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
internal fun QuestionExpandScreen(
    onBackRequest: () -> Unit,
    questionDetailPageRequest: (String) -> Unit,
    uiState: QuestionExpandUiState,
) {
    val questions = uiState.question.collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                title = {
                    Text(stringResource(R.string.appbar_question_expand_title))
                }
            )
        }
    ) { scaffoldPaddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
                .padding(top = 16.dp)
        ) {
            items(questions.itemCount) { index ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                        .background(G1, RoundedCornerShape(10.dp))
                        .noRippleClickable {
                            val id = questions.getOrNull(index)?.id ?: return@noRippleClickable
                            questionDetailPageRequest(id)
                        }
                ) {
                    Text(
                        text = "#${questions.getOrNull(index)?.number}",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = G4,
                        modifier = Modifier
                            .padding(top = 20.dp, start = 28.dp, bottom = 4.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
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
}

@Preview
@Composable
private fun QuestionExpandScreenPreview() {
    HarmonyTheme {
        QuestionExpandScreen(
            onBackRequest = {},
            questionDetailPageRequest = {},
            uiState = QuestionExpandUiState(question = flowOf(PagingData.from(FakeQuestions().get())))
        )
    }
}