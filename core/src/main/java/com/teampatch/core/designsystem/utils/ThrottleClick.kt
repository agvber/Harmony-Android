package com.teampatch.core.designsystem.utils

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.throttleClick(
    throttleTime: Long = 1000L,
    onClick: () -> Unit,
): Modifier {
    var lastClickTime = 0L
    return clickable {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= throttleTime) {
            onClick()
            lastClickTime = currentTime
        }
    }
}