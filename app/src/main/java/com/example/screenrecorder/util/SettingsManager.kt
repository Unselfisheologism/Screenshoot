package com.example.screenrecorder.util

import android.content.Context
import android.content.SharedPreferences
import com.example.screenrecorder.model.AudioOption
import com.example.screenrecorder.model.RecordingSettings
import com.example.screenrecorder.model.VideoQuality

class SettingsManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    fun getSettings(): RecordingSettings {
        return RecordingSettings(
            videoQuality = VideoQuality.valueOf(
                prefs.getString(KEY_VIDEO_QUALITY, VideoQuality.MEDIUM.name) ?: VideoQuality.MEDIUM.name
            ),
            audioOption = AudioOption.valueOf(
                prefs.getString(KEY_AUDIO_OPTION, AudioOption.BOTH.name) ?: AudioOption.BOTH.name
            ),
            maxDurationMinutes = prefs.getInt(KEY_MAX_DURATION, 0),
            customResolution = prefs.getInt(KEY_CUSTOM_RESOLUTION, 720),
            customFps = prefs.getInt(KEY_CUSTOM_FPS, 30),
            customBitrate = prefs.getInt(KEY_CUSTOM_BITRATE, 5000000)
        )
    }
    
    fun saveSettings(settings: RecordingSettings) {
        prefs.edit().apply {
            putString(KEY_VIDEO_QUALITY, settings.videoQuality.name)
            putString(KEY_AUDIO_OPTION, settings.audioOption.name)
            putInt(KEY_MAX_DURATION, settings.maxDurationMinutes)
            putInt(KEY_CUSTOM_RESOLUTION, settings.customResolution)
            putInt(KEY_CUSTOM_FPS, settings.customFps)
            putInt(KEY_CUSTOM_BITRATE, settings.customBitrate)
            apply()
        }
    }
    
    fun setVideoQuality(quality: VideoQuality) {
        prefs.edit().putString(KEY_VIDEO_QUALITY, quality.name).apply()
    }
    
    fun setAudioOption(option: AudioOption) {
        prefs.edit().putString(KEY_AUDIO_OPTION, option.name).apply()
    }
    
    fun setMaxDuration(minutes: Int) {
        prefs.edit().putInt(KEY_MAX_DURATION, minutes).apply()
    }
    
    companion object {
        private const val PREFS_NAME = "screen_recorder_prefs"
        private const val KEY_VIDEO_QUALITY = "video_quality"
        private const val KEY_AUDIO_OPTION = "audio_option"
        private const val KEY_MAX_DURATION = "max_duration"
        private const val KEY_CUSTOM_RESOLUTION = "custom_resolution"
        private const val KEY_CUSTOM_FPS = "custom_fps"
        private const val KEY_CUSTOM_BITRATE = "custom_bitrate"
    }
}
