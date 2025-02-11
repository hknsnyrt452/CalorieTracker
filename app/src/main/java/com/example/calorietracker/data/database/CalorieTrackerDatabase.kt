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
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val foodDao = INSTANCE?.foodDao()
                            foodDao?.let {
                                val defaultFoods = listOf(
                                    Food(name = "Elma", calories = 52),
                                    Food(name = "Muz", calories = 89),
                                    Food(name = "Yoğurt", calories = 59),
                                    Food(name = "Ekmek", calories = 265),
                                    Food(name = "Peynir", calories = 264),
                                    Food(name = "Yumurta", calories = 155),
                                    Food(name = "Tavuk Göğsü", calories = 165),
                                    Food(name = "Pilav", calories = 130),
                                    Food(name = "Makarna", calories = 131),
                                    Food(name = "Süt", calories = 42),
                                    Food(name = "Badem", calories = 576),
                                    Food(name = "Ceviz", calories = 654),
                                    Food(name = "Portakal", calories = 47),
                                    Food(name = "Havuç", calories = 41),
                                    Food(name = "Salatalık", calories = 15),
                                    Food(name = "Domates", calories = 18),
                                    Food(name = "Mercimek Çorbası", calories = 230),
                                    Food(name = "Zeytinyağı", calories = 884),
                                    Food(name = "Bal", calories = 304),
                                    Food(name = "Yulaf", calories = 389)
                                )
                                defaultFoods.forEach { food ->
                                    foodDao.insertFood(food)
                                }
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 