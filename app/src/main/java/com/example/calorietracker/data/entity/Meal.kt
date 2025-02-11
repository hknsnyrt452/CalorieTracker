package com.example.calorietracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
import com.example.calorietracker.data.entity.Food
import java.time.LocalDateTime

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: MealType,
    val foodId: Long,
    val grams: Float,
    val dateTime: LocalDateTime = LocalDateTime.now()
) {
    // Hesaplanan değerler
    @Ignore
    var calories: Int = 0
        private set

    @Ignore
    var protein: Float = 0f
        private set

    @Ignore
    var carbs: Float = 0f
        private set

    @Ignore
    var fat: Float = 0f
        private set

    fun calculateNutrients(food: Food) {
        val multiplier = grams / 100f
        calories = (food.caloriesPer100g * multiplier).toInt()
        protein = food.proteinPer100g * multiplier
        carbs = food.carbsPer100g * multiplier
        fat = food.fatPer100g * multiplier
    }
}

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
} 