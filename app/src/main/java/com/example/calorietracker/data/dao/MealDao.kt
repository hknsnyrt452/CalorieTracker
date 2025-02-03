package com.example.calorietracker.data.dao

import androidx.room.*
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.MealType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE dateTime BETWEEN :startDate AND :endDate")
    fun getMealsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE type = :mealType AND dateTime BETWEEN :startDate AND :endDate")
    fun getMealsByTypeAndDateRange(
        mealType: MealType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMealById(id: Long): Meal?

    @Query("SELECT * FROM meals ORDER BY dateTime DESC")
    fun getAllMeals(): Flow<List<Meal>>
}

data class MealWithNutrients(
    val id: Long,
    val foodId: Long,
    val mealType: MealType,
    val grams: Float,
    val dateTime: LocalDateTime,
    val caloriesPer100g: Float,
    val proteinPer100g: Float,
    val carbsPer100g: Float,
    val fatPer100g: Float
)

data class MealTypeCalories(
    val mealType: MealType,
    val totalCalories: Float
) 