package com.teampatch.core.data.network.di

import com.teampatch.core.data.network.UserRemoteDataSource
import com.teampatch.core.data.network.impl.UserRemoteDataSourceImpl
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