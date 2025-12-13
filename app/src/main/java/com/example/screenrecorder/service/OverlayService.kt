package com.example.screenrecorder.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import com.example.screenrecorder.R

class OverlayService : Service() {
    
    private val binder = OverlayBinder()
    private var overlayView: LinearLayout? = null
    private var windowManager: WindowManager? = null
    private var chronometer: Chronometer? = null
    private var overlayCallback: OverlayCallback? = null
    
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder = binder
    
    override fun onDestroy() {
        removeOverlay()
        super.onDestroy()
    }
    
    fun showOverlay() {
        if (overlayView != null) return
        
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_recording, null) as LinearLayout
        
        chronometer = overlayView?.findViewById(R.id.chronometer)
        chronometer?.setOnChronometerTickListener { chrono ->
            overlayCallback?.onTimeUpdate(chrono.base)
        }
        
        val pauseResumeBtn = overlayView?.findViewById<Button>(R.id.btn_pause_resume)
        val stopBtn = overlayView?.findViewById<Button>(R.id.btn_stop)
        
        pauseResumeBtn?.setOnClickListener {
            overlayCallback?.onPauseResumeClicked()
        }
        
        stopBtn?.setOnClickListener {
            overlayCallback?.onStopClicked()
        }
        
        val params = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            }
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 100
        }
        
        windowManager?.addView(overlayView, params)
    }
    
    fun removeOverlay() {
        overlayView?.let {
            try {
                windowManager?.removeView(it)
                overlayView = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun updateTimer(elapsedTime: Long) {
        chronometer?.apply {
            base = elapsedTime
        }
    }
    
    fun setOverlayCallback(callback: OverlayCallback) {
        overlayCallback = callback
    }
    
    inner class OverlayBinder : Binder() {
        fun getService(): OverlayService = this@OverlayService
    }
    
    interface OverlayCallback {
        fun onPauseResumeClicked()
        fun onStopClicked()
        fun onTimeUpdate(elapsedTime: Long)
    }
    
    companion object {
        const val ACTION_START_OVERLAY = "com.example.screenrecorder.START_OVERLAY"
        const val ACTION_STOP_OVERLAY = "com.example.screenrecorder.STOP_OVERLAY"
    }
}
