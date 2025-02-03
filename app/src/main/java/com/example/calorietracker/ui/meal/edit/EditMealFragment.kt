package com.example.calorietracker.ui.meal.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.calorietracker.R
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.databinding.FragmentEditMealBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditMealFragment : Fragment(R.layout.fragment_edit_meal) {
    private var _binding: FragmentEditMealBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditMealViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditMealBinding.bind(view)

        setupViews()
        observeMeal()
    }

    private fun setupViews() {
        binding.btnUpdate.setOnClickListener {
            val name = binding.etName.text.toString()
            val type = when (binding.rgMealType.checkedRadioButtonId) {
                R.id.rbBreakfast -> MealType.BREAKFAST
                R.id.rbLunch -> MealType.LUNCH
                R.id.rbDinner -> MealType.DINNER
                else -> MealType.SNACK
            }
            val foodId = binding.spinnerFood.selectedItemId
            val grams = binding.etGrams.text.toString().toFloatOrNull() ?: 0f

            if (name.isNotBlank() && grams > 0) {
                viewModel.updateMeal(name, type, foodId, grams)
                findNavController().navigateUp()
            }
        }
    }

    private fun observeMeal() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.meal.collectLatest { meal ->
                meal?.let {
                    binding.etName.setText(it.name)
                    binding.etGrams.setText(it.grams.toString())
                    // Diğer alanları da doldur
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 