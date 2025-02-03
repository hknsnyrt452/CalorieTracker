package com.example.calorietracker.ui.meal.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.calorietracker.R
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.databinding.FragmentMealListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MealListFragment : Fragment(R.layout.fragment_meal_list) {
    private var _binding: FragmentMealListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MealListViewModel by viewModels()
    private lateinit var mealAdapter: MealAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMealListBinding.bind(view)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter(
            onEditClick = { meal ->
                findNavController().navigate(
                    MealListFragmentDirections.actionMealListFragmentToEditMealFragment(meal.id)
                )
            },
            onDeleteClick = { meal ->
                showDeleteConfirmationDialog(meal)
            }
        )
        binding.rvMeals.adapter = mealAdapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dailyMeals.collectLatest { meals ->
                mealAdapter.submitList(meals)
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddMeal.setOnClickListener {
            findNavController().navigate(
                MealListFragmentDirections.actionMealListFragmentToAddMealFragment()
            )
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