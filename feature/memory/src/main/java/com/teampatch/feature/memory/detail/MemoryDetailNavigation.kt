package com.teampatch.feature.memory.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class MemoryDetailRoute(
    val memoryCardId: String
)

fun NavController.navigateToMemoryDetailScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {

}

fun NavGraphBuilder.addMemoryDetailScreen() {
    composable<MemoryDetailRoute> {

    }
}