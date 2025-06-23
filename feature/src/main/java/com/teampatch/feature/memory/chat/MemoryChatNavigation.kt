package com.teampatch.feature.memory.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class MemoryChatRoute(val memoryCardId: String)

fun NavController.navigateToMemoryChatScreen(
    memoryCardId: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navigate(
        route = MemoryChatRoute(memoryCardId),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}

fun NavGraphBuilder.addMemoryChatScreen(
    onCloseRequest: () -> Unit,
    onReplyChat: (memoryCardId: String) -> Unit,
) {
    composable<MemoryChatRoute> {
        MemoryChatScreenWithViewModel(
            onCloseRequest = onCloseRequest,
            onReplyChat = onReplyChat
        )
    }
}