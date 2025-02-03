package com.example.calorietracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDateTime.now())
    val selectedDate = _selectedDate.asStateFlow()

    val dailyStats = selectedDate.flatMapLatest { date ->
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = date.toLocalDate().plusDays(1).atStartOfDay()
        repository.getMealsByDateRange(startOfDay, endOfDay)
    }.map { meals ->
        DailyStats(
            totalCalories = 0f,  // Şimdilik 0 olarak bırakıyoruz
            totalProtein = 0f,
            totalCarbs = 0f,
            totalFat = 0f,
            mealTypeDistribution = meals.groupBy { it.type }
                .mapValues { (_, mealList) -> mealList.size.toFloat() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DailyStats()
    )

    val weeklyStats = selectedDate.flatMapLatest { date ->
        val startOfWeek = date.toLocalDate().minusDays(6).atStartOfDay()
        val endOfWeek = date.toLocalDate().plusDays(1).atStartOfDay()
        
        repository.getMealsByDateRange(startOfWeek, endOfWeek)
    }.map { meals ->
        val dailyCalories = meals.groupBy { 
            it.dateTime.toLocalDate().atStartOfDay() 
        }.mapValues { (_, dayMeals) ->
            dayMeals.size.toFloat() // Şimdilik sadece öğün sayısını gösteriyoruz
        }
        WeeklyStats(dailyCalories = dailyCalories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeeklyStats()
    )

    fun setDate(date: LocalDateTime) {
        _selectedDate.value = date
    }
}

data class DailyStats(
    val totalCalories: Float = 0f,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    val mealTypeDistribution: Map<MealType, Float> = emptyMap()
)

data class WeeklyStats(
    val dailyCalories: Map<LocalDateTime, Float> = emptyMap()
) 