package com.example.screenrecorder.core

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioPlaybackCaptureConfiguration
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.Build
import android.view.Surface
import com.example.screenrecorder.model.AudioOption
import com.example.screenrecorder.model.RecordingSettings
import com.example.screenrecorder.util.FileUtils
import java.io.File

class ScreenRecorder(
    private val context: Context,
    private val settings: RecordingSettings
) {
    
    private var mediaProjection: MediaProjection? = null
    private var mediaRecorder: MediaRecorder? = null
    private var audioRecord: AudioRecord? = null
    private var recordingFile: File? = null
    private var isRecording = false
    private var isPaused = false
    
    fun startRecording(mediaProjection: MediaProjection) {
        if (isRecording) return
        
        this.mediaProjection = mediaProjection
        recordingFile = FileUtils.createRecordingFile(context)
        
        try {
            mediaRecorder = createMediaRecorder()
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
        } catch (e: Exception) {
            e.printStackTrace()
            cleanup()
        }
    }
    
    fun stopRecording(): File? {
        if (!isRecording) return null
        
        return try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false
            isPaused = false
            recordingFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            cleanup()
        }
    }
    
    fun pauseRecording() {
        if (!isRecording || isPaused) return
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder?.pause()
                isPaused = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun resumeRecording() {
        if (!isRecording || !isPaused) return
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder?.resume()
                isPaused = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun isRecording(): Boolean = isRecording && !isPaused
    
    fun isPaused(): Boolean = isPaused
    
    private fun createMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            
            val quality = settings.videoQuality
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setVideoSize(quality.width, quality.height)
            setVideoFrameRate(quality.fps)
            setVideoBitRate(quality.bitrate)
            setAudioSamplingRate(44100)
            setAudioChannels(2)
            setAudioEncodingBitRate(128000)
            
            recordingFile?.let { file ->
                setOutputFile(file.absolutePath)
            }
            
            if (settings.maxDurationMinutes > 0) {
                setMaxDuration(settings.maxDurationMinutes * 60 * 1000)
            }
        }
    }
    
    private fun cleanup() {
        mediaProjection?.stop()
        mediaProjection = null
        mediaRecorder?.release()
        mediaRecorder = null
        audioRecord?.release()
        audioRecord = null
    }
}
