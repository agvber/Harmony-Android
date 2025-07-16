package com.teampatch.core.data.repository

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.teampatch.core.BuildConfig
import com.teampatch.core.domain.repository.AppManagementRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AppManagementRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : AppManagementRepository {

    override fun getPlayStoreLatestAppVersionCode(): Flow<Int> = callbackFlow {
        val appUpdateManager = AppUpdateManagerFactory.create(appContext)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            trySendBlocking(appUpdateInfo.availableVersionCode())
        }
        awaitClose()
    }

    override fun getCurrentInstalledAppVersionCode(): Int = BuildConfig.VERSION_CODE

    override fun getCurrentInstalledAppVersionName(): String = BuildConfig.VERSION_NAME
}