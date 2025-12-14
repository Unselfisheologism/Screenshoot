package com.example.screenrecorder.util

import org.junit.Assert.*
import org.junit.Test

class FileUtilsTest {
    
    @Test
    fun getFormattedFileSize_withBytes() {
        val result = FileUtils.getFormattedFileSize(512)
        assertEquals("512 B", result)
    }
    
    @Test
    fun getFormattedFileSize_withKilobytes() {
        val result = FileUtils.getFormattedFileSize(1024 * 5)
        assertTrue(result.contains("KB"))
    }
    
    @Test
    fun getFormattedFileSize_withMegabytes() {
        val result = FileUtils.getFormattedFileSize(1024 * 1024 * 10)
        assertTrue(result.contains("MB"))
    }
    
    @Test
    fun getFormattedFileSize_withGigabytes() {
        val result = FileUtils.getFormattedFileSize(1024 * 1024 * 1024 * 2)
        assertTrue(result.contains("GB"))
    }
    
    @Test
    fun getFormattedFileSize_zeroBytes() {
        val result = FileUtils.getFormattedFileSize(0)
        assertEquals("0 B", result)
    }
}
