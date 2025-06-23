package com.teampatch.feature.question.detail.model

internal sealed interface QuestionDetailSideEffect {

    data class LoadError(val t: Throwable) : QuestionDetailSideEffect
    data class AddCommentError(val t: Throwable) : QuestionDetailSideEffect
    data class EditCommentError(val t: Throwable) : QuestionDetailSideEffect
    data class DeleteCommentError(val t: Throwable) : QuestionDetailSideEffect
}