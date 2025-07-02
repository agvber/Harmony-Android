package com.teampatch.core.domain.model

data class TaskProgress(
    val totalCount: Int,
    val finishedCount: Int,
) {

    val progress: Float = (finishedCount.toFloat() / totalCount.toFloat())
        .let { progress ->
            when {
                progress.isNaN() -> 0f
                progress.isInfinite() -> 1f
                else -> progress
            }
        }
}