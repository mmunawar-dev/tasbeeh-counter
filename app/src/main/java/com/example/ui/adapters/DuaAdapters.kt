package com.example.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.R
import com.example.databinding.ItemDuaCategoryBinding
import com.example.databinding.ItemDuaListEntryBinding
import com.example.model.DuaCategory
import com.example.model.DuaItem
import com.example.service.SakinahPreferences

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
      val count = category.count
      binding.tvDuaCatCount.text = if (count == 1) "1 Dua" else "$count Duas"
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
  private val onSelectDua: ((DuaItem) -> Unit)? = null
) : RecyclerView.Adapter<DuaListAdapter.DuaViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuaViewHolder {
    val binding = ItemDuaListEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return DuaViewHolder(binding)
  }

  override fun onBindViewHolder(holder: DuaViewHolder, position: Int) {
    holder.bind(duas[position], position)
  }

  override fun getItemCount(): Int = duas.size

  inner class DuaViewHolder(private val binding: ItemDuaListEntryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dua: DuaItem, position: Int) {
      val context = itemView.context

      // Sequential Numbering: "1.", "2.", "3."
      binding.tvDuaNumber.text = "${position + 1}."

      // Full Arabic Text
      binding.tvDuaEntryArabic.text = dua.arabic

      // Translation in signature emerald green
      binding.tvDuaEntryTranslation.text = dua.translation

      // Reference in signature golden amber
      binding.tvDuaEntryReference.text = dua.reference

      // Benefit note if present
      if (dua.benefit.isNotBlank()) {
        binding.tvDuaEntryBenefit.visibility = View.VISIBLE
        binding.tvDuaEntryBenefit.text = "Benefit: ${dua.benefit}"
      } else {
        binding.tvDuaEntryBenefit.visibility = View.GONE
      }

      // Favorite state
      val isFav = SakinahPreferences.isDuaFavorite(context, dua.id)
      binding.btnDuaFavorite.setImageResource(
        if (isFav) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
      )

      binding.btnDuaFavorite.setOnClickListener {
        val nowFav = SakinahPreferences.toggleDuaFavorite(context, dua.id)
        binding.btnDuaFavorite.setImageResource(
          if (nowFav) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
        )
        Toast.makeText(
          context,
          if (nowFav) "Added to Favorites" else "Removed from Favorites",
          Toast.LENGTH_SHORT
        ).show()
      }

      // Share button action
      binding.btnDuaShare.setOnClickListener {
        val shareBody = buildString {
          append(dua.title)
          append("\n\n")
          append(dua.arabic)
          append("\n\n\"")
          append(dua.translation)
          append("\"\n\n")
          append("Reference: ")
          append(dua.reference)
          if (dua.benefit.isNotBlank()) {
            append("\nBenefit: ")
            append(dua.benefit)
          }
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_SUBJECT, dua.title)
          putExtra(Intent.EXTRA_TEXT, shareBody)
        }
        context.startActivity(Intent.createChooser(intent, "Share Supplication"))
      }

      // Note: As requested, tapping the card does not navigate away to detail screen.
      // Optional listener preserved for callers if needed:
      onSelectDua?.let { listener ->
        itemView.setOnClickListener { listener(dua) }
      }
    }
  }
}
