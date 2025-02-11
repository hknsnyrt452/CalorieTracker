package com.example.calorietracker.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Burası şimdilik boş kalabilir
    // İleride uygulama seviyesinde başka bağımlılıklar eklemek istersek buraya ekleyeceğiz
} 