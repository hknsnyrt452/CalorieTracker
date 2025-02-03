package com.example.calorietracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.calorietracker.data.converter.DateTimeConverters
import com.example.calorietracker.data.converter.Converters
import com.example.calorietracker.data.dao.FoodDao
import com.example.calorietracker.data.dao.MealDao
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.Meal

@Database(
    entities = [Food::class, Meal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class CalorieTrackerDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: CalorieTrackerDatabase? = null

        fun getDatabase(context: Context): CalorieTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalorieTrackerDatabase::class.java,
                    "calorie_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 