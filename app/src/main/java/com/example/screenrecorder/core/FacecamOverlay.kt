package com.example.screenrecorder.core

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceTexture

data class FacecamConfig(
    val enabled: Boolean = false,
    val width: Int = 320,
    val height: Int = 240,
    val positionX: Float = 0.85f,
    val positionY: Float = 0.85f,
    val opacity: Float = 1f,
    val borderColor: Int = android.graphics.Color.WHITE,
    val borderWidth: Float = 2f
)

class FacecamOverlay(
    private val context: Context,
    private val config: FacecamConfig = FacecamConfig()
) {
    
    private var camera: Camera? = null
    private var surfaceTexture: SurfaceTexture? = null
    
    fun startFacecam(callback: (Boolean, String?) -> Unit) {
        if (!config.enabled) {
            callback(true, null)
            return
        }
        
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            val parameters = camera?.parameters
            parameters?.setPreviewSize(config.width, config.height)
            camera?.parameters = parameters
            
            callback(true, null)
        } catch (e: Exception) {
            callback(false, e.message)
        }
    }
    
    fun stopFacecam() {
        try {
            camera?.stopPreview()
            camera?.release()
            camera = null
            surfaceTexture?.release()
            surfaceTexture = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun getOverlayPosition(): Pair<Float, Float> {
        return Pair(config.positionX, config.positionY)
    }
    
    fun getOverlaySize(): Pair<Int, Int> {
        return Pair(config.width, config.height)
    }
}
