package com.example.ui.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import kotlin.math.cos
import kotlin.math.sin

/**
 * Native Android Custom View for Qibla Compass Dial
 * Faithfully reproduces CompassDialCanvas for all 5 styles:
 * CLASSIC_GOLD, EMERALD_MINIMAL, ASTROLABE, KAABA_ARROW, NIGHT_CELESTIAL
 */
class QiblaCompassDialView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private var styleName: String = "EMERALD_MINIMAL"
  private var isAligned: Boolean = false
  private var headingDegrees: Float = 0f
  private var qiblaBearingDegrees: Float = 118f

  private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
  }

  private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
  }

  private val innerRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
  }

  private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
  }

  private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    textAlign = Paint.Align.CENTER
    typeface = Typeface.DEFAULT_BOLD
  }

  private val needlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
  }

  private val needlePath = Path()

  fun updateCompass(
    style: String,
    heading: Float,
    qiblaBearing: Float,
    aligned: Boolean
  ) {
    this.styleName = style
    this.headingDegrees = heading
    this.qiblaBearingDegrees = qiblaBearing
    this.isAligned = aligned
    invalidate()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val widthF = width.toFloat()
    val heightF = height.toFloat()
    if (widthF <= 0f || heightF <= 0f) return

    val cx = widthF / 2f
    val cy = heightF / 2f
    val density = resources.displayMetrics.density
    val radius = (minOf(widthF, heightF) / 2f) - (4f * density)

    // 1. Draw outer bezel & background according to style
    when (styleName) {
      "CLASSIC_GOLD" -> {
        bgPaint.color = Color.parseColor("#FBF8F0")
        canvas.drawCircle(cx, cy, radius, bgPaint)

        ringPaint.color = if (isAligned) Color.parseColor("#1B8A5A") else Color.parseColor("#D4AF37")
        ringPaint.strokeWidth = (if (isAligned) 4f else 3f) * density
        canvas.drawCircle(cx, cy, radius, ringPaint)

        innerRingPaint.color = Color.parseColor("#E8D49B")
        innerRingPaint.strokeWidth = 1f * density
        canvas.drawCircle(cx, cy, radius - (8f * density), innerRingPaint)

        // Degree ticks every 15 deg
        for (deg in 0 until 360 step 15) {
          val rad = Math.toRadians(deg.toDouble())
          val isMajor = deg % 90 == 0
          val isMedium = deg % 30 == 0
          val tickLen = (if (isMajor) 12f else if (isMedium) 8f else 4f) * density
          val startR = radius - (8f * density)
          val endR = startR - tickLen

          val sx = cx + (startR * sin(rad)).toFloat()
          val sy = cy - (startR * cos(rad)).toFloat()
          val ex = cx + (endR * sin(rad)).toFloat()
          val ey = cy - (endR * cos(rad)).toFloat()

          tickPaint.color = if (isMajor) Color.parseColor("#996515") else Color.parseColor("#D4B37A")
          tickPaint.strokeWidth = (if (isMajor) 2f else 1f) * density
          canvas.drawLine(sx, sy, ex, ey, tickPaint)
        }
      }

      "ASTROLABE" -> {
        bgPaint.color = Color.parseColor("#F5EFE1")
        canvas.drawCircle(cx, cy, radius, bgPaint)

        ringPaint.color = Color.parseColor("#8B6B38")
        ringPaint.strokeWidth = 3f * density
        canvas.drawCircle(cx, cy, radius, ringPaint)

        innerRingPaint.color = Color.parseColor("#B89B60")
        innerRingPaint.strokeWidth = 1.2f * density
        canvas.drawCircle(cx, cy, radius - (12f * density), innerRingPaint)

        for (deg in 0 until 360 step 10) {
          val rad = Math.toRadians(deg.toDouble())
          val isMajor = deg % 30 == 0
          val tickLen = (if (isMajor) 10f else 4f) * density
          val startR = radius - (2f * density)
          val endR = startR - tickLen

          val sx = cx + (startR * sin(rad)).toFloat()
          val sy = cy - (startR * cos(rad)).toFloat()
          val ex = cx + (endR * sin(rad)).toFloat()
          val ey = cy - (endR * cos(rad)).toFloat()

          tickPaint.color = if (isMajor) Color.parseColor("#6E4D1B") else Color.parseColor("#A6854F")
          tickPaint.strokeWidth = 1f * density
          canvas.drawLine(sx, sy, ex, ey, tickPaint)
        }
      }

      "KAABA_ARROW" -> {
        bgPaint.color = Color.parseColor("#FAFAFA")
        canvas.drawCircle(cx, cy, radius, bgPaint)

        ringPaint.color = if (isAligned) Color.parseColor("#1B8A5A") else Color.parseColor("#424242")
        ringPaint.strokeWidth = 3f * density
        canvas.drawCircle(cx, cy, radius, ringPaint)

        innerRingPaint.color = if (isAligned) Color.parseColor("#55E6B7") else Color.parseColor("#E0E0E0")
        innerRingPaint.strokeWidth = 2f * density
        canvas.drawCircle(cx, cy, radius - (6f * density), innerRingPaint)

        for (deg in 0 until 360 step 30) {
          val rad = Math.toRadians(deg.toDouble())
          val tickLen = 8f * density
          val startR = radius - (6f * density)
          val endR = startR - tickLen

          val sx = cx + (startR * sin(rad)).toFloat()
          val sy = cy - (startR * cos(rad)).toFloat()
          val ex = cx + (endR * sin(rad)).toFloat()
          val ey = cy - (endR * cos(rad)).toFloat()

          tickPaint.color = Color.parseColor("#616161")
          tickPaint.strokeWidth = 1.5f * density
          canvas.drawLine(sx, sy, ex, ey, tickPaint)
        }
      }

      "NIGHT_CELESTIAL" -> {
        bgPaint.color = Color.parseColor("#0F1A15")
        canvas.drawCircle(cx, cy, radius, bgPaint)

        ringPaint.color = if (isAligned) Color.parseColor("#55E6B7") else Color.parseColor("#264C3B")
        ringPaint.strokeWidth = 2.5f * density
        canvas.drawCircle(cx, cy, radius, ringPaint)

        innerRingPaint.color = Color.parseColor("#1A382B")
        innerRingPaint.strokeWidth = 1f * density
        canvas.drawCircle(cx, cy, radius - (10f * density), innerRingPaint)

        for (deg in 0 until 360 step 20) {
          val rad = Math.toRadians(deg.toDouble())
          val starR = radius - (6f * density)
          val sx = cx + (starR * sin(rad)).toFloat()
          val sy = cy - (starR * cos(rad)).toFloat()
          val isMajor = deg % 60 == 0
          tickPaint.color = if (isMajor) Color.parseColor("#72DFBA") else Color.parseColor("#386151")
          tickPaint.style = Paint.Style.FILL
          canvas.drawCircle(sx, sy, (if (isMajor) 2f else 1.2f) * density, tickPaint)
          tickPaint.style = Paint.Style.STROKE
        }
      }

      else -> { // EMERALD_MINIMAL
        bgPaint.color = Color.parseColor("#F4F9F6")
        canvas.drawCircle(cx, cy, radius, bgPaint)

        ringPaint.color = if (isAligned) Color.parseColor("#1B8A5A") else Color.parseColor("#2C6B55")
        ringPaint.strokeWidth = 2.5f * density
        canvas.drawCircle(cx, cy, radius, ringPaint)

        for (deg in 0 until 360 step 30) {
          val rad = Math.toRadians(deg.toDouble())
          val isMajor = deg % 90 == 0
          val tickLen = (if (isMajor) 10f else 5f) * density
          val startR = radius - (4f * density)
          val endR = startR - tickLen

          val sx = cx + (startR * sin(rad)).toFloat()
          val sy = cy - (startR * cos(rad)).toFloat()
          val ex = cx + (endR * sin(rad)).toFloat()
          val ey = cy - (endR * cos(rad)).toFloat()

          tickPaint.color = if (isMajor) Color.parseColor("#16523F") else Color.parseColor("#6BB599")
          tickPaint.strokeWidth = 1.5f * density
          canvas.drawLine(sx, sy, ex, ey, tickPaint)
        }
      }
    }

    // 2. Rotating Compass Rose (-headingDegrees)
    canvas.save()
    canvas.rotate(-headingDegrees, cx, cy)

    // Draw Cardinal Labels: N, S, E, W
    textPaint.textSize = 13f * density
    textPaint.color = if (styleName == "NIGHT_CELESTIAL") Color.parseColor("#55E6B7") else Color.parseColor("#E53935")
    canvas.drawText("N", cx, cy - radius + (22f * density), textPaint)

    textPaint.textSize = 11f * density
    textPaint.color = if (styleName == "NIGHT_CELESTIAL") Color.parseColor("#88A096") else Color.parseColor("#64748B")
    canvas.drawText("S", cx, cy + radius - (12f * density), textPaint)
    canvas.drawText("E", cx + radius - (16f * density), cy + (4f * density), textPaint)
    canvas.drawText("W", cx - radius + (16f * density), cy + (4f * density), textPaint)

    // Draw Qibla pointer arrow rotated by qiblaBearingDegrees
    canvas.save()
    canvas.rotate(qiblaBearingDegrees, cx, cy)

    val arrowTopY = cy - radius + (26f * density)
    val arrowWidth = (if (styleName == "KAABA_ARROW") 28f else 24f) * density
    val arrowHeight = (if (styleName == "KAABA_ARROW") 32f else 28f) * density

    needlePath.reset()
    needlePath.moveTo(cx, arrowTopY)
    needlePath.lineTo(cx + arrowWidth / 2f, arrowTopY + arrowHeight)
    needlePath.lineTo(cx, arrowTopY + arrowHeight * 0.72f)
    needlePath.lineTo(cx - arrowWidth / 2f, arrowTopY + arrowHeight)
    needlePath.close()

    needlePaint.color = when (styleName) {
      "CLASSIC_GOLD" -> if (isAligned) Color.parseColor("#1B8A5A") else Color.parseColor("#D4AF37")
      "ASTROLABE" -> if (isAligned) Color.parseColor("#1B8A5A") else Color.parseColor("#78541D")
      "KAABA_ARROW" -> if (isAligned) Color.parseColor("#1B8A5A") else Color.parseColor("#2E7D32")
      "NIGHT_CELESTIAL" -> if (isAligned) Color.parseColor("#55E6B7") else Color.parseColor("#388E6E")
      else -> if (isAligned) Color.parseColor("#249D6A") else Color.parseColor("#185D46")
    }
    canvas.drawPath(needlePath, needlePaint)

    // Kaaba icon / label below arrowhead
    textPaint.textSize = 14f * density
    canvas.drawText("🕋", cx, arrowTopY + arrowHeight + (18f * density), textPaint)

    canvas.restore() // restore qibla rotate
    canvas.restore() // restore heading rotate

    // 3. Fixed Phone Forward Indicator at top (pointing straight up)
    val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      style = Paint.Style.FILL
      color = if (isAligned) {
        Color.parseColor("#1B8A5A")
      } else when (styleName) {
        "CLASSIC_GOLD" -> Color.parseColor("#D4AF37")
        "NIGHT_CELESTIAL" -> Color.parseColor("#55E6B7")
        else -> Color.parseColor("#10B981")
      }
    }
    val indWidth = (if (styleName == "KAABA_ARROW") 4f else 3f) * density
    val indHeight = (if (styleName == "KAABA_ARROW") 34f else 30f) * density
    canvas.drawRoundRect(
      RectF(cx - indWidth / 2f, 2f * density, cx + indWidth / 2f, 2f * density + indHeight),
      2f * density, 2f * density, indicatorPaint
    )
  }
}
