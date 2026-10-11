package com.example.service

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import com.example.R

object TasbeehSoundPlayer {

  private const val TAG = "TasbeehSoundPlayer"
  private var soundPool: SoundPool? = null
  private var tap1SoundId: Int = 0
  private var tap2SoundId: Int = 0
  private var isTap1Ready = false
  private var isTap2Ready = false
  private var toggleTap = false

  @Synchronized
  fun init(context: Context) {
    if (soundPool != null) return

    try {
      val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

      val pool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(audioAttributes)
        .build()

      pool.setOnLoadCompleteListener { _, sampleId, status ->
        if (status == 0) {
          if (sampleId == tap1SoundId) isTap1Ready = true
          if (sampleId == tap2SoundId) isTap2Ready = true
        }
      }

      tap1SoundId = pool.load(context.applicationContext, R.raw.tap1, 1)
      tap2SoundId = pool.load(context.applicationContext, R.raw.tap2, 1)
      soundPool = pool
    } catch (e: Exception) {
      Log.e(TAG, "Failed to initialize SoundPool", e)
    }
  }

  @Synchronized
  fun playTapSound(context: Context) {
    if (soundPool == null) {
      init(context)
    }

    val pool = soundPool ?: return
    toggleTap = !toggleTap
    val soundId = if (toggleTap) tap2SoundId else tap1SoundId
    val isReady = if (toggleTap) isTap2Ready else isTap1Ready

    try {
      if (isReady && soundId != 0) {
        pool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
      } else if (tap1SoundId != 0) {
        // Fallback to tap1
        pool.play(tap1SoundId, 1.0f, 1.0f, 1, 0, 1.0f)
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error playing tap sound", e)
    }
  }

  @Synchronized
  fun release() {
    soundPool?.release()
    soundPool = null
    isTap1Ready = false
    isTap2Ready = false
    tap1SoundId = 0
    tap2SoundId = 0
  }
}
