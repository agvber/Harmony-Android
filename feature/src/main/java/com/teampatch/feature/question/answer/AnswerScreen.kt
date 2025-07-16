package com.teampatch.feature.question.answer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.R.drawable.ic_more_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeQuestionDetail
import com.teampatch.feature.R
import com.teampatch.feature.question.answer.model.AnswerSideEffect
import com.teampatch.feature.question.answer.model.AnswerUiState

@Composable
internal fun AnswerRoute(
    onBackRequest: () -> Unit,
    onCompleteRequest: (String) -> Unit,
    viewModel: AnswerViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState: AnswerUiState by viewModel.answerUiState

    if (!uiState.isLoading) {
        AnswerScreen(
            onBackRequest = onBackRequest,
            onCompleteRequest = {
                viewModel.saveQuestionAnswer(it)
                onCompleteRequest(it)
            },
            uiState = uiState
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    is AnswerSideEffect.AddAnswerError -> {
                        Toast.makeText(context, "서버로 부터 데이터 전송 오류", Toast.LENGTH_SHORT).show()
                    }

                    is AnswerSideEffect.LoadError -> {
                        Toast.makeText(context, "데이터를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}

@Composable
internal fun AnswerScreen(
    onBackRequest: () -> Unit,
    onCompleteRequest: (String) -> Unit,
    uiState: AnswerUiState,
) {
    var answer by rememberSaveable { mutableStateOf(uiState.questionDetail.content) }

    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                actions = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 20.dp)
                            .noRippleClickable {
                            }
                    ) {
                        Image(
                            painter = painterResource(ic_more_question),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            DefaultButton(
                onClick = { onCompleteRequest(answer) },
                enabled = answer.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            ) {
                Text(stringResource(R.string.btn_complete_answer))
            }
        }
    ) { scaffoldPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
        ) {
            Text(
                text = stringResource(R.string.text_per_question, uiState.questionDetail.number),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = MainGreen,
                modifier = Modifier
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp)
            )
            Text(
                text = uiState.questionDetail.title,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = BL,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp, start = 20.dp, end = 20.dp)
            )
            Text(
                text = with(uiState.questionDetail.dateTime) {
                    stringResource(R.string.text_datetime_question, year, monthValue, dayOfMonth)
                },
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = G3,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 36.dp, bottom = 32.dp)
            ) {
                DefaultTextField(
                    value = answer,
                    onValueChange = {
                        if (it.length <= 200) {
                            answer = it
                        }
                    },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                    modifier = Modifier
                        .fillMaxSize()
                )
                Text(
                    text = stringResource(R.string.text_count_answer, answer.length),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = G4,
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnswerScreenPreview() {
    HarmonyTheme {
        AnswerScreen(
            onBackRequest = {},
            onCompleteRequest = {},
            uiState = AnswerUiState(
                questionDetail = FakeQuestionDetail().get()
            )
        )
    }
}