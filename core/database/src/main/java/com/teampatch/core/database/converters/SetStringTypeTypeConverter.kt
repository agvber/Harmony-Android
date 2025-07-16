package com.teampatch.core.database.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

internal class SetStringTypeTypeConverter : RoomTypeConverter<Set<String>>() {

    @TypeConverter
    override fun fromObjectToJson(value: Set<String>): String = adapter.toJson(value)

    @TypeConverter
    override fun fromJsonToObject(value: String): Set<String> = adapter.fromJson(value)!!

    companion object {
        private val moshi: Moshi = Moshi.Builder().build()
        private val adapter: JsonAdapter<Set<String>> = moshi.adapter<Set<String>>(Set::class.java)
    }
}