package com.teampatch.core.data.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.teampatch.core.data.model.MediaStoreInfo

internal fun ContentResolver.getMediaStoreInfo(uri: Uri): MediaStoreInfo? {
    query(
        uri,
        arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE
        ),
        null,
        null,
        null
    )?.use { cursor ->
        cursor.moveToFirst()
        val fileName = cursor.getString(0)
        val fileMediaType = cursor.getString(1)
        val fileSize = cursor.getLong(2)

        return MediaStoreInfo(
            displayName = fileName,
            mimType = fileMediaType,
            size = fileSize
        )
    }

    return null
}