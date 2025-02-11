package com.example.calorietracker.data.repository

import com.example.calorietracker.data.dao.MealDao
import com.example.calorietracker.data.dao.FoodDao
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.MealType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val mealDao: MealDao,
    private val foodDao: FoodDao
) {
    fun getMealsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Meal>> = mealDao.getMealsByDateRange(startDate, endDate)

    fun getMealsByTypeAndDateRange(
        mealType: MealType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Meal>> = mealDao.getMealsByTypeAndDateRange(mealType, startDate, endDate)

    suspend fun insertMeal(meal: Meal): Long = mealDao.insertMeal(meal)

    suspend fun updateMeal(meal: Meal) = mealDao.updateMeal(meal)

    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)

    suspend fun getMealById(id: Long): Meal? = mealDao.getMealById(id)

    fun getAllMeals(): Flow<List<Meal>> = mealDao.getAllMeals()

    fun getDailyMeals(): Flow<List<Meal>> = mealDao.getDailyMeals().map { meals ->
        meals.onEach { meal ->
            foodDao.getFoodById(meal.foodId)?.let { food ->
                meal.calculateNutrients(food)
            }
        }
    }
} 