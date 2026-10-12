package com.example.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
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

  private val locationPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    if (coarseGranted || fineGranted) {
      obtainCurrentLocationAndReturn()
    } else {
      // Permission denied - still return result keeping existing selected city
      returnCityResultAndNavigateBack(viewModel.uiState.value.selectedCity)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnCityBack.setOnClickListener {
      findNavController().navigateUp()
    }

    adapter = CityAdapter(viewModel.uiState.value.selectedCity) { selected ->
      viewModel.selectCity(selected)
      returnCityResultAndNavigateBack(selected)
    }

    binding.rvCities.layoutManager = LinearLayoutManager(requireContext())
    binding.rvCities.adapter = adapter

    val gpsClickListener = View.OnClickListener {
      checkLocationPermissionAndObtain()
    }
    binding.btnCityUseGps.setOnClickListener(gpsClickListener)
    binding.layoutCurrentLocation.setOnClickListener(gpsClickListener)

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

  private fun checkLocationPermissionAndObtain() {
    val context = requireContext()
    val hasCoarse = ContextCompat.checkSelfPermission(
      context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val hasFine = ContextCompat.checkSelfPermission(
      context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (hasCoarse || hasFine) {
      obtainCurrentLocationAndReturn()
    } else {
      locationPermissionLauncher.launch(
        arrayOf(
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.ACCESS_COARSE_LOCATION
        )
      )
    }
  }

  private fun obtainCurrentLocationAndReturn() {
    binding.pbGpsLoading.visibility = View.VISIBLE
    binding.ivGpsIcon.visibility = View.GONE
    viewModel.detectLocation(requireContext()) { detectedCity ->
      if (isAdded) {
        returnCityResultAndNavigateBack(detectedCity)
      }
    }
  }

  private fun returnCityResultAndNavigateBack(city: CityLocation) {
    // 1. Shared backStackEntry SavedStateHandle
    findNavController().previousBackStackEntry?.savedStateHandle?.set("selected_city_id", city.id)
    findNavController().previousBackStackEntry?.savedStateHandle?.set("selected_city_name", city.name)

    // 2. FragmentResult API
    setFragmentResult(
      REQUEST_KEY_CITY_SELECTION,
      bundleOf(
        KEY_CITY_ID to city.id,
        KEY_CITY_NAME to city.name,
        KEY_COUNTRY to city.country,
        KEY_LATITUDE to city.latitude,
        KEY_LONGITUDE to city.longitude,
        KEY_IS_GPS to city.isGpsDetected
      )
    )

    // 3. Return to whatever caller opened this fragment (HomeFragment, QiblaFragment, AppSettingsFragment, etc.)
    findNavController().navigateUp()
  }

  private fun filterCities(selected: CityLocation) {
    val allCities = CityLocation.getAllCities(context)
    val list = if (currentQuery.isBlank()) {
      binding.tvCitySectionTitle.text = "POPULAR CITIES"
      allCities
    } else {
      val matches = allCities.filter {
        it.name.contains(currentQuery, ignoreCase = true) ||
          it.country.contains(currentQuery, ignoreCase = true)
      }
      binding.tvCitySectionTitle.text = "SEARCH RESULTS (${matches.size})"
      matches
    }
    adapter?.submitList(list, selected)
  }

  companion object {
    const val REQUEST_KEY_CITY_SELECTION = "city_selection_result"
    const val KEY_CITY_ID = "city_id"
    const val KEY_CITY_NAME = "city_name"
    const val KEY_COUNTRY = "country"
    const val KEY_LATITUDE = "latitude"
    const val KEY_LONGITUDE = "longitude"
    const val KEY_IS_GPS = "is_gps"
  }
}

