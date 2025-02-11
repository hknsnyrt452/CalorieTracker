package com.example.calorietracker.data.dao

import androidx.room.*
import com.example.calorietracker.data.entity.WeightRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight_records ORDER BY date DESC")
    fun getAllWeightRecords(): Flow<List<WeightRecord>>

    @Query("SELECT * FROM weight_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    fun getWeightRecordsBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<WeightRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(weightRecord: WeightRecord)

    @Delete
    suspend fun deleteWeightRecord(weightRecord: WeightRecord)
} 