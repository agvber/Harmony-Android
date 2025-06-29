package com.teampatch.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

const val SHARING_STARTED_TIME: Long = 5_000

fun <T> flowErrorCatch(
    block: () -> Flow<T>,
    action: suspend FlowCollector<T>.(cause: Throwable) -> Unit,
): Flow<T> = try {
    block()
        .catch(action)
} catch (e: Exception) {
    flow<T> { throw e }
        .catch(action)
}

inline fun <T> flowExceptionSafety(
    block: () -> Flow<T>
): Flow<T> {
    return try {
        block()
    } catch (e: Exception) {
        flow<T> { throw e }
    }
}