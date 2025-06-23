package com.teampatch.core.data.repository.local

import android.util.Log
import androidx.paging.PagingData
import com.teampatch.core.data.database.dao.MemoryCardDao
import com.teampatch.core.data.database.dao.UserDao
import com.teampatch.core.data.mapper.toDomain
import com.teampatch.core.domain.fake.FakeMemoryCardQuestion
import com.teampatch.core.domain.model.MemoryCard
import com.teampatch.core.domain.model.MemoryCardQuestion
import com.teampatch.core.domain.repository.MemoryCardRepository
import java.io.InputStream
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LocalMemoryCardRepositoryImpl @Inject constructor(
    private val memoryCardDao: MemoryCardDao,
    private val userDao: UserDao,
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

    override suspend fun getQuestionMessage(memoryCardId: String): MemoryCardQuestion = FakeMemoryCardQuestion().get()

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
    override fun getMemoryCardById(memoryCardId: String): Flow<MemoryCard> = memoryCardDao.getMemoryStorageById(memoryCardId.toLong())
        .flatMapLatest { memoryCardEntity ->
            userDao.getUserById(memoryCardEntity.writtenUid).map { writerUserInfo ->
                memoryCardEntity.toDomain(writerUserInfo.name)
            }
        }

    companion object {
        private const val TAG = "MemoryCardRepositoryImpl"
    }
}