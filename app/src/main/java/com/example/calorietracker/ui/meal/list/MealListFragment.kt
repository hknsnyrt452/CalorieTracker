package com.example.calorietracker.ui.meal.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietracker.R
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.data.entity.MealType
import com.example.calorietracker.databinding.FragmentMealListBinding
import com.example.calorietracker.ui.meal.MealAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.core.os.bundleOf

@AndroidEntryPoint
class MealListFragment : Fragment(R.layout.fragment_meal_list) {
    private var _binding: FragmentMealListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MealListViewModel by viewModels()
    
    private lateinit var breakfastAdapter: MealAdapter
    private lateinit var lunchAdapter: MealAdapter
    private lateinit var dinnerAdapter: MealAdapter
    private lateinit var snacksAdapter: MealAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMealListBinding.bind(view)

        setupRecyclerViews()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerViews() {
        val onEditClick: (Meal) -> Unit = { meal ->
            findNavController().navigate(
                MealListFragmentDirections.actionMealListFragmentToEditMealFragment(meal.id)
            )
        }
        
        val onDeleteClick: (Meal) -> Unit = { meal ->
            showDeleteConfirmationDialog(meal)
        }

        // Her öğün için ayrı adapter oluştur
        breakfastAdapter = MealAdapter(
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
        lunchAdapter = MealAdapter(
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
        dinnerAdapter = MealAdapter(
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
        snacksAdapter = MealAdapter(
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )

        // RecyclerView'ları ayarla
        binding.apply {
            rvBreakfast.apply {
                adapter = breakfastAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            
            rvLunch.apply {
                adapter = lunchAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            
            rvDinner.apply {
                adapter = dinnerAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            
            rvSnacks.apply {
                adapter = snacksAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.groupedMeals.collectLatest { groupedMeals ->
                // Her öğün tipine göre listeleri güncelle
                breakfastAdapter.submitList(groupedMeals[MealType.BREAKFAST] ?: emptyList())
                lunchAdapter.submitList(groupedMeals[MealType.LUNCH] ?: emptyList())
                dinnerAdapter.submitList(groupedMeals[MealType.DINNER] ?: emptyList())
                snacksAdapter.submitList(groupedMeals[MealType.SNACK] ?: emptyList())

                // Kalori hesaplamalarını güncelle
                updateCalorieCalculations(groupedMeals)
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnAddBreakfast.setOnClickListener {
                navigateToAddMealFragment(MealType.BREAKFAST)
            }
            
            btnAddLunch.setOnClickListener {
                navigateToAddMealFragment(MealType.LUNCH)
            }
            
            btnAddDinner.setOnClickListener {
                navigateToAddMealFragment(MealType.DINNER)
            }
            
            btnAddSnack.setOnClickListener {
                navigateToAddMealFragment(MealType.SNACK)
            }
        }
    }

    private fun navigateToAddMealFragment(mealType: MealType) {
        findNavController().navigate(
            R.id.action_mealListFragment_to_addMealFragment,
            bundleOf("mealType" to mealType)
        )
    }

    private fun updateCalorieCalculations(groupedMeals: Map<MealType, List<Meal>>) {
        val totalCalories = groupedMeals.values.flatten()
            .sumOf { it.calories }
        
        binding.apply {
            tvTarget.text = "2.080"
            tvConsumed.text = totalCalories.toString()
            tvRemaining.text = (2080 - totalCalories).toString()
        }
    }

    private fun showDeleteConfirmationDialog(meal: Meal) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(R.string.meal_delete_confirmation)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteMeal(meal)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 