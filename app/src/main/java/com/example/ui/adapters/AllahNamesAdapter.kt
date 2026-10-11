package com.example.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.ItemAllahNameBinding
import com.example.model.AllahName

class AllahNamesAdapter : RecyclerView.Adapter<AllahNamesAdapter.NameViewHolder>() {

  private var names: List<AllahName> = emptyList()
  private var expandedNumber: Int? = null

  fun submitList(newList: List<AllahName>) {
    this.names = newList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
    val binding = ItemAllahNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return NameViewHolder(binding)
  }

  override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
    holder.bind(names[position])
  }

  override fun getItemCount(): Int = names.size

  inner class NameViewHolder(private val binding: ItemAllahNameBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(name: AllahName) {
      val isExpanded = expandedNumber == name.number

      binding.tvNameNumber.text = name.number.toString()
      binding.tvNameTransliteration.text = name.transliteration
      binding.tvNameArabic.text = name.arabic

      // Downward arrow: rotates 180 (down) when collapsed, 00 (up) when expanded
      binding.ivExpandArrow.rotation = if (isExpanded) 180f else 0f

      // Expanded content
      if (isExpanded) {
        binding.layoutExpandedContent.visibility = View.VISIBLE
        binding.tvExpandedNumberPill.text = "NAME #${name.number}"
        binding.tvExpandedArabic.text = name.arabic
        binding.tvExpandedTransliteration.text = name.transliteration
        binding.tvExpandedMeaning.text = "“${name.englishMeaning}”"
        binding.tvExpandedExplanation.text = name.explanation
      } else {
        binding.layoutExpandedContent.visibility = View.GONE
      }

      val toggleListener = View.OnClickListener {
        val previousExpanded = expandedNumber
        expandedNumber = if (isExpanded) null else name.number

        // Rebind previously expanded and newly expanded items
        val currentPos = adapterPosition
        if (currentPos != RecyclerView.NO_POSITION) {
          notifyItemChanged(currentPos)
        }
        if (previousExpanded != null && previousExpanded != name.number) {
          val prevIndex = names.indexOfFirst { it.number == previousExpanded }
          if (prevIndex != -1) {
            notifyItemChanged(prevIndex)
          }
        }
      }

      binding.root.setOnClickListener(toggleListener)
      binding.ivExpandArrow.setOnClickListener(toggleListener)
    }
  }
}
