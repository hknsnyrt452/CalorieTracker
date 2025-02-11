package com.example.calorietracker.ui.food.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.calorietracker.R
import com.example.calorietracker.databinding.FragmentFoodListBinding
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.ui.food.FoodAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodListFragment : Fragment(R.layout.fragment_food_list) {
    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodListViewModel by viewModels()
    private lateinit var foodAdapter: FoodAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFoodListBinding.bind(view)
        
        setupRecyclerView()
        setupFab()
        observeViewModel()

        // Test verilerini kaldırdık çünkü veritabanında zaten varsayılan veriler var
    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter(
            onEditClick = { food ->
                // TODO: Navigate to edit screen
            },
            onDeleteClick = { food ->
                viewModel.deleteFood(food)
            }
        )
        
        binding.rvFoods.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupFab() {
        binding.fabAddFood.setOnClickListener {
            // TODO: Navigate to add food screen
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.foods.collect { foodList ->
                foodAdapter.submitList(foodList)
                
                // Boş liste kontrolü
                binding.tvEmptyList.visibility = if (foodList.isEmpty()) View.VISIBLE else View.GONE
                binding.rvFoods.visibility = if (foodList.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 