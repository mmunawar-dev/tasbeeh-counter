package com.example.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.PowerManager
import android.util.Log
import com.example.R

object AzanAudioPlayer {

  private const val TAG = "AzanAudioPlayer"
  private var mediaPlayer: MediaPlayer? = null
  private var wakeLock: PowerManager.WakeLock? = null
  private var audioManager: AudioManager? = null
  private var audioFocusRequest: AudioFocusRequest? = null
  private var legacyFocusListener: AudioManager.OnAudioFocusChangeListener? = null
  private var userInterruptionReceiver: BroadcastReceiver? = null
  private var appContext: Context? = null
  private val lock = Any()

  fun playAzan(context: Context) {
    synchronized(lock) {
      stopAzan()

      try {
        val appCtx = context.applicationContext
        appContext = appCtx

        // Stop any background Quran / Allah Names recitation to prevent overlapping audio
        AudioPlayerService.stopPlayback(appCtx)

        val am = appCtx.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        audioManager = am

        val audioAttributes = AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_ALARM)
          .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
          .build()

        // 1. Request Audio Focus properly without changing system volume
        var focusGranted = false
        if (am != null) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val afr = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
              .setAudioAttributes(audioAttributes)
              .setAcceptsDelayedFocusGain(false)
              .setOnAudioFocusChangeListener { focusChange ->
                when (focusChange) {
                  AudioManager.AUDIOFOCUS_LOSS,
                  AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    Log.d(TAG, "Audio focus lost ($focusChange). Stopping Azan playback.")
                    stopAzan()
                  }
                }
              }
              .build()
            audioFocusRequest = afr
            focusGranted = am.requestAudioFocus(afr) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
          } else {
            val listener = AudioManager.OnAudioFocusChangeListener { focusChange ->
              if (focusChange == AudioManager.AUDIOFOCUS_LOSS || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                Log.d(TAG, "Audio focus lost ($focusChange). Stopping Azan playback.")
                stopAzan()
              }
            }
            legacyFocusListener = listener
            @Suppress("DEPRECATION")
            focusGranted = am.requestAudioFocus(
              listener,
              AudioManager.STREAM_ALARM,
              AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
          }
        }

        if (!focusGranted) {
          Log.w(TAG, "Audio focus not granted, proceeding with normal playback")
        }

        // 2. Partial WakeLock for playback duration
        val powerManager = appCtx.getSystemService(Context.POWER_SERVICE) as? PowerManager
        wakeLock = powerManager?.newWakeLock(
          PowerManager.PARTIAL_WAKE_LOCK,
          "Sakinah:AzanWakeLock"
        )?.apply {
          setReferenceCounted(false)
          acquire(4 * 60 * 1000L) // 4 minutes max safety timeout
        }

        // 3. Setup MediaPlayer respecting current system volume & non-looping
        val player = MediaPlayer()
        val afd = appCtx.resources.openRawResourceFd(R.raw.azan)
        if (afd != null) {
          player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
          afd.close()
        }

        player.setAudioAttributes(audioAttributes)
        player.isLooping = false

        player.setOnCompletionListener {
          Log.d(TAG, "Azan playback completed naturally")
          stopAzan()
        }

        player.setOnErrorListener { _, what, extra ->
          Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
          stopAzan()
          true
        }

        // 4. Register BroadcastReceiver to stop Azan when user presses Power (Screen Off) or Volume Keys
        registerInterruptionReceiver(appCtx)

        player.prepare()
        player.start()
        mediaPlayer = player
        Log.d(TAG, "Azan playback started successfully at system volume")
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

      // Abandon Audio Focus
      try {
        audioManager?.let { am ->
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { afr -> am.abandonAudioFocusRequest(afr) }
          } else {
            legacyFocusListener?.let { l ->
              @Suppress("DEPRECATION")
              am.abandonAudioFocus(l)
            }
          }
        }
      } catch (e: Exception) {
        Log.e(TAG, "Error abandoning audio focus", e)
      } finally {
        audioFocusRequest = null
        legacyFocusListener = null
        audioManager = null
      }

      // Unregister Screen Off & Volume Change Interruption Receiver
      try {
        userInterruptionReceiver?.let { r ->
          appContext?.unregisterReceiver(r)
        }
      } catch (e: Exception) {
        Log.e(TAG, "Error unregistering interruption receiver", e)
      } finally {
        userInterruptionReceiver = null
      }

      // Release WakeLock
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

  private fun registerInterruptionReceiver(context: Context) {
    try {
      val receiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
          val action = intent?.action
          Log.d(TAG, "Interruption receiver triggered action: $action. Stopping Azan.")
          stopAzan()
        }
      }

      val filter = IntentFilter().apply {
        addAction(Intent.ACTION_SCREEN_OFF)
        addAction("android.media.VOLUME_CHANGED_ACTION")
        addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
      }

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
      } else {
        context.registerReceiver(receiver, filter)
      }
      userInterruptionReceiver = receiver
    } catch (e: Exception) {
      Log.w(TAG, "Failed to register screen/volume interruption receiver", e)
    }
  }
}
