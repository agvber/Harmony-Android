package com.teampatch.feature.question.main.model

internal sealed interface QuestionMainEvent {

    data class LoadError(val t: Throwable) : QuestionMainEvent
}