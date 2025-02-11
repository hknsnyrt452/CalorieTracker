package com.example.calorietracker.ui.food.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.insertDefaultFoodsIfEmpty()
        }
    }

    val foods: StateFlow<List<Food>> = repository.getAllFoods()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            repository.deleteFood(food)
        }
    }

    // Test için örnek veri ekleme
    fun insertTestData() {
        viewModelScope.launch {
            val testFoods = listOf(
                Food(
                    name = "Elma",
                    caloriesPer100g = 52f,
                    proteinPer100g = 0.3f,
                    carbsPer100g = 14f,
                    fatPer100g = 0.2f
                ),
                Food(
                    name = "Muz",
                    caloriesPer100g = 89f,
                    proteinPer100g = 1.1f,
                    carbsPer100g = 23f,
                    fatPer100g = 0.3f
                ),
                Food(
                    name = "Yoğurt",
                    caloriesPer100g = 61f,
                    proteinPer100g = 3.5f,
                    carbsPer100g = 4.7f,
                    fatPer100g = 3.3f
                )
            )
            
            testFoods.forEach { food ->
                repository.insertFood(food)
            }
        }
    }
} 