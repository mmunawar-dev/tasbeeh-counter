package com.example.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.databinding.FragmentDuaDetailBinding
import com.example.viewmodel.SakinahViewModel

class DuaDetailFragment : BaseFragment<FragmentDuaDetailBinding>(FragmentDuaDetailBinding::inflate) {

    private val viewModel: SakinahViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDuaDetailBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val currentDua = viewModel.uiState.value.selectedDuaItem
        if (currentDua != null) {
            binding.tvDetailDuaTitle.text = currentDua.title
            binding.tvDetailDuaRef.text = currentDua.reference
            binding.tvDetailDuaArabic.text = currentDua.arabic
            binding.tvDetailDuaTransliteration.text = currentDua.transliteration
            binding.tvDetailDuaTranslation.text = "“${currentDua.translation}”"
            binding.tvDetailDuaBenefit.text = currentDua.benefit

            binding.btnDuaDetailShare.setOnClickListener {
                val shareBody = buildString {
                    append(currentDua.title)
                    append("\n\n")
                    append(currentDua.arabic)
                    append("\n\n")
                    append(currentDua.transliteration)
                    append("\n\n\"")
                    append(currentDua.translation)
                    append("\"\n\n")
                    append("Reference: ")
                    append(currentDua.reference)
                    if (currentDua.benefit.isNotBlank()) {
                        append("\nBenefit: ")
                        append(currentDua.benefit)
                    }
                }
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, currentDua.title)
                    putExtra(Intent.EXTRA_TEXT, shareBody)
                }
                startActivity(Intent.createChooser(intent, "Share Supplication"))
            }
        } else {
            findNavController().navigateUp()
        }
    }
}
