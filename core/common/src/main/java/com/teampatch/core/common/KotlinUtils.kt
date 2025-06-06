package com.teampatch.core.common

inline fun <T : Any?> T.takeIfNull(
    action: () -> Unit,
): Any = this ?: action()