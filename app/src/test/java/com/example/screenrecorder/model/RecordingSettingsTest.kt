package com.example.screenrecorder.model

import org.junit.Assert.*
import org.junit.Test

class RecordingSettingsTest {
    
    @Test
    fun recordingSettings_defaultValues() {
        val settings = RecordingSettings()
        assertEquals(VideoQuality.MEDIUM, settings.videoQuality)
        assertEquals(AudioOption.BOTH, settings.audioOption)
        assertEquals(0, settings.maxDurationMinutes)
        assertEquals(720, settings.customResolution)
        assertEquals(30, settings.customFps)
        assertEquals(5000000, settings.customBitrate)
    }
    
    @Test
    fun recordingSettings_customValues() {
        val settings = RecordingSettings(
            videoQuality = VideoQuality.HIGH,
            audioOption = AudioOption.SYSTEM_AUDIO,
            maxDurationMinutes = 30,
            customResolution = 1080,
            customFps = 60,
            customBitrate = 10000000
        )
        
        assertEquals(VideoQuality.HIGH, settings.videoQuality)
        assertEquals(AudioOption.SYSTEM_AUDIO, settings.audioOption)
        assertEquals(30, settings.maxDurationMinutes)
        assertEquals(1080, settings.customResolution)
        assertEquals(60, settings.customFps)
        assertEquals(10000000, settings.customBitrate)
    }
    
    @Test
    fun recordingSettings_copy() {
        val original = RecordingSettings()
        val copy = original.copy(
            videoQuality = VideoQuality.HIGH,
            maxDurationMinutes = 15
        )
        
        assertEquals(VideoQuality.MEDIUM, original.videoQuality)
        assertEquals(VideoQuality.HIGH, copy.videoQuality)
        assertEquals(0, original.maxDurationMinutes)
        assertEquals(15, copy.maxDurationMinutes)
    }
}
