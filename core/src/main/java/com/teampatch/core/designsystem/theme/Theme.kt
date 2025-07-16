package com.teampatch.core.designsystem.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = MainGreen,
    secondary = Green2,
    background = WH,
    surfaceContainer = WH,
    surfaceContainerHigh = WH

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
     */
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonyTheme(
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalHarmonyTimPickerColors provides TimePickerDefaults.colors()
                    .transferHarmonyColors(),
                content = content
            )
        }
    )
}