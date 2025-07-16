package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.memory.MemoryCard
import java.time.LocalDateTime

class FakeMemoryCard : FakeModel<List<MemoryCard>>() {
    override fun build(): List<MemoryCard> = listOf(
        MemoryCard(
            id = "1",
            writerTitle = "Happy Moment",
            writerName = "Alice",
            text = "This was one of the best days of my life!",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now(),
            tags = setOf("happy", "moments", "memories")
        ),
        MemoryCard(
            id = "2",
            writerTitle = "Vacation Fun",
            writerName = "Bob",
            text = "Enjoying the sun at the beach.",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(1),
            tags = setOf("beach", "vacation", "fun")
        ),
        MemoryCard(
            id = "3",
            writerTitle = "Family Time",
            writerName = "Charlie",
            text = "Great dinner with family.",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(2),
            tags = setOf("family", "dinner", "time")
        ),
        MemoryCard(
            id = "4",
            writerTitle = "Adventure",
            writerName = "Dave",
            text = "Hiking in the mountains.",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(3),
            tags = setOf("hiking", "mountains", "adventure")
        ),
        MemoryCard(
            id = "5",
            writerTitle = "Birthday Bash",
            writerName = "Eve",
            text = "Celebrating my birthday with friends.",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(4),
            tags = setOf("birthday", "celebration", "friends")
        ),
        MemoryCard(
            id = "6",
            writerTitle = "New Job",
            writerName = "Frank",
            text = "Started my new job today!",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(5),
            tags = setOf("new", "job", "today")
        ),
        MemoryCard(
            id = "7",
            writerTitle = "Concert",
            writerName = "Grace",
            text = "Amazing night at the concert.",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(6),
            tags = setOf("concert", "night", "amazing")
        ),
        MemoryCard(
            id = "8",
            writerTitle = "Pet Love",
            writerName = "Hank",
            text = "Cuddling with my dog.",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(7),
            tags = setOf("pet", "love", "cuddle")
        ),
        MemoryCard(
            id = "9",
            writerTitle = "Travel Diaries",
            writerName = "Ivy",
            text = "Exploring a new city.",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(8),
            tags = setOf("travel", "diaries", "new city")
        ),
        MemoryCard(
            id = "10",
            writerTitle = "Graduation",
            writerName = "Jack",
            text = "Finally graduated!",
            imageUrl = FAKE_IMAGE_URL,
            imageUri = null,
            dateTime = LocalDateTime.now().minusDays(9),
            tags = setOf("graduation", "finally", "today")
        )
    )
}