package com.teampatch.core.domain.usecase.version

import com.teampatch.core.domain.model.AppVersion
import com.teampatch.core.domain.repository.AppManagementRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAppLatestVersionUseCase @Inject constructor(
    private val appManagementRepository: AppManagementRepository,
) {

    operator fun invoke(): Flow<AppVersion> {
        val installedVersionName = appManagementRepository.getCurrentInstalledAppVersionName()
        val installedVersionCode = appManagementRepository.getCurrentInstalledAppVersionCode()

        return appManagementRepository.getPlayStoreLatestAppVersionCode()
            .map { latestVersionCode ->
                AppVersion(
                    isLatest = installedVersionCode == latestVersionCode,
                    playStoreVersionName = "null",
                    installedVersionName = installedVersionName
                )
            }
    }
}