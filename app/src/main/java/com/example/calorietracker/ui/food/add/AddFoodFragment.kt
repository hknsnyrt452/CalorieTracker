package com.example.calorietracker.ui.food.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.calorietracker.R
import com.example.calorietracker.databinding.FragmentAddFoodBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFoodFragment : Fragment(R.layout.fragment_add_food) {
    private var _binding: FragmentAddFoodBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddFoodViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddFoodBinding.bind(view)

        setupViews()
    }

    private fun setupViews() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val caloriesPer100g = binding.etCalories.text.toString().toFloatOrNull() ?: 0f
            val proteinPer100g = binding.etProtein.text.toString().toFloatOrNull() ?: 0f
            val carbsPer100g = binding.etCarbs.text.toString().toFloatOrNull() ?: 0f
            val fatPer100g = binding.etFat.text.toString().toFloatOrNull() ?: 0f

            if (name.isBlank() || caloriesPer100g <= 0) {
                Snackbar.make(binding.root, R.string.error_invalid_input, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addFood(name, caloriesPer100g, proteinPer100g, carbsPer100g, fatPer100g)
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 