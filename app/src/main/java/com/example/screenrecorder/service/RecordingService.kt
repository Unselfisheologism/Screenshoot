package com.example.screenrecorder.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.screenrecorder.R
import com.example.screenrecorder.core.ScreenRecorder
import com.example.screenrecorder.model.RecordingSettings
import com.example.screenrecorder.ui.MainActivity
import com.example.screenrecorder.util.FileUtils
import com.example.screenrecorder.util.SettingsManager

class RecordingService : Service() {
    
    private val binder = RecordingBinder()
    private var screenRecorder: ScreenRecorder? = null
    private var settings: RecordingSettings? = null
    private var startTime = 0L
    private var pausedTime = 0L
    
    private var recordingCallback: RecordingCallback? = null
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        settings = SettingsManager(this).getSettings()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder = binder
    
    override fun onDestroy() {
        stopRecording()
        super.onDestroy()
    }
    
    fun startRecording(mediaProjection: MediaProjection) {
        try {
            screenRecorder = ScreenRecorder(this, settings ?: SettingsManager(this).getSettings())
            screenRecorder?.startRecording(mediaProjection)
            startTime = SystemClock.elapsedRealtime()
            startForeground(NOTIFICATION_ID, createNotification())
            recordingCallback?.onRecordingStarted()
        } catch (e: Exception) {
            e.printStackTrace()
            recordingCallback?.onRecordingError(e.message ?: "Unknown error")
        }
    }
    
    fun stopRecording() {
        try {
            val recordingFile = screenRecorder?.stopRecording()
            if (recordingFile != null) {
                FileUtils.addToGallery(this, recordingFile.absolutePath)
                recordingCallback?.onRecordingStopped(recordingFile.absolutePath)
            } else {
                recordingCallback?.onRecordingError("Failed to save recording")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            recordingCallback?.onRecordingError(e.message ?: "Unknown error")
        } finally {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }
    
    fun pauseRecording() {
        try {
            screenRecorder?.pauseRecording()
            pausedTime = SystemClock.elapsedRealtime()
            recordingCallback?.onRecordingPaused()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun resumeRecording() {
        try {
            screenRecorder?.resumeRecording()
            startTime += (SystemClock.elapsedRealtime() - pausedTime)
            recordingCallback?.onRecordingResumed()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun getElapsedTime(): Long {
        return if (screenRecorder?.isPaused() == true) {
            pausedTime - startTime
        } else {
            SystemClock.elapsedRealtime() - startTime
        }
    }
    
    fun isRecording(): Boolean = screenRecorder?.isRecording() ?: false
    
    fun isPaused(): Boolean = screenRecorder?.isPaused() ?: false
    
    fun setRecordingCallback(callback: RecordingCallback) {
        recordingCallback = callback
    }
    
    private fun createNotification(): Notification {
        val stopIntent = Intent(this, RecordingService::class.java).apply {
            action = ACTION_STOP_RECORDING
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val contentIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.recording_active))
            .setContentText(getString(R.string.tap_to_stop))
            .setSmallIcon(R.drawable.ic_recorder)
            .setContentIntent(contentPendingIntent)
            .addAction(0, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Screen Recording",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for screen recording"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    
    inner class RecordingBinder : Binder() {
        fun getService(): RecordingService = this@RecordingService
    }
    
    interface RecordingCallback {
        fun onRecordingStarted()
        fun onRecordingStopped(filePath: String)
        fun onRecordingPaused()
        fun onRecordingResumed()
        fun onRecordingError(error: String)
    }
    
    companion object {
        const val CHANNEL_ID = "recording_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_STOP_RECORDING = "com.example.screenrecorder.STOP_RECORDING"
    }
}
