package com.example.calorietracker.data.converter

import androidx.room.TypeConverter
import com.example.calorietracker.data.entity.MealType
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun fromMealType(value: String?): MealType? {
        return value?.let { enumValueOf<MealType>(it) }
    }

    @TypeConverter
    fun toMealType(type: MealType?): String? {
        return type?.name
    }
} 