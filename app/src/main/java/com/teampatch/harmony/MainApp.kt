package com.teampatch.harmony

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.teampatch.core.designsystem.component.DefaultBottomNavigation
import com.teampatch.core.designsystem.component.NavigationItem
import com.teampatch.feature.home.HomeRoute
import com.teampatch.feature.home.navigateToHomeScreen
import com.teampatch.feature.memorystorage.MemoryStorageRoute
import com.teampatch.feature.memorystorage.navigateToMemoryStorageScreen
import com.teampatch.feature.onboarding.login.navigateToOnboardingScreen
import com.teampatch.feature.question.QuestionRoute
import com.teampatch.feature.question.navigateToQuestionScreen
import com.teampatch.harmony.model.MainUiState

private val BottomNavigationEnableScreens: Set<String?> = setOf(
    HomeRoute::class.qualifiedName,
    MemoryStorageRoute::class.qualifiedName,
    QuestionRoute::class.qualifiedName,
    DailyRoute::class.qualifiedName
)

@Composable
fun MainApp(
    mainUiState: MainUiState,
    navController: NavHostController = rememberNavController(),
) {
    val currentBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(
        initialValue = null
    )
    var previousNavigationItem by rememberSaveable { mutableStateOf(NavigationItem.HOME) }
    val navigationItem: NavigationItem by remember(currentBackStackEntry) {
        mutableStateOf(
            when (currentBackStackEntry?.destination?.route) {
                HomeRoute::class.qualifiedName -> {
                    NavigationItem.HOME.apply {
                        previousNavigationItem = this
                    }
                }

                MemoryStorageRoute::class.qualifiedName -> {
                    NavigationItem.STORE.apply {
                        previousNavigationItem = this
                    }
                }

                QuestionRoute::class.qualifiedName -> {
                    NavigationItem.QUESTION.apply {
                        previousNavigationItem = this
                    }
                }

                DailyRoute::class.qualifiedName -> {
                    NavigationItem.DAILY.apply {
                        previousNavigationItem = this
                    }
                }

                else -> previousNavigationItem
            }
        )
    }
    val isBottomNavigationShow: Boolean by remember(currentBackStackEntry) {
        derivedStateOf {
            currentBackStackEntry?.destination?.route in BottomNavigationEnableScreens
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomNavigationShow,
                enter = fadeIn(tween(400)),
                exit = fadeOut(tween(400))
            ) {
                DefaultBottomNavigation(
                    onClick = {
                        when (it) {
                            NavigationItem.HOME -> navController.navigateToHomeScreen()
                            NavigationItem.STORE -> navController.navigateToMemoryStorageScreen()
                            NavigationItem.QUESTION -> navController.navigateToQuestionScreen()
                            NavigationItem.DAILY -> navController.navigateToDailyScreen()
                        }
                    },
                    navigationItem = navigationItem
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .safeDrawingPadding()
    ) { scaffoldPaddingValue ->
        MainNavHost(
            mainUiState = mainUiState,
            navController = navController,
            modifier = Modifier.padding(scaffoldPaddingValue)
        )
    }

    LaunchedEffect(mainUiState.isLoginRequired) {
        if (mainUiState.isLoginRequired) {
            navController.navigateToOnboardingScreen()
        }
    }
}