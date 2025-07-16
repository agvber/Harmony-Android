package com.teampatch.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun CoroutineScope.launchWithCatch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline catch: suspend (Throwable) -> Unit = {},
    crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context, start) {
        runCatching { block() }
            .onFailure { it.printStackTrace(); catch(it) }
    }
}