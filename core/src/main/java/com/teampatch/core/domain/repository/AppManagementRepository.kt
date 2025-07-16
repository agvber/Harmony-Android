package com.teampatch.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppManagementRepository {

    /**
     * Play store 최신 앱 버전을 가져옵니다
     *
     * @return PlayStore App Version
     */

    fun getPlayStoreLatestAppVersionCode(): Flow<Int>

    /**
     * 현재 설치된 앱 버전을 가져옵니다
     *
     * @return Installed App Version
     */

    fun getCurrentInstalledAppVersionCode(): Int

    fun getCurrentInstalledAppVersionName(): String
}