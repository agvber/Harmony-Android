package com.teampatch.core.designsystem.theme

import androidx.compose.material3.Typography

// Set of Material typography styles to start with
val Typography = Typography().let {
    it.copy(
        displayLarge = it.displayLarge.copy(fontFamily = PretendardFontFamily),
        displayMedium = it.displayMedium.copy(fontFamily = PretendardFontFamily),
        displaySmall = it.displaySmall.copy(fontFamily = PretendardFontFamily),
        headlineLarge = it.headlineLarge.copy(fontFamily = PretendardFontFamily),
        headlineMedium = it.headlineMedium.copy(fontFamily = PretendardFontFamily),
        headlineSmall = it.headlineSmall.copy(fontFamily = PretendardFontFamily),
        titleLarge = it.titleLarge.copy(fontFamily = PretendardFontFamily),
        titleMedium = it.titleMedium.copy(fontFamily = PretendardFontFamily),
        titleSmall = it.titleSmall.copy(fontFamily = PretendardFontFamily),
        bodyLarge = it.bodyLarge.copy(fontFamily = PretendardFontFamily),
        bodyMedium = it.bodyMedium.copy(fontFamily = PretendardFontFamily),
        bodySmall = it.bodySmall.copy(fontFamily = PretendardFontFamily),
        labelLarge = it.labelLarge.copy(fontFamily = PretendardFontFamily),
        labelMedium = it.labelMedium.copy(fontFamily = PretendardFontFamily),
        labelSmall = it.labelSmall.copy(fontFamily = PretendardFontFamily),
    )
}