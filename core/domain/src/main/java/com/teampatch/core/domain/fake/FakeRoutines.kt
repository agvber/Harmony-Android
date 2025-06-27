package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.DayOfWeek
import com.teampatch.core.domain.model.Routine
import java.time.LocalTime

class FakeRoutines : FakeModel<List<Routine>>() {

    override fun build(): List<Routine> {
        return listOf(
            Routine(
                id = "1",
                name = "아침 스트레칭",
                daysOfWeekPeriod = setOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.FRIDAY
                ),
                periodTime = LocalTime.of(7, 0)
            ),
            Routine(
                id = "2",
                name = "조깅",
                daysOfWeekPeriod = setOf(
                    DayOfWeek.TUESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.SATURDAY
                ),
                periodTime = LocalTime.of(8, 0)
            ),
            Routine(
                id = "3",
                name = "명상",
                daysOfWeekPeriod = setOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.THURSDAY
                ),
                periodTime = LocalTime.of(6, 30)
            ),
            Routine(
                id = "4",
                name = "근력 운동",
                daysOfWeekPeriod = setOf(
                    DayOfWeek.TUESDAY,
                    DayOfWeek.FRIDAY
                ),
                periodTime = LocalTime.of(18, 0)
            ),
            Routine(
                id = "5",
                name = "요가",
                daysOfWeekPeriod = setOf(
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.SUNDAY
                ),
                periodTime = LocalTime.of(20, 0)
            ),
            Routine(
                id = "6",
                name = "책 읽기",
                daysOfWeekPeriod = setOf(
                    DayOfWeek.SATURDAY,
                    DayOfWeek.SUNDAY
                ),
                periodTime = LocalTime.of(10, 0)
            ),
            Routine(
                id = "7",
                name = "나이트 워크",
                daysOfWeekPeriod = setOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.FRIDAY
                ),
                periodTime = LocalTime.of(21, 0)
            )
        )

    }

}