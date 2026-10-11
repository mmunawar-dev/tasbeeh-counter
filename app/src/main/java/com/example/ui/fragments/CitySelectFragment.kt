package com.example.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding.FragmentCitySelectBinding
import com.example.model.CityLocation
import com.example.ui.adapters.CityAdapter
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CitySelectFragment : BaseFragment<FragmentCitySelectBinding>(FragmentCitySelectBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var adapter: CityAdapter? = null
  private var currentQuery: String = ""

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnCityBack.setOnClickListener {
      findNavController().navigateUp()
    }

    adapter = CityAdapter(viewModel.uiState.value.selectedCity) { selected ->
      viewModel.selectCity(selected)
      findNavController().navigateUp()
    }

    binding.rvCities.layoutManager = LinearLayoutManager(requireContext())
    binding.rvCities.adapter = adapter

    binding.btnCityUseGps.setOnClickListener {
      viewModel.detectLocation(requireContext())
      findNavController().navigateUp()
    }

    binding.etCitySearch.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        currentQuery = s?.toString()?.trim() ?: ""
        filterCities(viewModel.uiState.value.selectedCity)
      }
      override fun afterTextChanged(s: Editable?) {}
    })

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest { state ->
        if (state.isGpsSearching) {
          binding.pbGpsLoading.visibility = View.VISIBLE
          binding.ivGpsIcon.visibility = View.GONE
        } else {
          binding.pbGpsLoading.visibility = View.GONE
          binding.ivGpsIcon.visibility = View.VISIBLE
        }
        filterCities(state.selectedCity)
      }
    }
  }

  private fun filterCities(selected: CityLocation) {
    val list = if (currentQuery.isBlank()) {
      binding.tvCitySectionTitle.text = "POPULAR CITIES"
      CityLocation.PRESET_CITIES
    } else {
      binding.tvCitySectionTitle.text = "SEARCH RESULTS"
      CityLocation.PRESET_CITIES.filter {
        it.name.contains(currentQuery, ignoreCase = true) ||
          it.country.contains(currentQuery, ignoreCase = true)
      }
    }
    adapter?.submitList(list, selected)
  }
}
