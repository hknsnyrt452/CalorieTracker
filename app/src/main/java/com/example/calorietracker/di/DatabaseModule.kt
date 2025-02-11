package com.example.calorietracker.di

import android.content.Context
import androidx.room.Room
import com.example.calorietracker.data.database.CalorieTrackerDatabase
import com.example.calorietracker.data.dao.FoodDao
import com.example.calorietracker.data.dao.MealDao
import com.example.calorietracker.data.dao.WeightDao
import com.example.calorietracker.data.repository.FoodRepository
import com.example.calorietracker.data.repository.MealRepository
import com.example.calorietracker.data.repository.WeightRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CalorieTrackerDatabase {
        return Room.databaseBuilder(
            context,
            CalorieTrackerDatabase::class.java,
            "calorie_tracker_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFoodDao(database: CalorieTrackerDatabase): FoodDao {
        return database.foodDao()
    }

    @Provides
    fun provideMealDao(database: CalorieTrackerDatabase): MealDao {
        return database.mealDao()
    }

    @Provides
    fun provideWeightDao(database: CalorieTrackerDatabase): WeightDao {
        return database.weightDao()
    }

    @Provides
    fun provideFoodRepository(foodDao: FoodDao): FoodRepository {
        return FoodRepository(foodDao)
    }

    @Provides
    fun provideMealRepository(mealDao: MealDao, foodDao: FoodDao): MealRepository {
        return MealRepository(mealDao, foodDao)
    }

    @Provides
    fun provideWeightRepository(weightDao: WeightDao): WeightRepository {
        return WeightRepository(weightDao)
    }
} 