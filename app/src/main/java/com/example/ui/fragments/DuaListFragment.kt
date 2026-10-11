package com.example.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.R
import com.example.databinding.FragmentDuaListBinding
import com.example.model.DuaItem
import com.example.ui.adapters.DuaListAdapter
import com.example.ui.navigation.navigateWithSlide
import com.example.viewmodel.SakinahViewModel


class DuaListFragment : BaseFragment<FragmentDuaListBinding>(FragmentDuaListBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnDuaListBack.setOnClickListener {
      findNavController().navigateUp()
    }

    val category = viewModel.uiState.value.selectedDuaCategory
    if (category != null) {
      binding.tvDuaListCategoryTitle.text = category.title
      binding.tvDuaListCategoryArabic.text = category.arabicTitle

      val items = DuaItem.DUA_ITEMS.filter {
        it.categoryId == category.id || category.id == "morning_evening"
      }
      val adapter = DuaListAdapter(items) { selectedDua ->
        viewModel.selectDuaItem(selectedDua)
        findNavController().navigateWithSlide(R.id.duaDetailFragment)
      }
      binding.rvDuasList.layoutManager = LinearLayoutManager(requireContext())
      binding.rvDuasList.adapter = adapter
    } else {
      findNavController().navigateUp()
    }
  }
}

