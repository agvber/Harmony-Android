package com.teampatch.core.data.di

import com.teampatch.core.data.datasource.AuthenticationLocalDatasource
import com.teampatch.core.data.datasource.AuthenticationLocalDatasourceImpl
import com.teampatch.core.data.entity.TokenManagerImpl
import com.teampatch.core.data.repository.AnswerRepositoryImpl
import com.teampatch.core.data.repository.AppManagementRepositoryImpl
import com.teampatch.core.data.repository.local.LocalAuthenticationRepositoryImpl
import com.teampatch.core.data.repository.local.LocalGroupManagementRepositoryImpl
import com.teampatch.core.data.repository.local.LocalMemoryCardRepositoryImpl
import com.teampatch.core.data.repository.local.LocalQuestionRepositoryImpl
import com.teampatch.core.data.repository.local.LocalRoutineRepositoryImpl
import com.teampatch.core.data.repository.local.LocalUserRepositoryImpl
import com.teampatch.core.domain.entities.TokenManager
import com.teampatch.core.domain.repository.AnswerRepository
import com.teampatch.core.domain.repository.AppManagementRepository
import com.teampatch.core.domain.repository.AuthenticationRepository
import com.teampatch.core.domain.repository.GroupManagementRepository
import com.teampatch.core.domain.repository.MemoryCardRepository
import com.teampatch.core.domain.repository.QuestionRepository
import com.teampatch.core.domain.repository.RoutineRepository
import com.teampatch.core.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSingletonModule {

    @Binds
    abstract fun bindsTokenManager(
        tokenManagerImpl: TokenManagerImpl,
    ): TokenManager

    @Binds
    abstract fun bindsAuthenticationLocalDatasource(
        authenticationLocalDatasourceImpl: AuthenticationLocalDatasourceImpl,
    ): AuthenticationLocalDatasource

    @Binds
    abstract fun bindsMemoryCardRepository(
        localMemoryCardRepositoryImpl: LocalMemoryCardRepositoryImpl,
    ): MemoryCardRepository

    @Binds
    abstract fun bindsAuthenticationRepository(
        localAuthenticationRepositoryImpl: LocalAuthenticationRepositoryImpl,
    ): AuthenticationRepository

    @Binds
    abstract fun bindsQuestionRepository(
        localQuestionRepositoryImpl: LocalQuestionRepositoryImpl,
    ): QuestionRepository

    @Binds
    abstract fun bindsAnswerRepository(
        answerRepositoryImpl: AnswerRepositoryImpl,
    ): AnswerRepository

    @Binds
    abstract fun bindsAppManagementRepository(
        appManagementRepositoryImpl: AppManagementRepositoryImpl,
    ): AppManagementRepository

    @Binds
    abstract fun bindsGroupManagementRepository(
        groupManagementOfflineRepositoryImpl: LocalGroupManagementRepositoryImpl,
    ): GroupManagementRepository

    @Binds
    abstract fun bindsUserRepository(
        userOfflineRepositoryImpl: LocalUserRepositoryImpl,
    ): UserRepository

    @Binds
    abstract fun bindsRoutineRepository(
        localRoutineRepositoryImpl: LocalRoutineRepositoryImpl
    ): RoutineRepository
}