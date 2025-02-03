package com.example.calorietracker.ui.meal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.calorietracker.R
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.databinding.FragmentAddMealBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMealFragment : Fragment(R.layout.fragment_add_meal) {
    private var _binding: FragmentAddMealBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddMealViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddMealBinding.bind(view)

        setupViews()
    }

    private fun setupViews() {
        binding.btnSave.setOnClickListener {
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
                viewModel.addMeal(name, type, foodId, grams)
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 