package com.teampatch.core.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Activity.requestRadioAudioPermission() {
    if (!checkRadioAudioPermission()) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
    }
}

/**
 * Radio Audio 권한 체크 함수
 * @return 권한 존재 여부
 */

fun Context.checkRadioAudioPermission(): Boolean = ActivityCompat.checkSelfPermission(
    this,
    Manifest.permission.RECORD_AUDIO
) == PackageManager.PERMISSION_GRANTED