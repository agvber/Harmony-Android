package com.teampatch.core.data.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.teampatch.core.data.di.annotation.MoshiAdapterType
import com.teampatch.core.data.di.annotation.MoshiType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object SerializationModule {

    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @MoshiType(moshiAdapterType = MoshiAdapterType.SET_STRING_ANY)
    @Provides
    fun providesMoshiSetStringAnyAdapter(
        moshi: Moshi
    ): JsonAdapter<Map<String, Any>> {
        return moshi.adapter<Map<String, Any>>(Map::class.java)
    }
}