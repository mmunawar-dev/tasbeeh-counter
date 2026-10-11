package com.example.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.R
import com.example.databinding.ItemCityLocationBinding
import com.example.model.CityLocation

class CityAdapter(
  private var currentSelectedCity: CityLocation,
  private val onCitySelected: (CityLocation) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

  private var cities: List<CityLocation> = emptyList()

  fun submitList(newCities: List<CityLocation>, selected: CityLocation) {
    this.cities = newCities
    this.currentSelectedCity = selected
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
    val binding = ItemCityLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return CityViewHolder(binding)
  }

  override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
    holder.bind(cities[position])
  }

  override fun getItemCount(): Int = cities.size

  inner class CityViewHolder(private val binding: ItemCityLocationBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(city: CityLocation) {
      val isSelected = city.name.equals(currentSelectedCity.name, ignoreCase = true)
      val context = itemView.context

      binding.tvCityName.text = city.name
      val gmtStr = if (city.timeZoneOffsetHours >= 0) "+${city.timeZoneOffsetHours.toInt()}" else "${city.timeZoneOffsetHours.toInt()}"
      binding.tvCityCountryGmt.text = "${city.country} • GMT $gmtStr"

      if (isSelected) {
        binding.cardCity.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_variant_light))
        binding.cardCity.strokeColor = ContextCompat.getColor(context, R.color.emerald_primary)
        binding.cardCity.strokeWidth = (1.5f * itemView.resources.displayMetrics.density).toInt()
        binding.cardCitySelectedBadge.visibility = View.VISIBLE
      } else {
        binding.cardCity.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
        binding.cardCity.strokeColor = ContextCompat.getColor(context, R.color.outline_border)
        binding.cardCity.strokeWidth = (1f * itemView.resources.displayMetrics.density).toInt()
        binding.cardCitySelectedBadge.visibility = View.GONE
      }

      itemView.setOnClickListener {
        onCitySelected(city)
      }
    }
  }
}
