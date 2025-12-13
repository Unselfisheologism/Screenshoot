package com.example.screenrecorder.util

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.screenrecorder.model.RecordingFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {
    
    private const val RECORDINGS_FOLDER = "Recordings"
    private const val DATE_FORMAT = "yyyyMMdd_HHmmss"
    
    fun createRecordingFile(context: Context): File {
        val recordingsDir = getRecordingsDirectory(context)
        recordingsDir.mkdirs()
        
        val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date())
        val fileName = "recording_$timeStamp.mp4"
        
        return File(recordingsDir, fileName)
    }
    
    fun getRecordingsDirectory(context: Context): File {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(context.getExternalFilesDir(null), RECORDINGS_FOLDER)
        } else {
            File(
                context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                RECORDINGS_FOLDER
            )
        }
    }
    
    fun getRecordingFiles(context: Context): List<RecordingFile> {
        val recordingsDir = getRecordingsDirectory(context)
        val recordingFiles = mutableListOf<RecordingFile>()
        
        recordingsDir.listFiles()?.forEach { file ->
            if (file.isFile && file.extension == "mp4") {
                recordingFiles.add(
                    RecordingFile(
                        fileName = file.name,
                        filePath = file.absolutePath,
                        fileSize = file.length(),
                        duration = 0,
                        timestamp = file.lastModified()
                    )
                )
            }
        }
        
        return recordingFiles.sortedByDescending { it.timestamp }
    }
    
    fun deleteRecording(file: File): Boolean {
        return try {
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    fun addToGallery(context: Context, filePath: String) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath),
            arrayOf("video/mp4")
        ) { _, _ ->
            // Media scanning complete
        }
    }
    
    fun shareRecording(context: Context, file: File): Intent {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        
        return Intent(Intent.ACTION_SEND).apply {
            type = "video/mp4"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    
    fun getFormattedFileSize(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
            bytes >= 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
            bytes >= 1024 -> String.format("%.2f KB", bytes / 1024.0)
            else -> "$bytes B"
        }
    }
}
