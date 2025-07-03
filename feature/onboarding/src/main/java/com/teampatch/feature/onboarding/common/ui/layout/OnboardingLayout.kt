package com.teampatch.feature.onboarding.common.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP36
import com.teampatch.core.designsystem.theme.EM1_4
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.SP28
import com.teampatch.feature.onboarding.R

@Composable
fun OnboardingLayout(
    title: AnnotatedString,
    subTitle: String,
    onBackRequest: () -> Unit,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                title = {},
                actions = {}
            )
        },
        bottomBar = {
            if (bottomBar != null) {
                bottomBar()
            }
        },
    ) { scaffoldPaddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
                .padding(horizontal = dimensionResource(R.dimen.padding_root_horizontal))
                .padding(top = DP12)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = SP28,
                lineHeight = EM1_4,
            )
            Text(
                text = subTitle,
                fontWeight = FontWeight.Medium,
                fontSize = SP18,
                lineHeight = EM1_4,
                color = G5,
                modifier = Modifier.padding(top = DP12, bottom = DP36)
            )
            content()
        }
    }

}