package com.example.calorietracker.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.databinding.DialogAddEditFoodBinding
import com.example.calorietracker.databinding.FragmentFoodListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodListFragment : Fragment() {

    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodViewModel by viewModels()
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeFoods()
    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter(
            onEditClick = { food -> showAddEditFoodDialog(food) },
            onDeleteClick = { food -> deleteFood(food) }
        )
        binding.rvFoods.adapter = foodAdapter
    }

    private fun setupClickListeners() {
        binding.fabAddFood.setOnClickListener {
            showAddEditFoodDialog(null)
        }
    }

    private fun observeFoods() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allFoods.collect { foods ->
                    foodAdapter.submitList(foods)
                }
            }
        }
    }

    private fun showAddEditFoodDialog(food: Food?) {
        val dialogBinding = DialogAddEditFoodBinding.inflate(layoutInflater)
        
        // Eğer düzenleme ise mevcut değerleri doldur
        food?.let {
            dialogBinding.apply {
                etFoodName.setText(it.name)
                etCalories.setText(it.caloriesPer100g.toString())
                etProtein.setText(it.proteinPer100g.toString())
                etCarbs.setText(it.carbsPer100g.toString())
                etFat.setText(it.fatPer100g.toString())
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (food == null) "Yemek Ekle" else "Yemeği Düzenle")
            .setView(dialogBinding.root)
            .setPositiveButton("Kaydet") { _, _ ->
                val newFood = Food(
                    id = food?.id ?: 0,
                    name = dialogBinding.etFoodName.text.toString(),
                    caloriesPer100g = dialogBinding.etCalories.text.toString().toFloatOrNull() ?: 0f,
                    proteinPer100g = dialogBinding.etProtein.text.toString().toFloatOrNull() ?: 0f,
                    carbsPer100g = dialogBinding.etCarbs.text.toString().toFloatOrNull() ?: 0f,
                    fatPer100g = dialogBinding.etFat.text.toString().toFloatOrNull() ?: 0f
                )

                if (food == null) {
                    viewModel.addFood(newFood)
                } else {
                    viewModel.updateFood(newFood)
                }
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    private fun deleteFood(food: Food) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Yemeği Sil")
            .setMessage("${food.name} yemeğini silmek istediğinizden emin misiniz?")
            .setPositiveButton("Sil") { _, _ ->
                viewModel.deleteFood(food)
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 