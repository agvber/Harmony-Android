package com.teampatch.feature.question.expand.model

internal sealed interface QuestionExpandEvent {

    data class LoadError(val t: Throwable) : QuestionExpandEvent
}