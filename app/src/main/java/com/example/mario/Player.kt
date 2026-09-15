package com.example.mario

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Player(startX: Float, startY: Float) {

    var x = startX
    var y = startY
    var width = 60f
    var height = 80f

    var vx = 0f
    var vy = 0f

    var onGround = false

    // Настройки физики
    private val moveSpeed = 8f
    private val gravity = 0.9f
    private val jumpPower = -20f
    private val maxFallSpeed = 25f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E53935") // красный
    }

    fun update(viewWidth: Float, viewHeight: Float) {
        // Гравитация
        vy += gravity
        if (vy > maxFallSpeed) vy = maxFallSpeed

        // Движение
        x += vx
        y += vy

        // Границы экрана
        if (x < 0) { x = 0f; vx = 0f }
        if (x + width > viewWidth) { x = viewWidth - width; vx = 0f }

        // Падение в яму (ниже экрана) — потом обработаем смерть
        if (y > viewHeight) {
            y = viewHeight.toFloat()
            vy = 0f
        }
    }

    fun moveLeft() { vx = -moveSpeed }
    fun moveRight() { vx = moveSpeed }
    fun stopMoving() { vx = 0f }

    fun jump() {
        if (onGround) {
            vy = jumpPower
            onGround = false
        }
    }

    fun draw(canvas: Canvas) {
        // Тело
        canvas.drawRect(x, y, x + width, y + height, paint)

        // Глаза (белые с чёрными зрачками)
        val eyePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
        val pupilPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLACK }

        canvas.drawCircle(x + width * 0.35f, y + height * 0.3f, 7f, eyePaint)
        canvas.drawCircle(x + width * 0.65f, y + height * 0.3f, 7f, eyePaint)
        canvas.drawCircle(x + width * 0.35f, y + height * 0.3f, 3f, pupilPaint)
        canvas.drawCircle(x + width * 0.65f, y + height * 0.3f, 3f, pupilPaint)

        // Рот
        val mouthPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }
        canvas.drawArc(
            x + width * 0.3f, y + height * 0.5f,
            x + width * 0.7f, y + height * 0.7f,
            0f, 180f, false, mouthPaint
        )
    }

    fun getRect(): RectF = RectF(x, y, x + width, y + height)

    fun setPosition(newX: Float, newY: Float) {
        x = newX
        y = newY
        vx = 0f
        vy = 0f
        onGround = false
    }
}
