package com.example.mario

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var player = Player(100f, 800f)
    private var level = Level.getLevel(0)
    private var currentLevelIndex = 0

    private var cameraX = 0f

    private var leftPressed = false
    private var rightPressed = false

    private var running = true
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (running) {
                update()
                invalidate()
                handler.postDelayed(this, 16) // ~60 FPS
            }
        }
    }

    // UI кнопки (нарисованные)
    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#80FFFFFF")
        style = Paint.Style.FILL
    }
    private val buttonArrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFFFFF")
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        isFakeBoldText = true
    }

    private var leftBtnRect = android.graphics.RectF()
    private var rightBtnRect = android.graphics.RectF()
    private var jumpBtnRect = android.graphics.RectF()

    init {
        handler.post(updateRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        running = false
        handler.removeCallbacks(updateRunnable)
    }

    private fun update() {
        // Движение
        if (leftPressed) player.moveLeft()
        else if (rightPressed) player.moveRight()
        else player.stopMoving()

        player.update(width.toFloat(), height.toFloat() + 500f)

        // Коллизии с платформами
        val playerRect = player.getRect()
        player.onGround = false

        for (platform in level.platforms) {
            val platRect = platform.getRect()

            if (playerRect.intersects(platRect)) {
                // Падение сверху — стоим на платформе
                if (player.vy > 0 && playerRect.bottom - player.vy <= platRect.top + 10f) {
                    player.y = platRect.top - player.height
                    player.vy = 0f
                    player.onGround = true
                }
                // Удар головой снизу
                else if (player.vy < 0 && playerRect.top - player.vy >= platRect.bottom - 10f) {
                    player.y = platRect.bottom
                    player.vy = 0f
                }
                // Столкновение сбоку
                else {
                    if (playerRect.centerX() < platRect.centerX()) {
                        player.x = platRect.left - player.width
                    } else {
                        player.x = platRect.right
                    }
                    player.vx = 0f
                }
            }
        }

        // Проверка падения в яму
        if (player.y > height.toFloat() + 200f) {
            respawn()
        }

        // Проверка достижения финиша
        if (player.x + player.width >= level.goalX && player.y + player.height >= level.goalY - 50f) {
            nextLevel()
        }

        // Камера
        val targetCameraX = player.x - width / 3f
        cameraX += (targetCameraX - cameraX) * 0.1f
        if (cameraX < 0) cameraX = 0f
        val maxCam = level.worldWidth - width
        if (cameraX > maxCam) cameraX = maxCam
    }

    private fun respawn() {
        player.setPosition(level.startX, level.startY)
        cameraX = 0f
    }

    private fun nextLevel() {
        currentLevelIndex++
        if (currentLevelIndex > 2) currentLevelIndex = 0
        level = Level.getLevel(currentLevelIndex)
        player.setPosition(level.startX, level.startY)
        cameraX = 0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Фон — голубое небо
        canvas.drawColor(Color.parseColor("#5C94FC"))

        // Сохраняем и сдвигаем на камеру
        canvas.save()
        canvas.translate(-cameraX, 0f)

        // Платформы
        for (platform in level.platforms) {
            platform.draw(canvas)
        }

        // Персонаж
        player.draw(canvas)

        canvas.restore()

        // UI поверх всего
        drawUI(canvas)
    }

    private fun drawUI(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        // Кнопки
        val btnSize = 150f
        val margin = 40f
        val bottomY = h - btnSize - margin

        leftBtnRect.set(margin, bottomY, margin + btnSize, bottomY + btnSize)
        rightBtnRect.set(margin + btnSize + 20f, bottomY, margin + btnSize * 2 + 20f, bottomY + btnSize)
        jumpBtnRect.set(w - btnSize - margin, bottomY, w - margin, bottomY + btnSize)

        canvas.drawRoundRect(leftBtnRect, 20f, 20f, buttonPaint)
        canvas.drawRoundRect(rightBtnRect, 20f, 20f, buttonPaint)
        canvas.drawRoundRect(jumpBtnRect, 20f, 20f, buttonPaint)

        // Стрелки ◀ ▶
        val leftCenterX = leftBtnRect.centerX()
        val rightCenterX = rightBtnRect.centerX()
        val btnCenterY = leftBtnRect.centerY()
        val arrowSize = 40f

        // ◀
        val path1 = android.graphics.Path()
        path1.moveTo(leftCenterX - arrowSize / 2, btnCenterY)
        path1.lineTo(leftCenterX + arrowSize / 2, btnCenterY - arrowSize / 2)
        path1.lineTo(leftCenterX + arrowSize / 2, btnCenterY + arrowSize / 2)
        path1.close()
        canvas.drawPath(path1, buttonArrowPaint)

        // ▶
        val path2 = android.graphics.Path()
        path2.moveTo(rightCenterX + arrowSize / 2, btnCenterY)
        path2.lineTo(rightCenterX - arrowSize / 2, btnCenterY - arrowSize / 2)
        path2.lineTo(rightCenterX - arrowSize / 2, btnCenterY + arrowSize / 2)
        path2.close()
        canvas.drawPath(path2, buttonArrowPaint)

        // Прыжок — стрелка вверх
        val jumpCenterX = jumpBtnRect.centerX()
        val path3 = android.graphics.Path()
        path3.moveTo(jumpCenterX, btnCenterY - arrowSize / 2)
        path3.lineTo(jumpCenterX - arrowSize / 2, btnCenterY + arrowSize / 2)
        path3.lineTo(jumpCenterX + arrowSize / 2, btnCenterY + arrowSize / 2)
        path3.close()
        canvas.drawPath(path3, buttonArrowPaint)

        // Название уровня
        canvas.drawText(level.name, 40f, 60f, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (leftBtnRect.contains(x, y)) leftPressed = true
                if (rightBtnRect.contains(x, y)) rightPressed = true
                if (jumpBtnRect.contains(x, y)) player.jump()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                // Отпускаем все
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    leftPressed = false
                    rightPressed = false
                } else {
                    // Multi-touch: проверяем каждый pointer
                    leftPressed = false
                    rightPressed = false
                    for (i in 0 until event.pointerCount) {
                        val px = event.getX(i)
                        val py = event.getY(i)
                        if (leftBtnRect.contains(px, py)) leftPressed = true
                        if (rightBtnRect.contains(px, py)) rightPressed = true
                    }
                }
            }
        }
        return true
    }
}
