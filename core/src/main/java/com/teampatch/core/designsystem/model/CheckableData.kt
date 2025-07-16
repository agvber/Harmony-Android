package com.teampatch.core.designsystem.model

import androidx.compose.runtime.MutableState

data class CheckableData<T>(
    val data: T,
    var checked: MutableState<Boolean>,
)