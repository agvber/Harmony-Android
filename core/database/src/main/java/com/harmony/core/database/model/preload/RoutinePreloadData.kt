package com.harmony.core.database.model.preload

import android.content.ContentValues

internal class RoutinePreloadData : DataPreloadHelper() {

    override val tableName: String = "routine"


    override val preloadData: List<ContentValues> = listOf(
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "아침 스트레칭")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"06:30\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "물 2L 마시기")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"08:00\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "책 30분 읽기")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"21:00\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "영어 단어 암기")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"07:15\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "주간 회고 작성")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"22:00\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "산책 30분")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"18:30\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "명상 10분")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"05:45\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "일기 쓰기")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"23:00\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "코딩 연습")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"20:15\"}"
            )
        },
        ContentValues().apply {
            put("group_id", 1L)
            put("name", "운동하기")
            put(
                "period",
                "{\"dayOfWeek\": [\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"], \"time\": \"17:00\"}"
            )
        }
    )


}