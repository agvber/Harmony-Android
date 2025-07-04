package com.teampatch.core.designsystem.theme

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

val EnterVisibilityAnimation = fadeIn(tween(600))
val ExitVisibilityAnimation = fadeOut(tween(600))

val FloatingButtonEnterVisibilityAnimation = fadeIn(tween(400))
val FloatingButtonExitVisibilityAnimation = fadeOut(tween(600))