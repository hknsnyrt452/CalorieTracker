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
} 