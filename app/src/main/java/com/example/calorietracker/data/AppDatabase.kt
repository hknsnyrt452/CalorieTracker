package com.example.calorietracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.calorietracker.data.converter.Converters
import com.example.calorietracker.data.dao.MealDao
import com.example.calorietracker.data.entity.Meal

@Database(
    entities = [Meal::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
} 