package com.example.calorietracker.di

import com.example.calorietracker.data.database.CalorieTrackerDatabase
import com.example.calorietracker.data.dao.FoodDao
import com.example.calorietracker.data.dao.MealDao
import com.example.calorietracker.data.repository.FoodRepository
import com.example.calorietracker.data.repository.MealRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideMealDao(database: CalorieTrackerDatabase): MealDao {
        return database.mealDao()
    }

    @Provides
    @Singleton
    fun provideMealRepository(mealDao: MealDao): MealRepository {
        return MealRepository(mealDao)
    }

    @Provides
    fun provideFoodDao(database: CalorieTrackerDatabase): FoodDao {
        return database.foodDao()
    }

    @Provides
    @Singleton
    fun provideFoodRepository(foodDao: FoodDao): FoodRepository {
        return FoodRepository(foodDao)
    }
} 