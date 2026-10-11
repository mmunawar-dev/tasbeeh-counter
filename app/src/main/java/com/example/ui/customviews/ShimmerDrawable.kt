package com.example.ui.customviews

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable

class ShimmerDrawable : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var progress = 0f

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {

        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        if (width <= 0f || height <= 0f) return

        val stripeWidth = height * 0.45f

        // Move from left -> right
        val x = -stripeWidth + (width + stripeWidth * 2f) * progress

        val path = Path()

        // Curved "/" shape
        path.moveTo(
            x - stripeWidth,
            height + 4f
        )

        path.quadTo(
            x - stripeWidth * 0.35f,
            height * 0.55f,
            x + stripeWidth * 0.20f,
            -4f
        )

        path.lineTo(
            x + stripeWidth * 0.60f,
            -4f
        )

        path.quadTo(
            x + stripeWidth * 0.05f,
            height * 0.55f,
            x - stripeWidth * 0.55f,
            height + 4f
        )

        path.close()

        paint.shader = LinearGradient(
            x - stripeWidth,
            height,
            x + stripeWidth,
            0f,
            intArrayOf(
                Color.TRANSPARENT,
                Color.argb(100, 255, 255, 255),
                Color.argb(210, 255, 255, 255),
                Color.argb(100, 255, 255, 255),
                Color.TRANSPARENT
            ),
            null,
            Shader.TileMode.CLAMP
        )

        canvas.drawPath(path, paint)

        paint.shader = null
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}