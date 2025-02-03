package com.example.calorietracker.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.data.repository.MealRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class MealViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    fun getMealsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime) =
        mealRepository.getMealsByDateRange(startDate, endDate)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            mealRepository.insertMeal(meal)
        }
    }

    fun updateMeal(meal: Meal) {
        viewModelScope.launch {
            mealRepository.updateMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealRepository.deleteMeal(meal)
        }
    }
} 