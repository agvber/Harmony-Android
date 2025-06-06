package com.teampatch.feature.onboarding.common.di

import com.teampatch.core.common.ActivitySavedInstanceHelper
import com.teampatch.feature.onboarding.common.OnboardingUiStateHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal object ActivitySavedInstanceModule {

    @Provides
    fun providesOnboardingUiStateHelper(): ActivitySavedInstanceHelper = OnboardingUiStateHelper.getInstance()
}