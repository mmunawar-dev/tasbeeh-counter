package com.example.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.ItemDuaCategoryBinding
import com.example.databinding.ItemDuaListEntryBinding
import com.example.model.DuaCategory
import com.example.model.DuaItem

class DuaCategoriesAdapter(
  private val categories: List<DuaCategory>,
  private val onSelectCategory: (DuaCategory) -> Unit
) : RecyclerView.Adapter<DuaCategoriesAdapter.CategoryViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
    val binding = ItemDuaCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return CategoryViewHolder(binding)
  }

  override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    holder.bind(categories[position])
  }

  override fun getItemCount(): Int = categories.size

  inner class CategoryViewHolder(private val binding: ItemDuaCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: DuaCategory) {
      binding.tvDuaCatTitle.text = category.title
      binding.tvDuaCatCount.text = "${category.count} Duas"
      binding.tvDuaCatArabic.text = category.arabicTitle
      binding.tvDuaCatDesc.text = category.description

      itemView.setOnClickListener {
        onSelectCategory(category)
      }
    }
  }
}

class DuaListAdapter(
  private val duas: List<DuaItem>,
  private val onSelectDua: (DuaItem) -> Unit
) : RecyclerView.Adapter<DuaListAdapter.DuaViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuaViewHolder {
    val binding = ItemDuaListEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return DuaViewHolder(binding)
  }

  override fun onBindViewHolder(holder: DuaViewHolder, position: Int) {
    holder.bind(duas[position])
  }

  override fun getItemCount(): Int = duas.size

  inner class DuaViewHolder(private val binding: ItemDuaListEntryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dua: DuaItem) {
      binding.tvDuaEntryTitle.text = dua.title
      binding.tvDuaEntryArabic.text = dua.arabic
      binding.tvDuaEntryTranslation.text = dua.translation


      itemView.setOnClickListener {
        onSelectDua(dua)
      }
    }
  }
}
