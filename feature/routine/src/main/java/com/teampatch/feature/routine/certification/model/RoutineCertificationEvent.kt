package com.teampatch.feature.routine.certification.model

internal sealed interface RoutineCertificationEvent {
    data object InitLoadError : RoutineCertificationEvent
}