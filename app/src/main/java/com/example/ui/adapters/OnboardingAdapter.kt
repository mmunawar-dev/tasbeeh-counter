package com.example.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.R
import com.example.databinding.ItemOnboardingPageBinding

data class OnboardingItem(
  val imageRes: Int,
  val title: String,
  val description: String,
  val subtitlePill: String
)

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

  val pages = listOf(
    OnboardingItem(
      imageRes = R.drawable.img_prayer_real_1791484687970,
      title = "Know Prayer Times",
      description = "Accurate daily prayer timetable calculated precisely for your location, sun position, and fiqh preferences.",
      subtitlePill = "ACCURATE & LOCALIZED"
    ),
    OnboardingItem(
      imageRes = R.drawable.img_tasbeeh_real_1791484707858,
      title = "Remember Allah / Tasbeeh",
      description = "Perform your daily dhikr with a digital Tasbeeh counter featuring authentic presets, bead tactile modes, and goal tracking.",
      subtitlePill = "DIGITAL TASBEEH & DHIKR"
    ),
    OnboardingItem(
      imageRes = R.drawable.img_qibla_real_1791484726025,
      title = "Qibla & Guidance",
      description = "Find the exact direction of the Holy Kaaba with a precise compass, plus authentic daily Duas and Islamic calendar.",
      subtitlePill = "QIBLA COMPASS & DUAS"
    )
  )

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
    val binding = ItemOnboardingPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return OnboardingViewHolder(binding)
  }

  override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
    holder.bind(pages[position])
  }

  override fun getItemCount(): Int = pages.size

  inner class OnboardingViewHolder(private val binding: ItemOnboardingPageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OnboardingItem) {
      binding.ivOnboardingHero.setImageResource(item.imageRes)
      binding.tvOnboardingPill.text = item.subtitlePill
      binding.tvOnboardingTitle.text = item.title
      binding.tvOnboardingDesc.text = item.description
    }
  }
}
