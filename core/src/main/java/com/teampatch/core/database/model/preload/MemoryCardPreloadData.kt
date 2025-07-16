package com.teampatch.core.database.model.preload

import android.content.ContentValues

internal class MemoryCardPreloadData : DataPreloadHelper() {

    companion object {
        private const val TABLE_NAME = "memory_card"
    }

    override val tableName: String = TABLE_NAME

    override val preloadData: List<ContentValues> = listOf(
        ContentValues().apply {
            put("question_id", 1)
            put("written_uid", 9999)
            put("title", "다은이 태어난 날")
            put("content", "다은이가 이때부터 참 많이 울었지~ 아주 우렁차게 울어대서 커서 뭐가 되려나 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-12 14:30:00")
            put("modified_at", "2024-06-12 14:45:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"추억\",\"가족\"]")
        },
        ContentValues().apply {
            put("question_id", 2)
            put("written_uid", 9999)
            put("title", "가족 여행의 추억")
            put("content", "여름방학마다 떠났던 가족 여행. 그때마다 설렜던 기억이 나네~ 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-13 10:15:00")
            put("modified_at", "2024-06-13 10:30:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"여행\",\"추억\"]")
        },
        ContentValues().apply {
            put("question_id", 3)
            put("written_uid", 9999)
            put("title", "학교에서의 첫 발표")
            put("content", "엄청 떨렸던 발표 시간, 목소리는 떨렸지만 끝나고 나니 뿌듯했던 기억이 나네... 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-14 16:45:00")
            put("modified_at", "2024-06-14 17:00:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"학교\",\"추억\"]")
        },
        ContentValues().apply {
            put("question_id", 4)
            put("written_uid", 9999)
            put("title", "어릴 때 가장 좋아했던 음식")
            put("content", "김치볶음밥과 떡볶이! 언제 먹어도 맛있는 음식, 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-15 09:20:00")
            put("modified_at", "2024-06-15 09:35:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"음식\",\"기억\"]")
        },
        ContentValues().apply {
            put("question_id", 5)
            put("written_uid", 9999)
            put("title", "첫 자전거 타기")
            put("content", "넘어지고 또 넘어졌지만 결국 혼자서 탈 수 있었던 순간! 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-16 14:00:00")
            put("modified_at", "2024-06-16 14:15:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"취미\",\"도전\"]")
        },
        ContentValues().apply {
            put("question_id", 6)
            put("written_uid", 9999)
            put("title", "처음 받은 편지")
            put("content", "친구가 손으로 정성껏 써준 편지, 아직도 간직하고 있지... 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-17 20:30:00")
            put("modified_at", "2024-06-17 20:45:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"우정\",\"추억\"]")
        },
        ContentValues().apply {
            put("question_id", 7)
            put("written_uid", 9999)
            put("title", "예전 꿈 이야기")
            put("content", "어릴 때는 우주 비행사가 되고 싶었는데, 지금은 전혀 다른 길을 가고 있네... 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-18 08:00:00")
            put("modified_at", "2024-06-18 08:20:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"꿈\",\"성장\"]")
        },
        ContentValues().apply {
            put("question_id", 8)
            put("written_uid", 9999)
            put("title", "가장 좋아했던 놀이")
            put("content", "숨바꼭질! 친구들과 해가 질 때까지 뛰어다녔던 기억이 나네... 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-19 11:50:00")
            put("modified_at", "2024-06-19 12:10:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"놀이\",\"추억\"]")
        },
        ContentValues().apply {
            put("question_id", 9)
            put("written_uid", 9999)
            put("title", "가장 감동적인 순간")
            put("content", "가족이 생일날 깜짝 파티를 해줬을 때, 정말 감동이었지... 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-20 17:10:00")
            put("modified_at", "2024-06-20 17:30:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"가족\",\"감동\"]")
        },
        ContentValues().apply {
            put("question_id", 10)
            put("written_uid", 9999)
            put("title", "어릴 때 가장 좋아했던 동물")
            put("content", "강아지! 항상 같이 놀고 싶었고 강아지랑 산책하는 꿈도 꾸었지... 어쩌구 저쩌구 더미 텍스트 블라블라...")
            put("created_at", "2024-06-21 22:40:00")
            put("modified_at", "2024-06-21 22:55:00")
            put("image_url", "https://picsum.photos/400")
            put("tags", "[\"동물\",\"추억\"]")
        }
    )
}