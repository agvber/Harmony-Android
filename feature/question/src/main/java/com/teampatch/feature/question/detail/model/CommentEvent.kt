package com.teampatch.feature.question.detail.model

internal sealed interface CommentEvent {
    data class Add(val commentText: String) : CommentEvent
    data class Edit(val comment: Comment, val commentText: String) : CommentEvent
    data class Delete(val comment: Comment) : CommentEvent
}