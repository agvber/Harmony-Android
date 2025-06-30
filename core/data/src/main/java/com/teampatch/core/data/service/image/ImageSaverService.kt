package com.teampatch.core.data.service.image

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.pathString

class ImageSaverService @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    fun saveProfileImage(inputStream: InputStream, fileFormat: String): Uri {
        val path: String = Path(appContext.dataDir.absolutePath, PROFILE_IMAGE_SUB_PATH)
            .also { it.createDirectories() }
            .pathString

        return File(path, "$PROFILE_IMAGE_FILE_NAME.$fileFormat")
            .also { it.writeBytes(inputStream.readBytes()) }
            .toUri()
    }

    companion object {
        private const val PROFILE_IMAGE_SUB_PATH = "profile_image"
        private const val PROFILE_IMAGE_FILE_NAME = "profileImage"
    }
}