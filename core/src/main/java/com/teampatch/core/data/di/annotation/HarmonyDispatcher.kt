package com.teampatch.core.data.di.annotation

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HarmonyDispatcher(val dispatchersContext: DispatchersContext)

enum class DispatchersContext {
    Default,
    IO,
}