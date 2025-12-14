package com.example.screenrecorder.core

import android.graphics.Color

data class WatermarkConfig(
    val text: String,
    val opacity: Float = 0.7f,
    val positionX: Float = 0.5f,
    val positionY: Float = 0.5f,
    val textSize: Float = 24f,
    val textColor: Int = Color.WHITE,
    val animationEnabled: Boolean = false,
    val animationType: AnimationType = AnimationType.NONE
) {
    companion object {
        fun default(text: String) = WatermarkConfig(text = text)
    }
}

enum class AnimationType {
    NONE,
    FADE_IN,
    FADE_OUT,
    SLIDE_IN,
    SLIDE_OUT
}

class WatermarkOverlay {
    
    fun applyWatermark(
        inputPath: String,
        outputPath: String,
        watermarkConfig: WatermarkConfig,
        callback: (Boolean, String?) -> Unit
    ) {
        try {
            callback(true, null)
        } catch (e: Exception) {
            callback(false, e.message)
        }
    }
}
