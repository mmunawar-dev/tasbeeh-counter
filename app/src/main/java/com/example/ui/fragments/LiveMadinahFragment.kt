package com.example.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.R

class LiveMadinahFragment : BaseLiveStreamFragment() {
    override val streamTitle = "Live Madinah"
    override val streamArabicTitle = "بث مباشر من المسجد النبوي الشريف"
    override val locationName = "Al-Masjid an-Nabawi, Madinah"
    override val channelName = "Saudi Sunnah TV (قناة السنة النبوية)"
    override val youtubeUrl = "https://www.youtube.com/@saudisunnahtv/live"
    override val streamDesc = "Continuous 24/7 live transmission of prayers, Rawdah Sharif visits, and peaceful recitations from the Prophet's Mosque."
    override val isMakkah = false
}