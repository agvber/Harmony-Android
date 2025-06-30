package com.teampatch.core.data.service.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageFormatTransferService @Inject constructor(
    @ApplicationContext private val appContext: Context,
) {
    fun getBitmapFormat(contentResolverUri: Uri): Bitmap {
        appContext.contentResolver.openInputStream(contentResolverUri).use {
            return BitmapFactory.decodeStream(it)
        }
    }
}