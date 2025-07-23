package com.teampatch.harmony.routine.di

import com.teampatch.core.domain.entities.RoutineAlarmManager
import com.teampatch.harmony.routine.entities.RoutineAlarmManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RoutineAlarmModule {

    @Binds
    abstract fun bindsRoutineAlarm(
        routineAlarmManagerImpl: RoutineAlarmManagerImpl
    ): RoutineAlarmManager
}