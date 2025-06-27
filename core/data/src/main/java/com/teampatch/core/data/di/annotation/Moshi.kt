package com.teampatch.core.data.di.annotation

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MoshiType(val moshiAdapterType: MoshiAdapterType)

enum class MoshiAdapterType {
    SET_STRING_ANY
}