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
import com.github.mikephil.charting.components.XAxis
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
import com.example.calorietracker.data.entity.WeightRecord
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.widget.EditText
import android.text.InputType
import javax.inject.Inject
import com.example.calorietracker.data.preferences.SettingsManager
import com.example.calorietracker.ui.statistics.StatisticsViewModel.NutrientInfo
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatisticsViewModel by viewModels()

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStatisticsBinding.bind(view)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.apply {
            // Kilo hedefi ayarı için tıklama
            layoutTargetWeight.setOnClickListener {
                showTargetWeightDialog()
            }

            // Yeni kilo girişi için FAB
            fabAddWeight.setOnClickListener {
                showAddWeightDialog()
            }

            // LineChart ayarları
            lineChart.apply {
                description.isEnabled = false
                legend.isEnabled = true
                setTouchEnabled(true)
                setScaleEnabled(true)
                setPinchZoom(true)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    setDrawAxisLine(true)
                }

                axisRight.isEnabled = false
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Toplam kalori ve besin değerleri
            viewModel.dailyNutrients.collectLatest { nutrients: NutrientInfo ->
                binding.apply {
                    tvTotalCalories.text = getString(R.string.total_calories, nutrients.calories)
                    tvNutrients.text = getString(
                        R.string.nutrient_info,
                        nutrients.protein,
                        nutrients.carbs,
                        nutrients.fat
                    )
                }
            }
        }

        // Kilo grafiği
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weightRecords.collectLatest { records: List<WeightRecord> ->
                updateWeightChart(records)
            }
        }
    }

    private fun updateWeightChart(records: List<WeightRecord>) {
        val entries = records.mapIndexed { index, record ->
            Entry(index.toFloat(), record.weight)
        }

        val dataSet = LineDataSet(entries, "Kilo").apply {
            color = Color.BLUE
            setCircleColor(Color.BLUE)
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
        }

        val dates = records.map { record ->
            record.date.format(DateTimeFormatter.ofPattern("dd/MM"))
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(dates)
            invalidate()
        }
    }

    private fun showAddWeightDialog() {
        val editText = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.add_weight)
            .setView(editText)
            .setPositiveButton(R.string.save) { _, _ ->
                val weight = editText.text.toString().toFloatOrNull()
                if (weight != null && weight > 0) {
                    viewModel.addWeightRecord(weight)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showTargetWeightDialog() {
        val editText = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(settingsManager.targetWeight.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.set_target_weight)
            .setView(editText)
            .setPositiveButton(R.string.save) { _, _ ->
                val weight = editText.text.toString().toFloatOrNull()
                if (weight != null && weight > 0) {
                    settingsManager.targetWeight = weight
                    updateWeightChart(viewModel.weightRecords.value)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 