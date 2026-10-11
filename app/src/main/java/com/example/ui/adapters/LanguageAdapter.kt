package com.example.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.R
import com.example.databinding.ItemLanguageCardBinding
import com.example.model.AppLanguage

class LanguageAdapter(
  private var selectedLanguage: AppLanguage,
  private val onLanguageSelected: (AppLanguage) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

  private val languages = AppLanguage.entries

  fun updateSelectedLanguage(newSelected: AppLanguage) {
    val previousIndex = languages.indexOf(selectedLanguage)
    val newIndex = languages.indexOf(newSelected)
    selectedLanguage = newSelected
    if (previousIndex != -1) notifyItemChanged(previousIndex)
    if (newIndex != -1) notifyItemChanged(newIndex)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
    val binding = ItemLanguageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return LanguageViewHolder(binding)
  }

  override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
    holder.bind(languages[position])
  }

  override fun getItemCount(): Int = languages.size

  inner class LanguageViewHolder(private val binding: ItemLanguageCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(language: AppLanguage) {
      val isSelected = language == selectedLanguage
      val context = binding.root.context

      binding.tvLanguageFlag.text = language.flagEmoji
      binding.tvLanguageDisplayName.text = language.displayName
      binding.tvLanguageNativeName.text = language.nativeName
      binding.rbLanguageSelected.isChecked = isSelected

      if (isSelected) {
        binding.cardLanguage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.active_prayer_bg))
        binding.cardLanguage.strokeColor = ContextCompat.getColor(context, R.color.active_prayer_border)
        binding.cardLanguage.strokeWidth = (1.5f * context.resources.displayMetrics.density).toInt()
        binding.tvLanguageDisplayName.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
      } else {
        binding.cardLanguage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
        binding.cardLanguage.strokeColor = ContextCompat.getColor(context, R.color.outline_border)
        binding.cardLanguage.strokeWidth = (1f * context.resources.displayMetrics.density).toInt()
        binding.tvLanguageDisplayName.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
      }

      binding.cardLanguage.setOnClickListener {
        onLanguageSelected(language)
      }
    }
  }
}
