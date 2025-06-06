package com.teampatch.harmony

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.teampatch.core.domain.entities.TokenManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TokenInvalidInstrumentTest {

    @get:Rule(order = 0)
    var hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var instrumentation: Instrumentation
    private lateinit var context: Context

    @Before
    fun initTest() {
        instrumentation = InstrumentationRegistry.getInstrumentation()
        context = instrumentation.targetContext
        hiltRule.inject()
    }

    @Test
    fun `토큰_만료_화면_이동_테스트`() {
        tokenManager.setAccessToken("invalid_token")

        TestScope().launch {
            val isTokenInvalid = tokenManager.isTokenInvalidListener.first { it }
            Assert.assertTrue(isTokenInvalid)
        }

        startMainActivity()
    }

    @Test
    fun `처음_사용자_온보딩_화면_테스트`() {
        tokenManager.setAccessToken("")

        val composeRule = createAndroidComposeRule<MainActivity>()
        composeRule.onNodeWithText("harmony")
    }

    private fun startMainActivity() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}