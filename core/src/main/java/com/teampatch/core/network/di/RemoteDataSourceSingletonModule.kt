package com.teampatch.core.network.di

import com.teampatch.core.network.UserRemoteDataSource
import com.teampatch.core.network.impl.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteDataSourceSingletonModule {

    @Binds
    abstract fun providesUserRemoteDataSource(
        userRemoteDataSourceImpl: UserRemoteDataSourceImpl,
    ): UserRemoteDataSource
}