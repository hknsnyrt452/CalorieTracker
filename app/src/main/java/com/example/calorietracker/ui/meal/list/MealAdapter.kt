package com.example.calorietracker.ui.meal.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calorietracker.data.entity.Meal
import com.example.calorietracker.databinding.ItemMealBinding
import java.time.format.DateTimeFormatter

class MealAdapter(
    private val onEditClick: (Meal) -> Unit,
    private val onDeleteClick: (Meal) -> Unit
) : ListAdapter<Meal, MealAdapter.MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MealViewHolder(
        private val binding: ItemMealBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            binding.apply {
                tvMealName.text = meal.name
                tvMealType.text = meal.type.name
                tvDateTime.text = meal.dateTime.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )

                btnEdit.setOnClickListener { onEditClick(meal) }
                btnDelete.setOnClickListener { onDeleteClick(meal) }
            }
        }
    }

    private class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal) = 
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal) = 
            oldItem == newItem
    }
} 