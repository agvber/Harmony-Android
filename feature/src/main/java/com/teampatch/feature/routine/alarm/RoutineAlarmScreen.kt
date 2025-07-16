package com.teampatch.feature.routine.alarm

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.teampatch.core.R.drawable.ic_harmony_anticipation
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DefaultButton
import com.teampatch.core.designsystem.component.DefaultButtonColor
import com.teampatch.core.designsystem.component.drawSpeechBubble
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP40
import com.teampatch.core.designsystem.theme.DP52
import com.teampatch.core.designsystem.theme.DP60
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.SP28
import com.teampatch.feature.R
import com.teampatch.feature.routine.toStringFormat
import java.time.LocalTime

@Composable
internal fun RoutineAlarmScreen(
    onLaterTaskRequest: () -> Unit,
    onHistoryPageRequest: () -> Unit,
    time: LocalTime,
    title: String
) {
    val context: Context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AppBar(title = { Text(text = stringResource(id = R.string.routine_alarm_appbar_title)) })
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = DP20)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawSpeechBubble()
                        .padding(vertical = DP52)
                ) {
                    Text(
                        text = time.toStringFormat(context),
                        fontWeight = FontWeight.Medium,
                        color = MainGreen,
                        fontSize = SP28
                    )
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        color = BL,
                        fontSize = SP28,
                        style = TextStyle(
                            lineBreak = LineBreak.Heading,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .padding(top = DP24)
                    )
                }
                Image(
                    painter = painterResource(ic_harmony_anticipation),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = DP40)
                )
            }
            DefaultButton(
                onClick = onHistoryPageRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DP60)
            ) {
                Text(stringResource(id = R.string.routine_alarm_button_now_history))
            }
            DefaultButton(
                onClick = onLaterTaskRequest,
                color = DefaultButtonColor(containerColor = BL),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DP12, bottom = DP8)
            ) {
                Text(stringResource(id = R.string.routine_alarm_button_later_history))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoutineAlarmScreenPreview() {
    HarmonyTheme {
        RoutineAlarmScreen(
            onHistoryPageRequest = {},
            onLaterTaskRequest = {},
            time = LocalTime.of(14, 30),
            title = "공원 산책 가서 비둘기 사진 찍기"
        )
    }
}