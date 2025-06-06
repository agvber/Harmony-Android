package com.teampatch.harmony

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.sdk.common.util.Utility
import com.teampatch.core.common.ActivitySavedInstanceHelper
import com.teampatch.core.designsystem.theme.HarmonyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var activitySavedInstanceHelper: ActivitySavedInstanceHelper

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setKeepOnSplashScreenCondition()
        onboardingStateRestore(savedInstanceState)
        initView()
        showHashKey()
    }

    private fun setKeepOnSplashScreenCondition() {
        val content: View = findViewById(android.R.id.content)
        val preDrawListener: ViewTreeObserver.OnPreDrawListener =
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    runBlocking { viewModel.uiState.first { !it.isLoading } }
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
            }
        content.viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

    private fun initView(): Unit = setContent {
        HarmonyTheme {
            val mainUiState by viewModel.uiState.collectAsStateWithLifecycle()
            MainApp(mainUiState)
        }
    }

    fun onboardingStateRestore(bundle: Bundle?) {
        bundle?.let { activitySavedInstanceHelper.restoreState(it) }
            ?: Log.d(TAG, "SavedInstanceState is null")
    }

    private fun showHashKey() {
        if (BuildConfig.DEBUG) {
            val keyHash = Utility.getKeyHash(this)
            Log.d(TAG, keyHash)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        activitySavedInstanceHelper.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}