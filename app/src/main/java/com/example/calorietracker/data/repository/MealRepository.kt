package com.example.calorietracker.data.repository

import com.example.calorietracker.data.dao.MealDao
import com.example.calorietracker.data.dao.FoodDao
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.ui.statistics.StatisticsViewModel.NutrientInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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

    fun getDailyNutrients(): Flow<NutrientInfo> {
        val startDate = LocalDateTime.now().toLocalDate().atStartOfDay()
        val endDate = startDate.plusDays(1)

        return mealDao.getMealsByDateRange(startDate, endDate).map { meals ->
            var totalCalories = 0f
            var totalProtein = 0f
            var totalCarbs = 0f
            var totalFat = 0f

            meals.forEach { meal ->
                val food = foodDao.getFoodById(meal.foodId)
                food?.let {
                    val multiplier = meal.grams / 100f
                    totalCalories += it.caloriesPer100g * multiplier
                    totalProtein += it.proteinPer100g * multiplier
                    totalCarbs += it.carbsPer100g * multiplier
                    totalFat += it.fatPer100g * multiplier
                }
            }

            NutrientInfo(
                calories = totalCalories,
                protein = totalProtein,
                carbs = totalCarbs,
                fat = totalFat
            )
        }
    }
} 