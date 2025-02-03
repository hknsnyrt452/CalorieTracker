package com.example.calorietracker.ui.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calorietracker.data.entity.Food
import com.example.calorietracker.databinding.ItemFoodBinding

class FoodAdapter(
    private val onEditClick: (Food) -> Unit,
    private val onDeleteClick: (Food) -> Unit
) : ListAdapter<Food, FoodAdapter.FoodViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FoodViewHolder(
        private val binding: ItemFoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.apply {
                tvFoodName.text = food.name
                tvCalories.text = "100g: ${food.caloriesPer100g} kcal"
                tvNutrients.text = "Protein: ${food.proteinPer100g}g | " +
                        "Karbonhidrat: ${food.carbsPer100g}g | " +
                        "Yağ: ${food.fatPer100g}g"

                btnEdit.setOnClickListener { onEditClick(food) }
                btnDelete.setOnClickListener { onDeleteClick(food) }
            }
        }
    }

    private class FoodDiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }
} 