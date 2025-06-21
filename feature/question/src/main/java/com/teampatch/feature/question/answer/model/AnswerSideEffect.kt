package com.teampatch.feature.question.answer.model

internal sealed interface AnswerSideEffect {

    data class LoadError(val t: Throwable) : AnswerSideEffect
    data class AddAnswerError(val t: Throwable) : AnswerSideEffect
}