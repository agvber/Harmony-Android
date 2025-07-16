package com.teampatch.core.database.model.preload

import android.content.ContentValues

internal class QuestionPreloadData : DataPreloadHelper() {

    override val tableName: String = TABLE_NAME

    companion object {
        private const val TABLE_NAME = "question"
    }

    override val preloadData: List<ContentValues> = listOf(
        ContentValues().apply {
            put("title", "할머니는 어릴 때 어떤 놀이를 가장 좋아하셨어요?")
            put("content", "예전에는 어떤 놀이를 했는지 궁금해요!")
            put("created_at", "2024-06-12 14:30:00")
        },
        ContentValues().apply {
            put("title", "예전에는 어떤 간식이 인기 있었나요?")
            put("content", "지금처럼 다양한 과자가 있었나요?")
            put("created_at", "2023-11-28 10:15:00")
        },
        ContentValues().apply {
            put("title", "어린 시절 가장 기억에 남는 순간은 무엇인가요?")
            put("content", "가장 행복했던 순간을 듣고 싶어요.")
            put("created_at", "2022-05-04 17:45:00")
        },
        ContentValues().apply {
            put("title", "할머니가 배운 가장 중요한 삶의 교훈은?")
            put("content", "오랜 경험 속에서 가장 중요한 것은 무엇인가요?")
            put("created_at", "2021-09-10 08:00:00")
        },
        ContentValues().apply {
            put("title", "가족 모임에서 가장 즐거웠던 기억이 있다면?")
            put("content", "가족과 함께했던 특별한 날이 있나요?")
            put("created_at", "2023-02-14 12:20:00")
        },
        ContentValues().apply {
            put("title", "할머니가 가장 좋아하는 음식은?")
            put("content", "특별한 날에 꼭 먹고 싶은 음식이 있나요?")
            put("created_at", "2024-01-01 19:10:00")
        },
        ContentValues().apply {
            put("title", "젊은 시절엔 어떤 꿈을 가지셨나요?")
            put("content", "어릴 때 꿈꿨던 미래가 궁금해요!")
            put("created_at", "2022-07-22 09:55:00")
        },
        ContentValues().apply {
            put("title", "예전에는 어떤 방식으로 친구들과 연락을 주고받았나요?")
            put("content", "지금처럼 휴대전화가 없던 시절엔 어떻게 연락했나요?")
            put("created_at", "2023-03-30 15:40:00")
        },
        ContentValues().apply {
            put("title", "할머니 시대에는 연애를 어떻게 했나요?")
            put("content", "예전엔 데이트를 어떻게 했는지 궁금해요.")
            put("created_at", "2020-12-24 18:25:00")
        },
        ContentValues().apply {
            put("title", "가장 자랑스러운 순간은 언제였나요?")
            put("content", "할머니가 가장 뿌듯했던 순간을 듣고 싶어요.")
            put("created_at", "2021-06-09 21:30:00")
        },
        ContentValues().apply {
            put("title", "지금도 자주 떠오르는 어린 시절 친구가 있나요?")
            put("content", "어릴 때 가장 친했던 친구가 기억나시나요?")
            put("created_at", "2024-04-15 10:05:00")
        },
        ContentValues().apply {
            put("title", "할머니가 들었던 가장 감동적인 이야기나 경험은?")
            put("content", "살면서 가장 감동받았던 순간이 궁금해요.")
            put("created_at", "2022-10-30 16:50:00")
        },
        ContentValues().apply {
            put("title", "할머니가 가장 행복했던 순간은?")
            put("content", "언제 가장 큰 행복을 느끼셨나요?")
            put("created_at", "2023-07-04 13:15:00")
        },
        ContentValues().apply {
            put("title", "어릴 때 가장 좋아했던 음악이나 노래는?")
            put("content", "예전에는 어떤 음악이 유행했나요?")
            put("created_at", "2020-11-01 08:30:00")
        },
        ContentValues().apply {
            put("title", "예전에는 사람들이 어떤 방식으로 시간을 보냈나요?")
            put("content", "지금처럼 TV나 스마트폰이 없던 시대에는 무엇을 했나요?")
            put("created_at", "2022-03-18 17:20:00")
        },
        ContentValues().apply {
            put("title", "젊은 시절 해보고 싶었지만 못해본 일이 있나요?")
            put("content", "아직도 해보고 싶은 꿈이 있나요?")
            put("created_at", "2021-08-12 11:45:00")
        },
        ContentValues().apply {
            put("title", "가장 좋아하는 가족 전통이나 명절 문화가 있나요?")
            put("content", "예전에는 명절을 어떻게 보냈나요?")
            put("created_at", "2023-09-27 09:10:00")
        },
        ContentValues().apply {
            put("title", "할머니가 가장 감사하게 여기는 것은?")
            put("content", "살면서 가장 감사했던 순간이 있나요?")
            put("created_at", "2020-04-30 22:00:00")
        },
        ContentValues().apply {
            put("title", "세월이 지나도 변하지 않는 소중한 가치는?")
            put("content", "지금까지 변하지 않는 중요한 가치가 있다면?")
            put("created_at", "2024-05-01 07:55:00")
        }
    )
}