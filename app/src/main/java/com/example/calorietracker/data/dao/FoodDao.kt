package com.example.calorietracker.data.dao

import androidx.room.*
import com.example.calorietracker.data.entity.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods")
    fun getAllFoods(): Flow<List<Food>>

    @Insert
    suspend fun insertFood(food: Food): Long

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM foods WHERE id = :foodId")
    suspend fun getFoodById(foodId: Long): Food?
} 