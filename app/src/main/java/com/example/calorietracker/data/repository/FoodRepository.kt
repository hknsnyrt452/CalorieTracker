package com.example.calorietracker.data.repository

import com.example.calorietracker.data.dao.FoodDao
import com.example.calorietracker.data.entity.Food
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val foodDao: FoodDao
) {
    fun getAllFoods(): Flow<List<Food>> = foodDao.getAllFoods()

    suspend fun insertFood(food: Food) = foodDao.insertFood(food)

    suspend fun updateFood(food: Food) = foodDao.updateFood(food)

    suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)

    suspend fun getFoodById(foodId: Long): Food? = foodDao.getFoodById(foodId)

    suspend fun insertDefaultFoodsIfEmpty() {
        if (foodDao.getAllFoodsOneShot().isEmpty()) {
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
            foodDao.insertAll(defaultFoods)
        }
    }
} 