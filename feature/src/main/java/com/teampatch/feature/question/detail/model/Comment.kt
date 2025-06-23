package com.teampatch.feature.question.detail.model

data class Comment(
    val id: String,
    val content: String,
    val writer: Writer,
    val hasWritePermission: Boolean,
) {
    data class Writer(
        val uid: String,
        val name: String,
    )
}