package com.example.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.databinding.FragmentLanguageSelectBinding
import com.example.ui.adapters.LanguageAdapter
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LanguageSelectFragment : BaseFragment<FragmentLanguageSelectBinding>(FragmentLanguageSelectBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var languageAdapter: LanguageAdapter? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnLanguageBack.visibility = View.VISIBLE
    binding.btnLanguageBack.setOnClickListener {
      findNavController().navigateUp()
    }

    val currentLang = viewModel.uiState.value.selectedLanguage
    languageAdapter = LanguageAdapter(currentLang) { selected ->
      viewModel.selectLanguage(selected)
      languageAdapter?.updateSelectedLanguage(selected)
    }

    binding.rvLanguages.layoutManager = GridLayoutManager(requireContext(), 2)
    binding.rvLanguages.adapter = languageAdapter

    binding.btnLanguageApply.setOnClickListener {
      findNavController().navigateUp()
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest { state ->
        languageAdapter?.updateSelectedLanguage(state.selectedLanguage)
      }
    }
  }
}
