package com.example.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.databinding.FragmentLiveStreamBinding
import com.example.viewmodel.SakinahViewModel


class LiveMakkahFragment : BaseLiveStreamFragment() {
  override val streamTitle = "Live Makkah"
  override val streamArabicTitle = "بث مباشر من المسجد الحرام بمكة المكرمة"
  override val locationName = "Masjid al-Haram, Makkah"
  override val channelName = "Saudi Quran TV (القرآن الكريم)"
  override val youtubeUrl = "https://www.youtube.com/watch?v=live_makkah"
  override val streamDesc = "Continuous 24/7 live transmission of prayers, circumambulation (Tawaf), and recitations from the Holy Kaaba."
  override val isMakkah = true
}


