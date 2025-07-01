package com.teampatch.core.data.repository.local

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.paging.PagingData
import com.harmony.core.database.LOCAL_DB_DATE_TIME_FORMATTER
import com.harmony.core.database.dao.MemoryCardDao
import com.harmony.core.database.dao.QuestionDao
import com.harmony.core.database.dao.UserDao
import com.harmony.core.database.model.MemoryCardEntity
import com.harmony.core.database.model.QuestionEntity
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.data.service.image.ImageCompressorService
import com.teampatch.core.data.service.image.ImageFormatTransferService
import com.teampatch.core.data.service.image.ImageSaverService
import com.teampatch.core.data.utils.FileFormat
import com.teampatch.core.domain.fake.FakeMemoryCardQuestion
import com.teampatch.core.domain.model.MemoryCard
import com.teampatch.core.domain.model.MemoryCardQuestion
import com.teampatch.core.domain.repository.MemoryCardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

internal class LocalMemoryCardRepositoryImpl @Inject constructor(
    private val memoryCardDao: MemoryCardDao,
    private val userDao: UserDao,
    private val questionDao: QuestionDao,
    private val imageFormatTransferService: ImageFormatTransferService,
    private val imageCompressorService: ImageCompressorService,
    private val imageSaverService: ImageSaverService,
) : MemoryCardRepository {

    override suspend fun addCommunication(
        memoryCardId: String,
        question: String,
        audioFile: InputStream,
    ) {
    }

    override suspend fun addAnswer(memoryCardId: String, answer: String) {
        val memoryCard = memoryCardDao.getMemoryStorageById(memoryCardId.toLong()).first()
        memoryCardDao.updateMemoryStorage(memoryCard.copy(content = answer))
    }

    override suspend fun getQuestionMessage(memoryCardId: String): MemoryCardQuestion =
        FakeMemoryCardQuestion().get()

    override fun getMemoryCards(): Flow<PagingData<MemoryCard>> {
        return memoryCardDao.getAllMemoryStorage().map { memoryCardEntities ->
            memoryCardEntities.mapNotNull { memoryCardEntity ->
                val writerUserInfo = userDao.getUserById(memoryCardEntity.writtenUid).firstOrNull()

                if (writerUserInfo == null) {
                    Log.e(TAG, "function: getMemoryCards(), data: writerUserInfo is null")
                    return@mapNotNull null
                }

                memoryCardEntity.toDomain(writerUserInfo.name)
            }
                .let {
                    PagingData.from(it)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMemoryCardById(memoryCardId: String): Flow<MemoryCard> =
        memoryCardDao.getMemoryStorageById(memoryCardId.toLong())
            .flatMapLatest { memoryCardEntity ->
                userDao.getUserById(memoryCardEntity.writtenUid).map { writerUserInfo ->
                    memoryCardEntity.toDomain(writerUserInfo.name)
                }
            }

    override suspend fun addMemoryCard(
        title: String,
        date: LocalDate,
        image: InputStream,
        writtenUserId: String
    ) {
        val bitmap: Bitmap = imageFormatTransferService.getBitmapFormat(image)
        val compressedImage: ByteArrayInputStream = imageCompressorService.compressImageWithTransferFormatJpeg(bitmap)
        val imageUri: Uri = imageSaverService.saveMemoryCardImage(compressedImage, FileFormat.JPEG)

        val now: LocalDateTime = LocalDateTime.now()
        val serverDateTimeFormat: String = now.format(LOCAL_DB_DATE_TIME_FORMATTER)
        val questionEntity =
            QuestionEntity(title = "", content = "", createdAt = serverDateTimeFormat)
        val questionEntitiesId: List<Long> = questionDao.insertQuestion(questionEntity)
        val memoryCardEntity = MemoryCardEntity(
            id = null,
            questionId = questionEntitiesId.first(),
            writtenUid = writtenUserId.toLong(),
            title = title,
            content = "",
            createdAt = serverDateTimeFormat,
            modifiedAt = serverDateTimeFormat,
            imageUri = imageUri.toString(),
            imageUrl = null,
            tags = emptySet()
        )
        memoryCardDao.insertMemoryStorage(memoryCardEntity)

        compressedImage.close()
        bitmap.recycle()
    }

    companion object {
        private const val TAG = "MemoryCardRepositoryImpl"
    }
}