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
    private val args: AddMealFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddMealBinding.bind(view)

        setupViews()
        observeFoods()
        setInitialMealType()
    }

    private fun setInitialMealType() {
        val radioButtonId = when (args.mealType) {
            MealType.BREAKFAST -> R.id.rbBreakfast
            MealType.LUNCH -> R.id.rbLunch
            MealType.DINNER -> R.id.rbDinner
            MealType.SNACK -> R.id.rbSnack
        }
        binding.rgMealType.check(radioButtonId)
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

    private fun setupViews() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val type = when (binding.rgMealType.checkedRadioButtonId) {
                R.id.rbBreakfast -> MealType.BREAKFAST
                R.id.rbLunch -> MealType.LUNCH
                R.id.rbDinner -> MealType.DINNER
                else -> MealType.SNACK
            }
            
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