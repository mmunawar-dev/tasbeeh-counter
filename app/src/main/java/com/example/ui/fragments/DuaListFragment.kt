package com.example.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding.FragmentDuaListBinding
import com.example.model.DuaItem
import com.example.ui.activities.PremiumActivity
import com.example.ui.adapters.DuaListAdapter
import com.example.viewmodel.SakinahViewModel

class DuaListFragment : BaseFragment<FragmentDuaListBinding>(FragmentDuaListBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnDuaListBack.setOnClickListener {
      findNavController().navigateUp()
    }

    // Connect PRO button in toolbar to PremiumActivity as shown in reference design
    binding.btnDuaListPro.setOnClickListener {
      startActivity(Intent(requireContext(), PremiumActivity::class.java))
    }

    val category = viewModel.uiState.value.selectedDuaCategory
    if (category != null) {
      binding.tvDuaListCategoryTitle.text = category.title
      binding.tvDuaListCategoryArabic.text = category.arabicTitle

      val items = DuaItem.DUA_ITEMS.filter { it.categoryId == category.id }
      // All duas are rendered in full descriptive detail directly on the cards, no navigation to separate detail screen
      val adapter = DuaListAdapter(items)
      binding.rvDuasList.layoutManager = LinearLayoutManager(requireContext())
      binding.rvDuasList.adapter = adapter
    } else {
      findNavController().navigateUp()
    }
  }
}
