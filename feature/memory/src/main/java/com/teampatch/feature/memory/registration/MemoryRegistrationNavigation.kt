package com.teampatch.feature.memory.registration

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class MemoryRegistrationRoute(
    val memoryCardId: String,
)

fun NavController.navigateToMemoryRegistrationScreen(
    memoryCardId: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(MemoryRegistrationRoute(memoryCardId), navOptions, navigatorExtras)
}

fun NavGraphBuilder.addMemoryRegistrationScreen(
    onDismissRequest: () -> Unit,
    onMemoryStorePageRequest: () -> Unit,
) {
    composable<MemoryRegistrationRoute> {
        MemoryRegistrationRoute(
            onDismissRequest = onDismissRequest,
            onMemoryStorePageRequest = onMemoryStorePageRequest
        )
    }
}