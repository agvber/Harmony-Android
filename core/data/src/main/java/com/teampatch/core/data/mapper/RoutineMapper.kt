package com.teampatch.core.data.mapper

import com.harmony.core.database.LOCAL_DB_TIME_FORMATTER
import com.harmony.core.database.model.RoutineEntity
import com.squareup.moshi.JsonAdapter
import com.teampatch.core.data.di.annotation.MoshiAdapterType
import com.teampatch.core.data.di.annotation.MoshiType
import com.teampatch.core.domain.model.Routine
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

internal class RoutineMapper @Inject constructor(
    @MoshiType(MoshiAdapterType.SET_STRING_ANY) private val adapter: JsonAdapter<Map<String, Any>>
) {
    fun toDomain(routineEntity: RoutineEntity): Routine? = routineEntity.runCatching {
        val routineMap: Map<String, Any> = adapter.fromJson(period)!!
        val daysOfWeekPeriod: Set<DayOfWeek> = (routineMap[DAY_OF_WEEK_PARAM] as ArrayList<*>)
            .map { DayOfWeek.valueOf(it as String) }
            .toSet()
        val periodTime: LocalTime =
            LocalTime.parse(routineMap[TIME_PARAM]?.toString(), LOCAL_DB_TIME_FORMATTER)

        Routine(
            id = id.toString(),
            name = name,
            daysOfWeekPeriod = daysOfWeekPeriod,
            periodTime = periodTime
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
        val routineMap: Map<String, Any> = mapOf<String, Any>(
            TIME_PARAM to periodTime.format(LOCAL_DB_TIME_FORMATTER),
            DAY_OF_WEEK_PARAM to daysOfWeekPeriod
        )
        val period: String = adapter.toJson(routineMap)!!
        return RoutineEntity(
            id = id,
            name = routineName,
            period = period,
            groupId = groupId
        )
    }

    companion object {
        private const val DAY_OF_WEEK_PARAM = "dayOfWeek"
        private const val TIME_PARAM = "time"
    }
}