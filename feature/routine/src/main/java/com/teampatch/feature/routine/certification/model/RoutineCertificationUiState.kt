package com.teampatch.feature.routine.certification.model

import android.net.Uri
import java.time.LocalTime

data class RoutineCertificationUiState(
    val routineTitle: String = "",
    val routineTime: LocalTime = LocalTime.MIN,
    val certificationImage: Uri? = null,
)