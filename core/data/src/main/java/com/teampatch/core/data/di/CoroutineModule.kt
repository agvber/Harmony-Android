package com.teampatch.core.data.di

import com.teampatch.core.data.di.annotation.DispatchersContext
import com.teampatch.core.data.di.annotation.HarmonyDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineModule {

    @HarmonyDispatcher(DispatchersContext.Default)
    @Provides
    fun providesDefaultCoroutine(): CoroutineDispatcher = Dispatchers.Default

    @HarmonyDispatcher(DispatchersContext.IO)
    @Provides
    fun providesIoCoroutine(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun providesCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}