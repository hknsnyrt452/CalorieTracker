package com.example.calorietracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val type: MealType,
    val dateTime: LocalDateTime = LocalDateTime.now()
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
} 