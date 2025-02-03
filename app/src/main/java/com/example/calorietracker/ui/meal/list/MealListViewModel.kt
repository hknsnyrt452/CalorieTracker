package com.example.calorietracker.ui.meal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MealListViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDateTime.now())
    val selectedDate = _selectedDate.asStateFlow()

    val dailyMeals = selectedDate.flatMapLatest { date ->
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = date.toLocalDate().plusDays(1).atStartOfDay()
        repository.getMealsByDateRange(startOfDay, endOfDay)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setDate(date: LocalDateTime) {
        _selectedDate.value = date
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }
} 