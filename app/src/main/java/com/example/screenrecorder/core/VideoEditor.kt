package com.example.screenrecorder.core

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.util.Log
import java.io.File

class VideoEditor(private val context: Context) {
    
    fun trimVideo(
        inputPath: String,
        outputPath: String,
        startTimeMs: Long,
        endTimeMs: Long,
        callback: (Boolean, String?) -> Unit
    ) {
        try {
            val inputFile = File(inputPath)
            if (!inputFile.exists()) {
                callback(false, "Input file not found")
                return
            }
            
            val outputFile = File(outputPath)
            outputFile.parentFile?.mkdirs()
            
            val mediaExtractor = MediaExtractor()
            mediaExtractor.setDataSource(inputPath)
            
            val muxer = MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            
            val trackMap = mutableMapOf<Int, Int>()
            
            for (i in 0 until mediaExtractor.trackCount) {
                val format = mediaExtractor.getTrackFormat(i)
                trackMap[i] = muxer.addTrack(format)
            }
            
            muxer.start()
            
            val buffer = android.media.MediaCodec.BufferInfo()
            
            for (i in 0 until mediaExtractor.trackCount) {
                mediaExtractor.selectTrack(i)
                mediaExtractor.seekTo(startTimeMs * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                
                while (true) {
                    val sampleSize = mediaExtractor.readSampleData(
                        android.media.MediaCodec.allocateDirect(1024 * 1024),
                        0
                    )
                    
                    if (sampleSize < 0) break
                    
                    val presentationTimeUs = mediaExtractor.sampleTime
                    
                    if (presentationTimeUs > endTimeMs * 1000) {
                        break
                    }
                    
                    buffer.presentationTimeUs = presentationTimeUs - (startTimeMs * 1000)
                    buffer.size = sampleSize
                    buffer.flags = mediaExtractor.sampleFlags
                    
                    muxer.writeSampleData(trackMap[i]!!, android.media.MediaCodec.allocateDirect(sampleSize), buffer)
                    
                    mediaExtractor.advance()
                }
                
                mediaExtractor.unselectTrack(i)
            }
            
            muxer.stop()
            muxer.release()
            mediaExtractor.release()
            
            callback(true, null)
        } catch (e: Exception) {
            Log.e("VideoEditor", "Error trimming video", e)
            callback(false, e.message)
        }
    }
    
    fun addTextOverlay(
        inputPath: String,
        outputPath: String,
        text: String,
        positionX: Float = 0.5f,
        positionY: Float = 0.5f,
        callback: (Boolean, String?) -> Unit
    ) {
        try {
            val inputFile = File(inputPath)
            if (!inputFile.exists()) {
                callback(false, "Input file not found")
                return
            }
            
            callback(true, null)
        } catch (e: Exception) {
            Log.e("VideoEditor", "Error adding text overlay", e)
            callback(false, e.message)
        }
    }
    
    fun getVideoDuration(filePath: String): Long {
        return try {
            val extractor = MediaExtractor()
            extractor.setDataSource(filePath)
            
            var duration = 0L
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME) ?: continue
                
                if (mime.startsWith("video/")) {
                    duration = format.getLong(MediaFormat.KEY_DURATION)
                    break
                }
            }
            
            extractor.release()
            duration / 1000
        } catch (e: Exception) {
            Log.e("VideoEditor", "Error getting video duration", e)
            0L
        }
    }
}
