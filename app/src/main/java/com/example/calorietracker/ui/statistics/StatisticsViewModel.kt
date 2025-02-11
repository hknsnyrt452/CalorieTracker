package com.example.calorietracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.data.repository.MealRepository
import com.example.calorietracker.data.repository.WeightRepository
import com.example.calorietracker.data.entity.WeightRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val weightRepository: WeightRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDateTime.now())
    val selectedDate = _selectedDate.asStateFlow()

    val dailyStats = selectedDate.flatMapLatest { date ->
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = date.toLocalDate().plusDays(1).atStartOfDay()
        mealRepository.getMealsByDateRange(startOfDay, endOfDay)
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
        
        mealRepository.getMealsByDateRange(startOfWeek, endOfWeek)
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

    val dailyNutrients = mealRepository.getDailyNutrients()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NutrientInfo(0f, 0f, 0f, 0f)
        )

    val weightRecords = weightRepository.getWeightRecordsForLastMonth()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setDate(date: LocalDateTime) {
        _selectedDate.value = date
    }

    fun addWeightRecord(weight: Float) {
        viewModelScope.launch {
            val record = WeightRecord(
                date = LocalDate.now(),
                weight = weight
            )
            weightRepository.insertWeightRecord(record)
        }
    }

    data class NutrientInfo(
        val calories: Float,
        val protein: Float,
        val carbs: Float,
        val fat: Float
    )
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