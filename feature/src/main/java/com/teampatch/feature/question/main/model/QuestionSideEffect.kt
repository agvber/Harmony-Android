package com.teampatch.feature.question.main.model

internal sealed interface QuestionSideEffect {

    data class LoadError(val t: Throwable) : QuestionSideEffect
}