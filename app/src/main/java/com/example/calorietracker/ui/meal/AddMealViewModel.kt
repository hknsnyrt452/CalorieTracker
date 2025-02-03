package com.example.calorietracker.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.data.repository.FoodRepository
import com.example.calorietracker.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val repository: MealRepository,
    foodRepository: FoodRepository
) : ViewModel() {

    val allFoods = foodRepository.getAllFoods()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun calculateNutrients(food: Food, grams: Float): NutrientInfo {
        val ratio = grams / 100f
        return NutrientInfo(
            calories = food.caloriesPer100g * ratio,
            protein = food.proteinPer100g * ratio,
            carbs = food.carbsPer100g * ratio,
            fat = food.fatPer100g * ratio
        )
    }

    fun addMeal(
        name: String,
        type: MealType,
        foodId: Long,
        grams: Float
    ) {
        viewModelScope.launch {
            val meal = Meal(
                name = name,
                type = type,
                foodId = foodId,
                grams = grams
            )
            repository.insertMeal(meal)
        }
    }
}

data class NutrientInfo(
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float
) 