package com.example.calorietracker.data.repository

import com.example.calorietracker.data.dao.WeightDao
import com.example.calorietracker.data.entity.WeightRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightRepository @Inject constructor(
    private val weightDao: WeightDao
) {
    fun getWeightRecordsForLastMonth(): Flow<List<WeightRecord>> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusMonths(1)
        return weightDao.getWeightRecordsBetweenDates(startDate, endDate)
    }

    suspend fun insertWeightRecord(weightRecord: WeightRecord) {
        weightDao.insertWeightRecord(weightRecord)
    }

    suspend fun deleteWeightRecord(weightRecord: WeightRecord) {
        weightDao.deleteWeightRecord(weightRecord)
    }
} 