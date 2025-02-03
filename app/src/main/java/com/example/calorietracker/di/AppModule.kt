package com.example.calorietracker.di

import android.content.Context
import com.example.calorietracker.data.database.CalorieTrackerDatabase
import com.example.calorietracker.data.repository.FoodRepository
import com.example.calorietracker.data.repository.MealRepository
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CalorieTrackerDatabase {
        return CalorieTrackerDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(
        database: CalorieTrackerDatabase
    ): FoodRepository {
        return FoodRepository(database.foodDao())
    }

    @Provides
    @Singleton
    fun provideMealRepository(
        database: CalorieTrackerDatabase
    ): MealRepository {
        return MealRepository(database.mealDao())
    }
} 