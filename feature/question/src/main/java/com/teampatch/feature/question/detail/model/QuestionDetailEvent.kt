package com.teampatch.feature.question.detail.model

internal sealed interface QuestionDetailEvent {
    data class LoadError(val t: Throwable) : QuestionDetailEvent
    data class AddCommentError(val t: Throwable) : QuestionDetailEvent
    data class EditCommentError(val t: Throwable) : QuestionDetailEvent
    data class DeleteCommentError(val t: Throwable) : QuestionDetailEvent
}