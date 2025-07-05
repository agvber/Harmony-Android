package com.teampatch.core.data.service.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.teampatch.core.common.exception.FileTooLargeException
import com.teampatch.core.data.model.CompressedImageInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.math.roundToInt

internal class ImageUriCompressor @Inject constructor(
    @ApplicationContext private val appContext: Context,
) {
    private lateinit var fileName: String
    private lateinit var fileMediaType: String
    private lateinit var imageSourceInputStream: InputStream
    private lateinit var imageCompressedOutputStream: ByteArrayOutputStream

    private val contextResolver = appContext.contentResolver

    fun loadImage(imageUri: Uri) {
        contextResolver.query(
            imageUri,
            imageProjections,
            null,
            null,
            null
        )?.use { cursor ->
            cursor.moveToFirst()
            fileName = cursor.getString(0)
            fileMediaType = cursor.getString(1)
            imageSourceInputStream = contextResolver.openInputStream(imageUri)!!
            imageCompressedOutputStream = ByteArrayOutputStream()
        }
    }

    /**
     * @param qualityRange Hint to the compressor, 0-100.
     */

    fun compressImage(
        targetByteSize: Int,
        qualityRange: IntRange = IntRange(IMAGE_MIN_QUALITY, IMAGE_MAX_QUALITY),
    ): CompressedImageInfo {
        require(::imageSourceInputStream.isInitialized) {
            "이미지가 로드 되지 않았습니다.\n loadImage() 함수를 먼저 호출하세요."
        }

        val profileImageInputStream = imageCompressedOutputStream.use { outputStream ->
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageSourceInputStream)
            var quality: Int = qualityRange.max()

            do {
                if (quality < qualityRange.min()) {
                    throw FileTooLargeException()
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            } while (
                (outputStream.size() > targetByteSize).also { isRetry ->
                    if (isRetry) {
                        quality -= (quality * IMAGE_COMPRESS_RATIO).roundToInt()
                        outputStream.reset()
                    }
                }
            )
            bitmap.recycle()
            ByteArrayInputStream(outputStream.toByteArray())
        }

        return CompressedImageInfo(
            displayName = fileName,
            mimeType = fileMediaType,
            bitmapInputStream = profileImageInputStream
        )
    }

    companion object {

        private const val IMAGE_MAX_QUALITY: Int = 90
        private const val IMAGE_MIN_QUALITY: Int = 5
        private const val IMAGE_COMPRESS_RATIO: Double = 0.1

        private val imageProjections: Array<String> = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE
        )
    }
}