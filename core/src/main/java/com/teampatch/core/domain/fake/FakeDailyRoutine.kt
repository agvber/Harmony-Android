package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.routine.DailyRoutine
import java.time.LocalTime

class FakeDailyRoutine : FakeModel<List<DailyRoutine>>() {
    override fun build(): List<DailyRoutine> {
        return listOf(
            DailyRoutine("1", "기상", LocalTime.of(6, 0), true),
            DailyRoutine("2", "물 한 잔 마시기", LocalTime.of(6, 5), true),
            DailyRoutine("3", "아침 운동", LocalTime.of(6, 30), false),
            DailyRoutine("4", "샤워", LocalTime.of(7, 15), false),
            DailyRoutine("5", "아침 식사", LocalTime.of(7, 45), true),
            DailyRoutine("6", "출근 준비", LocalTime.of(8, 15), false),
            DailyRoutine("7", "업무 시작", LocalTime.of(9, 0), false),
            DailyRoutine("8", "점심 식사", LocalTime.of(12, 0), false),
            DailyRoutine("9", "저녁 운동", LocalTime.of(18, 30), false),
            DailyRoutine("10", "취침 준비", LocalTime.of(22, 0), false)
        )
    }
}