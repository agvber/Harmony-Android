package com.teampatch.core.data.database.model.preload

import android.content.ContentValues

internal class TodoPreloadData : DataPreloadHelper() {

    companion object {
        private const val TABLE_NAME = "todo"
    }

    override val tableName: String = TABLE_NAME

    override val preloadData: List<ContentValues> = listOf(
        ContentValues().apply {
            put("title", "아침 운동하기")
            put("is_finished", false)
            put("created_at", "2024-04-10 06:30:00")
            put("modified_at", "2024-04-10 06:30:00")
        },
        ContentValues().apply {
            put("title", "책 한 챕터 읽기")
            put("is_finished", false)
            put("created_at", "2024-04-11 08:45:00")
            put("modified_at", "2024-04-11 08:45:00")
        },
        ContentValues().apply {
            put("title", "할머니께 전화 드리기")
            put("is_finished", true)
            put("created_at", "2024-04-12 10:00:00")
            put("modified_at", "2024-04-12 10:00:00")
        },
        ContentValues().apply {
            put("title", "친구와 저녁 식사하기")
            put("is_finished", false)
            put("created_at", "2024-04-13 19:30:00")
            put("modified_at", "2024-04-13 19:30:00")
        },
        ContentValues().apply {
            put("title", "쇼핑 리스트 작성하기")
            put("is_finished", true)
            put("created_at", "2024-04-14 12:15:00")
            put("modified_at", "2024-04-14 12:15:00")
        },
        ContentValues().apply {
            put("title", "코딩 공부하기")
            put("is_finished", false)
            put("created_at", "2024-04-15 14:00:00")
            put("modified_at", "2024-04-15 14:00:00")
        },
        ContentValues().apply {
            put("title", "산책하기")
            put("is_finished", true)
            put("created_at", "2024-04-16 17:50:00")
            put("modified_at", "2024-04-16 17:50:00")
        },
        ContentValues().apply {
            put("title", "영화 감상하기")
            put("is_finished", false)
            put("created_at", "2024-04-17 20:30:00")
            put("modified_at", "2024-04-17 20:30:00")
        },
        ContentValues().apply {
            put("title", "주간 목표 설정하기")
            put("is_finished", true)
            put("created_at", "2024-04-18 09:10:00")
            put("modified_at", "2024-04-18 09:10:00")
        },
        ContentValues().apply {
            put("title", "출근 전 커피 마시기")
            put("is_finished", false)
            put("created_at", "2024-04-19 07:20:00")
            put("modified_at", "2024-04-19 07:20:00")
        }
    )
}