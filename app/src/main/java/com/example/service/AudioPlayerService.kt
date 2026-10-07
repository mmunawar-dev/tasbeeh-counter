package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AudioPlayerService : Service() {

  data class PlaybackState(
    val isPlaying: Boolean = false,
    val isServiceRunning: Boolean = false,
    val trackTitle: String = "Asma-ul-Husna (99 Names of Allah)",
    val trackSubtitle: String = "Complete Melodious Recitation",
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0
  )

  companion object {
    const val CHANNEL_ID = "sakinah_audio_playback_channel"
    const val NOTIFICATION_ID = 1001

    const val ACTION_PLAY = "com.example.service.ACTION_PLAY"
    const val ACTION_PAUSE = "com.example.service.ACTION_PAUSE"
    const val ACTION_TOGGLE = "com.example.service.ACTION_TOGGLE"
    const val ACTION_STOP = "com.example.service.ACTION_STOP"
    const val ACTION_SEEK = "com.example.service.ACTION_SEEK"

    const val EXTRA_TITLE = "extra_title"
    const val EXTRA_SUBTITLE = "extra_subtitle"
    const val EXTRA_SEEK_MS = "extra_seek_ms"

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    fun startPlaying(context: Context, title: String = "Asma-ul-Husna (99 Names of Allah)", subtitle: String = "Complete Melodious Recitation") {
      val intent = Intent(context, AudioPlayerService::class.java).apply {
        action = ACTION_PLAY
        putExtra(EXTRA_TITLE, title)
        putExtra(EXTRA_SUBTITLE, subtitle)
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
      } else {
        context.startService(intent)
      }
    }

    fun togglePlayback(context: Context) {
      val intent = Intent(context, AudioPlayerService::class.java).apply {
        action = ACTION_TOGGLE
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
      } else {
        context.startService(intent)
      }
    }

    fun pausePlayback(context: Context) {
      val intent = Intent(context, AudioPlayerService::class.java).apply {
        action = ACTION_PAUSE
      }
      context.startService(intent)
    }

    fun stopPlayback(context: Context) {
      val intent = Intent(context, AudioPlayerService::class.java).apply {
        action = ACTION_STOP
      }
      context.startService(intent)
    }

    fun seekTo(context: Context, positionMs: Int) {
      val intent = Intent(context, AudioPlayerService::class.java).apply {
        action = ACTION_SEEK
        putExtra(EXTRA_SEEK_MS, positionMs)
      }
      context.startService(intent)
    }
  }

  private var mediaPlayer: MediaPlayer? = null
  private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
  private var progressTickerJob: Job? = null

  private var currentTitle = "Asma-ul-Husna (99 Names of Allah)"
  private var currentSubtitle = "Complete Melodious Recitation"

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
  }

  override fun onBind(intent: Intent?): IBinder? = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val action = intent?.action ?: ACTION_PLAY

    intent?.getStringExtra(EXTRA_TITLE)?.let { currentTitle = it }
    intent?.getStringExtra(EXTRA_SUBTITLE)?.let { currentSubtitle = it }

    when (action) {
      ACTION_PLAY -> handlePlay()
      ACTION_PAUSE -> handlePause()
      ACTION_TOGGLE -> handleToggle()
      ACTION_STOP -> handleStop()
      ACTION_SEEK -> {
        val seekMs = intent?.getIntExtra(EXTRA_SEEK_MS, 0) ?: 0
        handleSeek(seekMs)
      }
    }

    return START_NOT_STICKY
  }

  private fun handlePlay() {
    initMediaPlayerIfNeeded()

    mediaPlayer?.let { player ->
      if (!player.isPlaying) {
        player.start()
      }
      startProgressTicker()
      val duration = player.duration
      val position = player.currentPosition

      _playbackState.value = PlaybackState(
        isPlaying = true,
        isServiceRunning = true,
        trackTitle = currentTitle,
        trackSubtitle = currentSubtitle,
        currentPositionMs = position,
        durationMs = duration
      )

      startForegroundWithNotification(isPlaying = true)
    }
  }

  private fun handlePause() {
    mediaPlayer?.let { player ->
      if (player.isPlaying) {
        player.pause()
      }
      stopProgressTicker()
      val duration = player.duration
      val position = player.currentPosition

      _playbackState.value = _playbackState.value.copy(
        isPlaying = false,
        currentPositionMs = position,
        durationMs = duration
      )

      startForegroundWithNotification(isPlaying = false)
    }
  }

  private fun handleToggle() {
    if (mediaPlayer?.isPlaying == true) {
      handlePause()
    } else {
      handlePlay()
    }
  }

  private fun handleSeek(positionMs: Int) {
    mediaPlayer?.let { player ->
      val target = positionMs.coerceIn(0, player.duration)
      player.seekTo(target)
      _playbackState.value = _playbackState.value.copy(currentPositionMs = target)
    }
  }

  private fun handleStop() {
    stopProgressTicker()
    mediaPlayer?.let { player ->
      if (player.isPlaying) {
        player.stop()
      }
      player.reset()
      player.release()
    }
    mediaPlayer = null

    _playbackState.value = PlaybackState(
      isPlaying = false,
      isServiceRunning = false,
      trackTitle = currentTitle,
      trackSubtitle = currentSubtitle,
      currentPositionMs = 0,
      durationMs = 0
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      stopForeground(STOP_FOREGROUND_REMOVE)
    } else {
      @Suppress("DEPRECATION")
      stopForeground(true)
    }
    stopSelf()
  }

  private fun initMediaPlayerIfNeeded() {
    if (mediaPlayer != null) return

    try {
      mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
          AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        )
        val afd = resources.openRawResourceFd(R.raw.allah_names)
        if (afd != null) {
          setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
          afd.close()
        }
        prepare()

        setOnCompletionListener {
          stopProgressTicker()
          _playbackState.value = _playbackState.value.copy(
            isPlaying = false,
            currentPositionMs = 0
          )
          startForegroundWithNotification(isPlaying = false)
        }

        setOnErrorListener { _, _, _ ->
          handleStop()
          true
        }
      }
    } catch (_: Exception) {
      handleStop()
    }
  }

  private fun startProgressTicker() {
    progressTickerJob?.cancel()
    progressTickerJob = serviceScope.launch {
      while (isActive) {
        delay(400)
        mediaPlayer?.let { player ->
          if (player.isPlaying) {
            _playbackState.value = _playbackState.value.copy(
              isPlaying = true,
              currentPositionMs = player.currentPosition,
              durationMs = player.duration
            )
          }
        }
      }
    }
  }

  private fun stopProgressTicker() {
    progressTickerJob?.cancel()
    progressTickerJob = null
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        "Sakinah Audio Playback",
        NotificationManager.IMPORTANCE_LOW
      ).apply {
        description = "Controls for Asma-ul-Husna and prayer audio playback"
        setShowBadge(false)
      }
      val manager = getSystemService(NotificationManager::class.java)
      manager?.createNotificationChannel(channel)
    }
  }

  private fun startForegroundWithNotification(isPlaying: Boolean) {
    val notification = buildNotification(isPlaying)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      startForeground(
        NOTIFICATION_ID,
        notification,
        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
      )
    } else {
      startForeground(NOTIFICATION_ID, notification)
    }
  }

  private fun buildNotification(isPlaying: Boolean): Notification {
    val mainIntent = Intent(this, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    val contentPendingIntent = PendingIntent.getActivity(
      this,
      0,
      mainIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Toggle Play/Pause action intent
    val toggleIntent = Intent(this, AudioPlayerService::class.java).apply {
      action = ACTION_TOGGLE
    }
    val togglePendingIntent = PendingIntent.getService(
      this,
      1,
      toggleIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Stop action intent
    val stopIntent = Intent(this, AudioPlayerService::class.java).apply {
      action = ACTION_STOP
    }
    val stopPendingIntent = PendingIntent.getService(
      this,
      2,
      stopIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val playPauseActionTitle = if (isPlaying) "Pause" else "Play"
    val playPauseIcon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play

    return NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_notification_audio)
      .setContentTitle(currentTitle)
      .setContentText(currentSubtitle)
      .setSubText("Sakinah Audio")
      .setContentIntent(contentPendingIntent)
      .setOngoing(isPlaying)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .addAction(playPauseIcon, playPauseActionTitle, togglePendingIntent)
      .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .build()
  }

  override fun onDestroy() {
    handleStop()
    super.onDestroy()
  }
}
