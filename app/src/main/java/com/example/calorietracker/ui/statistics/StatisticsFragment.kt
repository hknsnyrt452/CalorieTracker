package com.example.calorietracker.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.core.content.ContextCompat
import com.example.calorietracker.R
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)

        setupCharts()
        setupDatePicker()
        setupObservers()
    }

    private fun setupCharts() {
        // Pie Chart ayarları
        binding.pieChartMeals.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            legend.isEnabled = true
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
        }

        // Line Chart ayarları
        binding.lineChartWeekly.apply {
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.valueFormatter = IndexAxisValueFormatter(
                (0..6).map { "Gün $it" }.toTypedArray()
            )
            axisRight.isEnabled = false
        }
    }

    private fun setupDatePicker() {
        updateDateButtonText(LocalDateTime.now())
        
        binding.btnSelectDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Tarih Seçin")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(selection),
                    ZoneId.systemDefault()
                )
                viewModel.setDate(selectedDate)
                updateDateButtonText(selectedDate)
            }

            datePicker.show(parentFragmentManager, "date_picker")
        }
    }

    private fun updateDateButtonText(date: LocalDateTime) {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        binding.btnSelectDate.text = date.format(formatter)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dailyStats.collect { stats: DailyStats ->
                updateDailyStats(stats)
                updatePieChart(stats.mealTypeDistribution)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weeklyStats.collect { stats: WeeklyStats ->
                updateLineChart(stats.dailyCalories)
            }
        }
    }

    private fun updateDailyStats(stats: DailyStats) {
        binding.tvTotalCalories.text = getString(R.string.total_calories, stats.totalCalories)
        binding.tvTotalNutrients.text = getString(
            R.string.nutrient_info,
            stats.totalProtein,
            stats.totalCarbs,
            stats.totalFat
        )
    }

    private fun updatePieChart(distribution: Map<MealType, Float>) {
        val entries = distribution.map { (type, value) ->
            PieEntry(value, type.name)
        }

        val colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.chart_breakfast),
            ContextCompat.getColor(requireContext(), R.color.chart_lunch),
            ContextCompat.getColor(requireContext(), R.color.chart_dinner),
            ContextCompat.getColor(requireContext(), R.color.chart_snack)
        )

        val dataSet = PieDataSet(entries, getString(R.string.meal_type)).apply {
            this.colors = colors
            valueTextSize = 14f
            valueTextColor = Color.BLACK
        }

        binding.pieChartMeals.data = PieData(dataSet)
        binding.pieChartMeals.invalidate()
    }

    private fun updateLineChart(dailyCalories: Map<LocalDateTime, Float>) {
        val entries = dailyCalories.entries.sortedBy { it.key }.map { (date, calories) ->
            Entry(
                date.toLocalDate().dayOfWeek.value.toFloat(),
                calories
            )
        }

        val dataSet = LineDataSet(entries, "Günlük Kalori").apply {
            color = Color.rgb(64, 89, 128)
            lineWidth = 2f
            setCircleColor(Color.rgb(64, 89, 128))
            circleRadius = 4f
            valueTextSize = 10f
        }

        binding.lineChartWeekly.data = LineData(dataSet)
        binding.lineChartWeekly.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 