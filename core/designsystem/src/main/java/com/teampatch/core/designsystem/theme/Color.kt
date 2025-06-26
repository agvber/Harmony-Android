package com.teampatch.core.designsystem.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerColors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val MainGreen = Color(0xFF00C573)
val Green2 = Color(0xFFDEF7E8)
val SubRed = Color(0xFFDD4A4A)
val BL = Color(0xFF363232)
val WH = Color(0xFFFFFFFF)
val G1 = Color(0xFFF5F5F1)
val G2 = Color(0xFFE6E3DD)
val G3 = Color(0xFFC0BEB6)
val G4 = Color(0xFF9B9B97)
val G5 = Color(0xFF7A7971)

@OptIn(ExperimentalMaterial3Api::class)
val LocalHarmonyTimPickerColors = staticCompositionLocalOf<TimePickerColors> {
    error("CompositionLocal LocalHarmonyTimPickerColors not present")
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerColors.transferHarmonyColors(): TimePickerColors {
    return copy(
        containerColor = G1,
        timeSelectorSelectedContainerColor = Green2,
        timeSelectorUnselectedContainerColor = G2,
        clockDialColor = G1,
        periodSelectorSelectedContainerColor = G3
    )
}