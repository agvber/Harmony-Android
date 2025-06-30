package com.teampatch.feature.memory.creation.model

import android.net.Uri
import java.time.LocalDate

internal data class MemoryCreationUiState(
    val imageUri: Uri? = null,
    val title: String = "",
    val date: LocalDate = LocalDate.now(),
) {

    fun checkNextProcess(): Boolean =
        title.isNotBlank() && imageUri != null
}