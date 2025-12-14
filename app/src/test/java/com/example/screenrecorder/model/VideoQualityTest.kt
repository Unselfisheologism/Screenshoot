package com.example.screenrecorder.model

import org.junit.Assert.*
import org.junit.Test

class VideoQualityTest {
    
    @Test
    fun videoQuality_lowSettings() {
        val low = VideoQuality.LOW
        assertEquals(480, low.width)
        assertEquals(854, low.height)
        assertEquals(30, low.fps)
        assertEquals(2000000, low.bitrate)
    }
    
    @Test
    fun videoQuality_mediumSettings() {
        val medium = VideoQuality.MEDIUM
        assertEquals(720, medium.width)
        assertEquals(1280, medium.height)
        assertEquals(30, medium.fps)
        assertEquals(5000000, medium.bitrate)
    }
    
    @Test
    fun videoQuality_highSettings() {
        val high = VideoQuality.HIGH
        assertEquals(1080, high.width)
        assertEquals(1920, high.height)
        assertEquals(60, high.fps)
        assertEquals(10000000, high.bitrate)
    }
    
    @Test
    fun audioOption_values() {
        val options = AudioOption.values()
        assertEquals(4, options.size)
        assertTrue(options.contains(AudioOption.SYSTEM_AUDIO))
        assertTrue(options.contains(AudioOption.MICROPHONE_AUDIO))
        assertTrue(options.contains(AudioOption.BOTH))
        assertTrue(options.contains(AudioOption.NO_AUDIO))
    }
}
