package com.example.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding.FragmentAllahNamesBinding
import com.example.model.AllahName
import com.example.service.AudioPlayerService
import com.example.ui.adapters.AllahNamesAdapter
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AllahNamesFragment : BaseFragment<FragmentAllahNamesBinding>(FragmentAllahNamesBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var adapter: AllahNamesAdapter? = null
  private var currentSearch: String = ""

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnAllahNamesBack.setOnClickListener {
      findNavController().navigateUp()
    }

    adapter = AllahNamesAdapter()
    binding.rvAllahNames.layoutManager = LinearLayoutManager(requireContext())
    binding.rvAllahNames.adapter = adapter
    filterNames()

    binding.btnHeroStart.setOnClickListener {
      viewModel.playAllahNamesFullRecitation(requireContext())
    }

    binding.btnHeroToggle.setOnClickListener {
      viewModel.toggleAudioPlayback(requireContext())
    }

    binding.btnHeroStop.setOnClickListener {
      viewModel.stopAudioPlayback(requireContext())
    }

    binding.etNamesSearch.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        currentSearch = s?.toString()?.trim() ?: ""
        filterNames()
      }
      override fun afterTextChanged(s: Editable?) {}
    })

    viewLifecycleOwner.lifecycleScope.launch {
      AudioPlayerService.playbackState.collectLatest { state ->
        if (state.isServiceRunning) {
          binding.btnHeroStart.visibility = View.GONE
          binding.llAudioPlayerControls.visibility = View.VISIBLE

          val progress = if (state.durationMs > 0) {
            ((state.currentPositionMs.toFloat() / state.durationMs.toFloat()) * 100).toInt()
          } else 0
          binding.pbHeroAudioProgress.progress = progress

          binding.tvHeroCurrentTime.text = formatTime(state.currentPositionMs)
          binding.tvHeroTotalTime.text = if (state.durationMs > 0) formatTime(state.durationMs) else "--:--"

          binding.btnHeroToggle.text = if (state.isPlaying) "Pause" else "Resume"
        } else {
          binding.btnHeroStart.visibility = View.VISIBLE
          binding.llAudioPlayerControls.visibility = View.GONE
        }
      }
    }
  }

  private fun filterNames() {
    val list = if (currentSearch.isBlank()) {
      AllahName.NAMES_LIST
    } else {
      AllahName.NAMES_LIST.filter {
        it.transliteration.contains(currentSearch, ignoreCase = true) ||
          it.englishMeaning.contains(currentSearch, ignoreCase = true) ||
          it.arabic.contains(currentSearch)
      }
    }
    adapter?.submitList(list)
  }

  private fun formatTime(ms: Int): String {
    val totalSec = ms / 1000
    val minutes = totalSec / 60
    val seconds = totalSec % 60
    return "%02d:%02d".format(minutes, seconds)
  }
}
