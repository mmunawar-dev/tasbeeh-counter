package com.example.service

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import com.example.R

object AzanAudioPlayer {

  private const val TAG = "AzanAudioPlayer"
  private var mediaPlayer: MediaPlayer? = null
  private var wakeLock: PowerManager.WakeLock? = null
  private val lock = Any()

  fun playAzan(context: Context) {
    synchronized(lock) {
      stopAzan()

      try {
        // Stop any Quran or Allah Names recitation to prevent overlapping audio
        AudioPlayerService.stopPlayback(context)

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        wakeLock = powerManager?.newWakeLock(
          PowerManager.PARTIAL_WAKE_LOCK,
          "Sakinah:AzanWakeLock"
        )?.apply {
          setReferenceCounted(false)
          acquire(5 * 60 * 1000L) // 5 minutes max timeout
        }

        val player = MediaPlayer()
        val afd = context.resources.openRawResourceFd(R.raw.azan)
        if (afd != null) {
          player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
          afd.close()
        }

        player.setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        )

        player.isLooping = false

        player.setOnCompletionListener {
          Log.d(TAG, "Azan playback completed")
          stopAzan()
        }

        player.setOnErrorListener { _, what, extra ->
          Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
          stopAzan()
          true
        }

        player.prepare()
        player.start()
        mediaPlayer = player
        Log.d(TAG, "Azan playback started successfully")
      } catch (e: Exception) {
        Log.e(TAG, "Failed to start Azan playback", e)
        stopAzan()
      }
    }
  }

  fun stopAzan() {
    synchronized(lock) {
      try {
        mediaPlayer?.let { player ->
          if (player.isPlaying) {
            player.stop()
          }
          player.reset()
          player.release()
        }
      } catch (e: Exception) {
        Log.e(TAG, "Error stopping media player", e)
      } finally {
        mediaPlayer = null
      }

      try {
        wakeLock?.let { wl ->
          if (wl.isHeld) {
            wl.release()
          }
        }
      } catch (e: Exception) {
        Log.e(TAG, "Error releasing wake lock", e)
      } finally {
        wakeLock = null
      }
    }
  }

  fun isPlaying(): Boolean {
    synchronized(lock) {
      return try {
        mediaPlayer?.isPlaying == true
      } catch (_: Exception) {
        false
      }
    }
  }
}
