package com.teampatch.core.domain.model

data class AppVersion(
    val isLatest: Boolean,
    val playStoreVersionName: String,
    val installedVersionName: String,
)