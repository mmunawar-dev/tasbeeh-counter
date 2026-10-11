package com.example.ui.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Native Android Custom View for Olive Wood Bead Tasbeeh
 * Faithfully matches SeedTasbeehInteractive in Compose.
 */
class SeedTasbeehView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private var count: Int = 0
  private var target: Int = 33
  private var isDark: Boolean = false
  private var onIncrementListener: (() -> Unit)? = null

  private var scaleFactor: Float = 1.0f
  private val scaleAnimator = ValueAnimator.ofFloat(1.0f, 0.96f, 1.0f).apply {
    duration = 180
    interpolator = OvershootInterpolator(1.2f)
    addUpdateListener {
      scaleFactor = it.animatedValue as Float
      invalidate()
    }
  }

  private val cordPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
  }

  private val beadShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.argb(64, 0, 0, 0)
    style = Paint.Style.FILL
  }

  private val beadPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
  }

  private val beadHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
  }

  private val separatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.parseColor("#FBBF24")
    style = Paint.Style.STROKE
  }

  private val imamBeadPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.parseColor("#D97706")
    style = Paint.Style.FILL
  }

  private val tasselPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.parseColor("#F59E0B")
    style = Paint.Style.STROKE
  }

  fun updateState(newCount: Int, newTarget: Int, darkMode: Boolean) {
    this.count = newCount
    this.target = newTarget
    this.isDark = darkMode
    invalidate()
  }

  fun setOnIncrementListener(listener: () -> Unit) {
    this.onIncrementListener = listener
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      if (!scaleAnimator.isRunning) {
        scaleAnimator.start()
      }
      onIncrementListener?.invoke()
      return true
    }
    return super.onTouchEvent(event)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val widthF = width.toFloat()
    val heightF = height.toFloat()
    if (widthF <= 0f || heightF <= 0f) return

    val cx = widthF / 2f
    val cy = heightF / 2f
    val radius = (minOf(widthF, heightF) * 0.40f) * scaleFactor

    cordPaint.color = if (isDark) Color.parseColor("#4B382A") else Color.parseColor("#B89876")
    cordPaint.strokeWidth = 3f * resources.displayMetrics.density

    // Cord
    canvas.drawCircle(cx, cy, radius, cordPaint)

    val beadCount = 33
    val activeIndex = count % beadCount
    val density = resources.displayMetrics.density

    // Draw individual beads
    for (i in 0 until beadCount) {
      val angle = (2.0 * PI / beadCount * i) - (PI / 2.0)
      val bx = (cx + radius * cos(angle)).toFloat()
      val by = (cy + radius * sin(angle)).toFloat()

      val isCurrent = (i == activeIndex)
      val isPassed = (i < activeIndex)
      val beadRadius = (if (isCurrent) 11f else 8.5f) * density

      // Gradient
      val colors = when {
        isCurrent -> intArrayOf(Color.parseColor("#F59E0B"), Color.parseColor("#B45309"), Color.parseColor("#78350F"))
        isPassed -> intArrayOf(Color.parseColor("#34D399"), Color.parseColor("#059669"), Color.parseColor("#064E3B"))
        isDark -> intArrayOf(Color.parseColor("#8D6E63"), Color.parseColor("#5D4037"), Color.parseColor("#3E2723"))
        else -> intArrayOf(Color.parseColor("#D7CCC8"), Color.parseColor("#A1887F"), Color.parseColor("#6D4C41"))
      }
      val shader = RadialGradient(
        bx - 3f, by - 3f, beadRadius * 1.5f,
        colors, null, Shader.TileMode.CLAMP
      )
      beadPaint.shader = shader

      // Shadow
      canvas.drawCircle(bx, by + 2f * density, beadRadius + 1f * density, beadShadowPaint)
      // Bead body
      canvas.drawCircle(bx, by, beadRadius, beadPaint)

      // Highlight
      beadHighlightPaint.color = Color.argb(if (isCurrent) 165 else 89, 255, 255, 255)
      canvas.drawCircle(bx - beadRadius * 0.3f, by - beadRadius * 0.3f, beadRadius * 0.35f, beadHighlightPaint)

      // Separator golden ring every 11 beads
      if (i % 11 == 0) {
        separatorPaint.strokeWidth = 1.5f * density
        canvas.drawCircle(bx, by, beadRadius + 2f * density, separatorPaint)
      }
    }

    // Imam bead at bottom (angle = PI / 2)
    val tasselAngle = PI / 2.0
    val tx = (cx + radius * cos(tasselAngle)).toFloat()
    val ty = (cy + radius * sin(tasselAngle)).toFloat()

    val imamRect = RectF(
      tx - 6f * density,
      ty,
      tx + 6f * density,
      ty + 22f * density
    )
    canvas.drawRoundRect(imamRect, 4f * density, 4f * density, imamBeadPaint)

    // Silk tassel strands
    tasselPaint.strokeWidth = 2f * density
    for (t in -2..2) {
      canvas.drawLine(
        tx + t * 3f * density,
        ty + 22f * density,
        tx + t * 5f * density,
        ty + 40f * density,
        tasselPaint
      )
    }
  }
}
