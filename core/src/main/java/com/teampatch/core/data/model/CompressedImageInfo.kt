package com.teampatch.core.data.model

import java.io.InputStream

data class CompressedImageInfo(
    val displayName: String,
    val mimeType: String,
    val bitmapInputStream: InputStream,
)