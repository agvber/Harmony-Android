package com.teampatch.core.data.network.model

import java.io.InputStream

data class FileUploadRequest(
    val fileName: String,
    val fileMediaType: String?,
    val fileContent: InputStream,
)