package com.example.screenrecorder.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.screenrecorder.R
import com.example.screenrecorder.core.VideoEditor
import com.example.screenrecorder.databinding.ActivityEditorBinding
import com.example.screenrecorder.util.FileUtils
import java.io.File

class EditorActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEditorBinding
    private var filePath: String? = null
    private lateinit var videoEditor: VideoEditor
    private var isTrimming = false
    private var isEditing = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Edit Recording"
        
        videoEditor = VideoEditor(this)
        filePath = intent.getStringExtra("file_path")
        
        if (filePath == null) {
            Toast.makeText(this, "Invalid file", Toast.LENGTH_SHORT).show()
            onBackPressed()
            return
        }
        
        setupUI()
        loadVideoDuration()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    private fun setupUI() {
        binding.btnTrim.setOnClickListener {
            if (!isTrimming) {
                isTrimming = true
                binding.btnTrim.text = "End Trim"
                binding.seekBarTrim.isEnabled = true
                Toast.makeText(this, "Select start and end points using the seek bar", Toast.LENGTH_LONG).show()
            } else {
                isTrimming = false
                binding.btnTrim.text = "Trim"
                performTrim()
            }
        }
        
        binding.btnAddText.setOnClickListener {
            Toast.makeText(this, "Text overlay coming soon", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnSaveEdit.setOnClickListener {
            if (isEditing) {
                Toast.makeText(this, "Please wait, editing in progress", Toast.LENGTH_SHORT).show()
            } else {
                onBackPressed()
            }
        }
    }
    
    private fun loadVideoDuration() {
        filePath?.let {
            val durationMs = videoEditor.getVideoDuration(it)
            binding.seekBarTrim.max = durationMs.toInt()
            
            val durationText = formatDuration(durationMs)
            binding.tvVideoInfo?.text = "Duration: $durationText"
        }
    }
    
    private fun performTrim() {
        if (isEditing) {
            Toast.makeText(this, "Editing already in progress", Toast.LENGTH_SHORT).show()
            return
        }
        
        val startTimeMs = binding.seekBarTrim.progress.toLong()
        val maxTime = binding.seekBarTrim.max.toLong()
        
        if (startTimeMs >= maxTime) {
            Toast.makeText(this, "Invalid trim range", Toast.LENGTH_SHORT).show()
            return
        }
        
        isEditing = true
        binding.btnSaveEdit.isEnabled = false
        Toast.makeText(this, "Trimming video...", Toast.LENGTH_SHORT).show()
        
        val originalFile = File(filePath!!)
        val outputFile = File(originalFile.parentFile, "trimmed_${System.currentTimeMillis()}.mp4")
        
        videoEditor.trimVideo(
            filePath!!,
            outputFile.absolutePath,
            startTimeMs,
            maxTime
        ) { success, error ->
            runOnUiThread {
                isEditing = false
                binding.btnSaveEdit.isEnabled = true
                
                if (success) {
                    Toast.makeText(this, "Trim successful", Toast.LENGTH_SHORT).show()
                    
                    val originalPath = originalFile.absolutePath
                    originalFile.delete()
                    outputFile.renameTo(File(originalPath))
                    
                    FileUtils.addToGallery(this, originalPath)
                    onBackPressed()
                } else {
                    Toast.makeText(this, "Trim failed: $error", Toast.LENGTH_SHORT).show()
                    outputFile.delete()
                }
            }
        }
    }
    
    private fun formatDuration(durationMs: Long): String {
        val seconds = durationMs / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}

