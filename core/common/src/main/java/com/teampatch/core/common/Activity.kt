package com.teampatch.core.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }

    return null
}

fun Context.startNotificationSettingsActivity(packageName: String) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    startActivity(intent)
}

fun Toast.makeText(
    context: Context,
    @StringRes stringResource: Int,
    duration: Int = Toast.LENGTH_SHORT
): Toast {
    return Toast.makeText(context, context.getString(stringResource), duration)
}