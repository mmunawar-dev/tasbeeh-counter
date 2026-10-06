package com.example.model

data class AudioTrack(
  val id: String,
  val title: String,
  val arabicTitle: String,
  val subtitle: String,
  val audioSource: String,
  val isPlaying: Boolean = false,
  val progressFraction: Float = 0f,
  val currentPositionSeconds: Int = 0,
  val durationSeconds: Int = 18
)
