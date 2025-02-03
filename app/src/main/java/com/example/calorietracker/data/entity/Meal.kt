package com.example.calorietracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
} 