package com.example.calorietracker.di

import android.content.Context
import androidx.room.Room
import com.example.calorietracker.data.database.CalorieTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCalorieTrackerDatabase(
        @ApplicationContext context: Context
    ): CalorieTrackerDatabase {
        return Room.databaseBuilder(
            context,
            CalorieTrackerDatabase::class.java,
            "calorie_tracker_db"
        ).build()
    }
} 