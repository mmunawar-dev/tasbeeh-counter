package com.example.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentLiveStreamBinding
import com.example.viewmodel.SakinahViewModel

abstract class BaseLiveStreamFragment : BaseFragment<FragmentLiveStreamBinding>(FragmentLiveStreamBinding::inflate) {

    protected val viewModel: SakinahViewModel by activityViewModels()

    protected abstract val streamTitle: String
    protected abstract val streamArabicTitle: String
    protected abstract val locationName: String
    protected abstract val channelName: String
    protected abstract val youtubeUrl: String
    protected abstract val streamDesc: String
    protected abstract val isMakkah: Boolean

    private var isPlaying = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Header
        binding.btnLiveBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvLiveTitle.text = streamTitle
        binding.tvLiveArabicTitle.text = streamArabicTitle

        // Player card
        binding.tvLiveSiteEmoji.text = if (isMakkah) "🕋" else "🕌"
        binding.ivLiveSiteBg.setImageResource(
            if (isMakkah) R.drawable.makkah else R.drawable.madinah
        )
        binding.tvLiveStreamStatus.text = "Streaming Live from $locationName"

        // Info card
        binding.tvLiveLocation.text = locationName
        binding.tvLiveChannel.text = channelName
        binding.tvLiveDesc.text = streamDesc

        // Play / Pause toggle (UI only — no actual player)
        binding.btnLivePlayPause.setOnClickListener {
            isPlaying = !isPlaying
            binding.tvLiveStreamStatus.text =
                if (isPlaying) "Streaming Live from $locationName" else "Stream Paused"
            binding.btnLivePlayPause.setImageResource(
                if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
            )
        }

        // Open on YouTube — icon button in player bar
        binding.btnLiveOpenYoutube.setOnClickListener { openYoutube() }

        // Open on YouTube — row in info card
        binding.btnLiveOpenYoutubeRow.setOnClickListener { openYoutube() }
    }

    private fun openYoutube() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        startActivity(intent)
    }
}
