package com.example.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentDuaDetailBinding
import com.example.databinding.FragmentDuaDetailBinding.inflate
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

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

//            binding.btnDetailPlayAudio.setOnClickListener {
//                viewModel.playDuaAudio(currentDua)
//            }

//            viewLifecycleOwner.lifecycleScope.launch {
//                viewModel.uiState.collectLatest { state ->
//                    val isPlaying = state.activeAudioTrack?.id == "dua_${currentDua.id}" && state.activeAudioTrack?.isPlaying == true
//                    binding.btnDetailPlayAudio.text = if (isPlaying) "Playing Recitation..." else "Listen to Dua Recitation"
//                }
//            }
        } else {
            findNavController().navigateUp()
        }
    }
}
