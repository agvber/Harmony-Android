package com.teampatch.core.data.mapper

import com.harmony.core.database.LOCAL_DB_TIME_FORMATTER
import com.harmony.core.database.model.RoutineEntity
import com.harmony.core.database.model.RoutineLogEntity
import com.harmony.core.database.model.multimap.RoutineWithLog
import com.squareup.moshi.JsonAdapter
import com.teampatch.core.data.di.annotation.MoshiAdapterType
import com.teampatch.core.data.di.annotation.MoshiType
import com.teampatch.core.domain.model.Routine
import com.teampatch.core.domain.model.routine.DailyRoutine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

internal class RoutineMapper @Inject constructor(
    @MoshiType(MoshiAdapterType.SET_STRING_ANY) private val adapter: JsonAdapter<Map<String, Any>>
) {
    fun toDomain(routineEntity: RoutineEntity): Routine? = routineEntity.runCatching {
        val periodPair = parsePeriod(period)
        Routine(
            id = id.toString(),
            name = name,
            daysOfWeekPeriod = periodPair.first,
            periodTime = periodPair.second
        )
    }
        .onFailure { it.printStackTrace() }
        .getOrNull()

    fun toDomain(
        routineEntity: RoutineEntity,
        routineLogs: List<RoutineLogEntity>,
        date: LocalDate
    ): DailyRoutine? =
        routineEntity.runCatching {
            val period: Pair<Set<DayOfWeek>, LocalTime> = parsePeriod(period)
            if (date.dayOfWeek !in period.first) return null
            DailyRoutine(
                routineId = id.toString(),
                name = name,
                time = period.second,
                isFinished = routineLogs
                    .find { it.routineId == routineEntity.id?.toLong() }?.isFinished == true
            )
        }
            .onFailure { it.printStackTrace() }
            .getOrNull()

    fun toDomain(routineWithLog: RoutineWithLog): DailyRoutine? = routineWithLog.runCatching {
        DailyRoutine(
            routineId = routineEntity.id.toString(),
            name = routineEntity.name,
            time = parsePeriod(routineEntity.period).second,
            isFinished = routineLogEntity.isFinished
        )
    }
        .onFailure { it.printStackTrace() }
        .getOrNull()

    fun buildRoutineEntity(
        routineName: String,
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime,
        groupId: Long,
        id: Long? = null
    ): RoutineEntity {
        return RoutineEntity(
            id = id,
            name = routineName,
            period = periodToString(daysOfWeekPeriod, periodTime),
            groupId = groupId
        )
    }

    fun parsePeriod(period: String): Pair<Set<DayOfWeek>, LocalTime> {
        val routineMap: Map<String, Any> = adapter.fromJson(period)!!
        val daysOfWeekPeriod: Set<DayOfWeek> = (routineMap[DAY_OF_WEEK_PARAM] as ArrayList<*>)
            .map { DayOfWeek.valueOf(it as String) }
            .toSet()
        val periodTime: LocalTime =
            LocalTime.parse(routineMap[TIME_PARAM]?.toString(), LOCAL_DB_TIME_FORMATTER)

        return Pair(daysOfWeekPeriod, periodTime)
    }

    fun periodToString(
        daysOfWeekPeriod: Set<DayOfWeek>,
        periodTime: LocalTime,
    ): String {
        val routineMap: Map<String, Any> = mapOf<String, Any>(
            TIME_PARAM to periodTime.format(LOCAL_DB_TIME_FORMATTER),
            DAY_OF_WEEK_PARAM to daysOfWeekPeriod
        )
        return adapter.toJson(routineMap)!!
    }

    companion object {
        private const val DAY_OF_WEEK_PARAM = "dayOfWeek"
        private const val TIME_PARAM = "time"
    }
}