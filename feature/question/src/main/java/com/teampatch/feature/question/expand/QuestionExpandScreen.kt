package com.teampatch.feature.question.expand

import android.content.Context
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teampatch.core.designsystem.R.drawable.ic_chevron_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP240
import com.teampatch.core.designsystem.theme.DP28
import com.teampatch.core.designsystem.theme.DP4
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeQuestions
import com.teampatch.core.domain.model.question.Question
import com.teampatch.feature.question.R
import com.teampatch.feature.question.expand.model.QuestionExpandEvent

@Composable
internal fun QuestionExpandRoute(
    onBackRequest: () -> Unit,
    questionDetailPageRequest: (String) -> Unit,
    viewModel: QuestionExpandViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val questions: List<Question> by viewModel.questions.collectAsStateWithLifecycle()

    QuestionExpandScreen(
        onBackRequest = onBackRequest,
        questionDetailPageRequest = questionDetailPageRequest,
        questions = questions
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is QuestionExpandEvent.LoadError ->
                    Toast.makeText(
                        context,
                        context.getString(R.string.question_main_toast_init_data_load_error),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }
}

@Composable
internal fun QuestionExpandScreen(
    onBackRequest: () -> Unit,
    questionDetailPageRequest: (String) -> Unit,
    questions: List<Question>
) {
    Column {
        BackButtonAppBar(
            onBackRequest = onBackRequest,
            title = { Text(stringResource(R.string.question_main_text_title)) }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = DP16)
        ) {
            items(questions) { currentItem ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DP20, end = DP20, bottom = DP12)
                        .background(G1, RoundedCornerShape10)
                        .noRippleClickable { questionDetailPageRequest(currentItem.id) }
                ) {
                    Text(
                        text = stringResource(
                            R.string.question_main_text_count,
                            currentItem.number
                        ),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = SP18,
                        color = G4,
                        modifier = Modifier
                            .padding(top = DP20, start = DP28, bottom = DP4)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = DP24, end = DP24, bottom = DP16)
                    ) {
                        Text(
                            text = currentItem.title,
                            fontFamily = PretendardFontFamily,
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
}

@Preview
@Composable
private fun QuestionExpandScreenPreview() {
    HarmonyTheme {
        QuestionExpandScreen(
            onBackRequest = {},
            questionDetailPageRequest = {},
            questions = FakeQuestions().get()
        )
    }
}