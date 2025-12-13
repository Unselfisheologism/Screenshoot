package com.example.screenrecorder.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.screenrecorder.R
import com.example.screenrecorder.databinding.ActivityGalleryBinding
import com.example.screenrecorder.model.RecordingFile
import com.example.screenrecorder.ui.adapter.RecordingAdapter
import com.example.screenrecorder.util.FileUtils

class GalleryActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter: RecordingAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Gallery"
        
        setupRecyclerView()
        loadRecordings()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
    
    private fun setupRecyclerView() {
        adapter = RecordingAdapter(mutableListOf()) { recording, action ->
            handleRecordingAction(recording, action)
        }
        
        binding.rvRecordings.apply {
            layoutManager = LinearLayoutManager(this@GalleryActivity)
            adapter = this@GalleryActivity.adapter
        }
    }
    
    private fun loadRecordings() {
        val recordings = FileUtils.getRecordingFiles(this)
        adapter.updateList(recordings)
        
        if (recordings.isEmpty()) {
            binding.tvEmptyMessage.text = "No recordings yet"
        }
    }
    
    private fun handleRecordingAction(recording: RecordingFile, action: String) {
        when (action) {
            "play" -> playRecording(recording)
            "edit" -> editRecording(recording)
            "share" -> shareRecording(recording)
            "delete" -> deleteRecording(recording)
        }
    }
    
    private fun playRecording(recording: RecordingFile) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(
                android.net.Uri.fromFile(java.io.File(recording.filePath)),
                "video/mp4"
            )
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No video player available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun editRecording(recording: RecordingFile) {
        val intent = Intent(this, EditorActivity::class.java).apply {
            putExtra("file_path", recording.filePath)
        }
        startActivity(intent)
    }
    
    private fun shareRecording(recording: RecordingFile) {
        val file = java.io.File(recording.filePath)
        val shareIntent = FileUtils.shareRecording(this, file)
        startActivity(Intent.createChooser(shareIntent, "Share Recording"))
    }
    
    private fun deleteRecording(recording: RecordingFile) {
        val file = java.io.File(recording.filePath)
        if (FileUtils.deleteRecording(file)) {
            Toast.makeText(this, "Recording deleted", Toast.LENGTH_SHORT).show()
            loadRecordings()
        } else {
            Toast.makeText(this, "Failed to delete recording", Toast.LENGTH_SHORT).show()
        }
    }
}
