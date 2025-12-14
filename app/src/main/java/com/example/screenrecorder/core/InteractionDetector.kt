package com.example.screenrecorder.core

import android.view.MotionEvent
import kotlin.math.abs

data class TouchInteraction(
    val x: Float,
    val y: Float,
    val type: InteractionType,
    val timestamp: Long
)

enum class InteractionType {
    TAP,
    SWIPE_UP,
    SWIPE_DOWN,
    SWIPE_LEFT,
    SWIPE_RIGHT,
    LONG_PRESS,
    PINCH
}

class InteractionDetector {
    
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var lastTouchTime = 0L
    private var touchDownTime = 0L
    
    fun detectInteraction(event: MotionEvent): TouchInteraction? {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
                touchDownTime = System.currentTimeMillis()
                null
            }
            MotionEvent.ACTION_UP -> {
                val currentTime = System.currentTimeMillis()
                val duration = currentTime - touchDownTime
                
                when {
                    duration > 500 -> {
                        TouchInteraction(
                            event.x,
                            event.y,
                            InteractionType.LONG_PRESS,
                            currentTime
                        )
                    }
                    else -> {
                        TouchInteraction(
                            event.x,
                            event.y,
                            InteractionType.TAP,
                            currentTime
                        )
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastTouchX
                val dy = event.y - lastTouchY
                val distance = kotlin.math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                
                if (distance > 50) {
                    val interactionType = when {
                        abs(dx) > abs(dy) -> {
                            if (dx > 0) InteractionType.SWIPE_RIGHT else InteractionType.SWIPE_LEFT
                        }
                        else -> {
                            if (dy > 0) InteractionType.SWIPE_DOWN else InteractionType.SWIPE_UP
                        }
                    }
                    
                    lastTouchX = event.x
                    lastTouchY = event.y
                    lastTouchTime = System.currentTimeMillis()
                    
                    TouchInteraction(
                        event.x,
                        event.y,
                        interactionType,
                        lastTouchTime
                    )
                } else {
                    null
                }
            }
            else -> null
        }
    }
    
    fun calculateZoomArea(interaction: TouchInteraction, screenWidth: Int, screenHeight: Int): ZoomArea {
        val zoomRadius = 200f
        val centerX = interaction.x.coerceIn(zoomRadius, screenWidth - zoomRadius)
        val centerY = interaction.y.coerceIn(zoomRadius, screenHeight - zoomRadius)
        
        return ZoomArea(
            centerX = centerX,
            centerY = centerY,
            radius = zoomRadius,
            zoomLevel = 1.5f,
            duration = 500
        )
    }
}

data class ZoomArea(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
    val zoomLevel: Float,
    val duration: Int
)
