package com.teampatch.core.data.service.image

import android.graphics.Bitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageCompressorService @Inject constructor() {

    fun compressImageWithTransferFormatJpeg(
        bitmap: Bitmap,
        quality: Int = DEFAULT_QUALITY
    ): ByteArrayInputStream {
        ByteArrayOutputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
            return ByteArrayInputStream(it.toByteArray())
        }
    }

    companion object {
        private const val DEFAULT_QUALITY = 65
    }
}