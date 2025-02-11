package com.example.calorietracker.data.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var targetCalories: Int
        get() = prefs.getInt(KEY_TARGET_CALORIES, DEFAULT_TARGET_CALORIES)
        set(value) = prefs.edit().putInt(KEY_TARGET_CALORIES, value).apply()

    var targetWeight: Float
        get() = prefs.getFloat(KEY_TARGET_WEIGHT, DEFAULT_TARGET_WEIGHT)
        set(value) = prefs.edit().putFloat(KEY_TARGET_WEIGHT, value).apply()

    companion object {
        private const val PREFS_NAME = "calorie_tracker_settings"
        private const val KEY_TARGET_CALORIES = "target_calories"
        private const val KEY_TARGET_WEIGHT = "target_weight"
        private const val DEFAULT_TARGET_CALORIES = 2000
        private const val DEFAULT_TARGET_WEIGHT = 70.0f
    }
} 