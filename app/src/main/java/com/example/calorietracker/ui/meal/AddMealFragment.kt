package com.example.calorietracker.ui.meal

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.calorietracker.R
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.databinding.FragmentAddMealBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddMealFragment : Fragment(R.layout.fragment_add_meal) {
    private var _binding: FragmentAddMealBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddMealViewModel by viewModels()
    private var foodList: List<Food> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddMealBinding.bind(view)

        val mealType = arguments?.getSerializable("mealType") as? MealType ?: MealType.BREAKFAST

        setupViews(mealType)
        observeFoods()
    }

    private fun observeFoods() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.foods.collect { foods ->
                foodList = foods
                setupFoodSpinner(foods)
            }
        }
    }

    private fun setupFoodSpinner(foods: List<Food>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            foods.map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerFood.adapter = adapter
    }

    private fun setupViews(mealType: MealType) {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val type = mealType
            
            val selectedPosition = binding.spinnerFood.selectedItemPosition
            if (selectedPosition >= 0 && selectedPosition < foodList.size) {
                val selectedFood = foodList[selectedPosition]
                val grams = binding.etGrams.text.toString().toFloatOrNull() ?: 0f

                if (name.isNotBlank() && grams > 0) {
                    viewModel.addMeal(name, type, selectedFood.id.toLong(), grams)
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 