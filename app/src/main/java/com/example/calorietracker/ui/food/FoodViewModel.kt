package com.example.calorietracker.ui.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.repository.FoodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    val allFoods = foodRepository.getAllFoods()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFood(food: Food) {
        viewModelScope.launch {
            foodRepository.insertFood(food)
        }
    }

    fun updateFood(food: Food) {
        viewModelScope.launch {
            foodRepository.updateFood(food)
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            foodRepository.deleteFood(food)
        }
    }
} 