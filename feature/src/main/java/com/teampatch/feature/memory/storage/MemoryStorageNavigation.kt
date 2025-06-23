package com.teampatch.feature.memory.storage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
data object MemoryStorageRoute

fun NavController.navigateToMemoryStorageScreen(
    navOptions: NavOptions? = navOptions { launchSingleTop = true },
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(MemoryStorageRoute, navOptions, navigatorExtras)
}

fun NavGraphBuilder.addMemoryStorageScreen(
    onDetailPageRequest: (memoryCardId: String) -> Unit,
) {
    composable<MemoryStorageRoute> {
        MemoryStorageWithViewModel(
            onDetailPageRequest = onDetailPageRequest
        )
    }
}