package com.example.mario

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Platform(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val type: Int = TYPE_GROUND
) {

    companion object {
        const val TYPE_GROUND = 0
        const val TYPE_BRICK = 1
        const val TYPE_BLOCK = 2
        const val TYPE_GOAL = 3
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = when (type) {
            TYPE_GROUND -> Color.parseColor("#8B4513")
            TYPE_BRICK -> Color.parseColor("#D2691E")
            TYPE_BLOCK -> Color.parseColor("#808080")
            TYPE_GOAL -> Color.parseColor("#4CAF50")
            else -> Color.GRAY
        }
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#3E2723")
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint)
        canvas.drawRect(x, y, x + width, y + height, borderPaint)
    }

    fun getRect(): RectF = RectF(x, y, x + width, y + height)
}
