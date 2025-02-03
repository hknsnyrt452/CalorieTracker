package com.example.calorietracker.ui.meal.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.data.repository.FoodRepository
import com.example.calorietracker.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditMealViewModel @Inject constructor(
    private val repository: MealRepository,
    foodRepository: FoodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mealId: Long = savedStateHandle.get<Long>("mealId") ?: -1

    val allFoods = foodRepository.getAllFoods()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedFood = MutableStateFlow<Food?>(null)
    val selectedFood = _selectedFood.asStateFlow()

    private val _selectedMealType = MutableStateFlow<MealType?>(null)
    val selectedMealType = _selectedMealType.asStateFlow()

    private val _grams = MutableStateFlow(0f)
    val grams = _grams.asStateFlow()

    private val _selectedDateTime = MutableStateFlow(LocalDateTime.now())
    val selectedDateTime = _selectedDateTime.asStateFlow()

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal = _meal.asStateFlow()

    val nutrientInfo = combine(selectedFood, grams) { food, grams ->
        food?.let { calculateNutrients(it, grams) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        loadMeal()
    }

    private fun loadMeal() {
        viewModelScope.launch {
            _meal.value = repository.getMealById(mealId)
        }
    }

    fun setFood(food: Food) {
        _selectedFood.value = food
    }

    fun setMealType(mealType: MealType) {
        _selectedMealType.value = mealType
    }

    fun setGrams(grams: Float) {
        _grams.value = grams
    }

    fun setDateTime(dateTime: LocalDateTime) {
        _selectedDateTime.value = dateTime
    }

    fun updateMeal(
        name: String,
        type: MealType,
        foodId: Long,
        grams: Float
    ) {
        viewModelScope.launch {
            _meal.value?.let { currentMeal ->
                val updatedMeal = currentMeal.copy(
                    name = name,
                    type = type,
                    foodId = foodId,
                    grams = grams
                )
                repository.updateMeal(updatedMeal)
            }
        }
    }

    private fun calculateNutrients(food: Food, grams: Float): NutrientInfo {
        val ratio = grams / 100f
        return NutrientInfo(
            calories = food.caloriesPer100g * ratio,
            protein = food.proteinPer100g * ratio,
            carbs = food.carbsPer100g * ratio,
            fat = food.fatPer100g * ratio
        )
    }
}

data class NutrientInfo(
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float
) 