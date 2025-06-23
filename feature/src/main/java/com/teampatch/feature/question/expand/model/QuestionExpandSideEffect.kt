package com.teampatch.feature.question.expand.model

internal sealed interface QuestionExpandSideEffect {

    data class LoadError(val t: Throwable) : QuestionExpandSideEffect
}