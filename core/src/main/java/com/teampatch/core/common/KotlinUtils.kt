package com.teampatch.core.common

inline fun <T : Any?> T.takeIfNull(
    action: () -> Unit,
): Any = this ?: action()

inline fun <T, R> Iterable<T>.set(transform: (T) -> R): Set<R> {
    return mutableSetOf<R>().also { set -> forEach { set.add(transform(it)) } }
}