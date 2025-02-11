package com.example.calorietracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.calorietracker.data.converter.DateTimeConverters
import com.example.calorietracker.data.converter.Converters
import com.example.calorietracker.data.dao.FoodDao
import com.example.calorietracker.data.dao.MealDao
import com.example.calorietracker.data.dao.WeightDao
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.WeightRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Food::class,
        Meal::class,
        WeightRecord::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class CalorieTrackerDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun mealDao(): MealDao
    abstract fun weightDao(): WeightDao

    companion object {
        @Volatile
        private var INSTANCE: CalorieTrackerDatabase? = null

        fun getDatabase(context: Context): CalorieTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalorieTrackerDatabase::class.java,
                    "calorie_tracker_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val defaultFoods = listOf(
                                    Food(name = "Elma", caloriesPer100g = 52f, proteinPer100g = 0.3f, carbsPer100g = 14f, fatPer100g = 0.2f),
                                    Food(name = "Muz", caloriesPer100g = 89f, proteinPer100g = 1.1f, carbsPer100g = 23f, fatPer100g = 0.3f),
                                    Food(name = "Yoğurt", caloriesPer100g = 59f, proteinPer100g = 3.5f, carbsPer100g = 4.7f, fatPer100g = 3.3f),
                                    Food(name = "Ekmek", caloriesPer100g = 265f, proteinPer100g = 9f, carbsPer100g = 49f, fatPer100g = 3.2f),
                                    Food(name = "Peynir", caloriesPer100g = 264f, proteinPer100g = 17f, carbsPer100g = 3.1f, fatPer100g = 21f),
                                    Food(name = "Yumurta", caloriesPer100g = 155f, proteinPer100g = 13f, carbsPer100g = 1.1f, fatPer100g = 11f),
                                    Food(name = "Tavuk Göğsü", caloriesPer100g = 165f, proteinPer100g = 31f, carbsPer100g = 0f, fatPer100g = 3.6f),
                                    Food(name = "Pilav", caloriesPer100g = 130f, proteinPer100g = 2.7f, carbsPer100g = 28f, fatPer100g = 0.3f),
                                    Food(name = "Makarna", caloriesPer100g = 131f, proteinPer100g = 5f, carbsPer100g = 25f, fatPer100g = 1.1f),
                                    Food(name = "Süt", caloriesPer100g = 42f, proteinPer100g = 3.4f, carbsPer100g = 5f, fatPer100g = 1f)
                                )
                                database.foodDao().insertAll(defaultFoods)
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
} 