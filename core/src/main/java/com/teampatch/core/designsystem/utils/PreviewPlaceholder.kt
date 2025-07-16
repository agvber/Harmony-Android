package com.teampatch.core.designsystem.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource

@Composable
fun previewPlaceholder(@DrawableRes resourceId: Int): Painter? {
    if (LocalInspectionMode.current) {
        return painterResource(resourceId)
    }
    return null
}