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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.teampatch.core.designsystem.component.DefaultBottomNavigation
import com.teampatch.core.designsystem.component.NavigationItem
import com.teampatch.feature.routine.main.DailyMainRoute
import com.teampatch.feature.routine.main.navigateToDailyMainScreen
import com.teampatch.feature.home.HomeRoute
import com.teampatch.feature.home.navigateToHomeScreen
import com.teampatch.feature.memory.storage.MemoryStorageRoute
import com.teampatch.feature.memory.storage.navigateToMemoryStorageScreen
import com.teampatch.feature.question.main.QuestionMainRoute
import com.teampatch.feature.question.main.navigateToQuestionMainScreen
import com.teampatch.harmony.model.MainUiState

private val BottomNavigationEnableScreens: Set<String?> = setOf(
    HomeRoute::class.qualifiedName,
    MemoryStorageRoute::class.qualifiedName,
    QuestionMainRoute::class.qualifiedName,
    DailyMainRoute::class.qualifiedName
)

@Composable
fun MainApp(
    mainUiState: MainUiState,
    navController: NavHostController = rememberNavController(),
) {
    val currentBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsStateWithLifecycle()
    var previousNavigationItem by rememberSaveable { mutableStateOf(NavigationItem.HOME) }
    val navigationItem: NavigationItem by remember(currentBackStackEntry) {
        mutableStateOf(
            handleNavigationItem(
                route = currentBackStackEntry?.destination?.route,
                previousNavigationItem = previousNavigationItem,
                onNavigationItemChange = { previousNavigationItem = it }
            )
        )
    }
    val isBottomNavigationShow: Boolean by remember(currentBackStackEntry) {
        derivedStateOf { currentBackStackEntry?.destination?.route in BottomNavigationEnableScreens }
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
                            NavigationItem.QUESTION -> navController.navigateToQuestionMainScreen()
                            NavigationItem.DAILY -> navController.navigateToDailyMainScreen()
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
}

private fun handleNavigationItem(
    route: String?,
    previousNavigationItem: NavigationItem,
    onNavigationItemChange: (NavigationItem) -> Unit
): NavigationItem = when (route) {
    HomeRoute::class.qualifiedName -> {
        NavigationItem.HOME.apply {
            onNavigationItemChange(this)
        }
    }

    MemoryStorageRoute::class.qualifiedName -> {
        NavigationItem.STORE.apply {
            onNavigationItemChange(this)
        }
    }

    QuestionMainRoute::class.qualifiedName -> {
        NavigationItem.QUESTION.apply {
            onNavigationItemChange(this)
        }
    }

    DailyMainRoute::class.qualifiedName -> {
        NavigationItem.DAILY.apply {
            onNavigationItemChange(this)
        }
    }

    else -> previousNavigationItem
}