package com.example.kelompok10_tickdone

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress: Float = 0f // Progress from 0 to 100

    private val progressPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20f
        isAntiAlias = true
        color = ContextCompat.getColor(context, android.R.color.holo_orange_light) // Yellow color
    }

    private val backgroundPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
        color = ContextCompat.getColor(context, android.R.color.darker_gray) // Background color
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width / 2f) - 20f // Subtract stroke width

        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // Draw progress arc
        val sweepAngle = (progress / 100) * 360
        canvas.drawArc(
            centerX - radius, centerY - radius, centerX + radius, centerY + radius,
            -90f, sweepAngle, false, progressPaint
        )
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate() // Redraw the view
    }
}
