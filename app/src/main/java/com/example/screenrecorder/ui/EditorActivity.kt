package com.example.screenrecorder.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.screenrecorder.R
import com.example.screenrecorder.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEditorBinding
    private var filePath: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Edit Recording"
        
        filePath = intent.getStringExtra("file_path")
        
        if (filePath == null) {
            Toast.makeText(this, "Invalid file", Toast.LENGTH_SHORT).show()
            onBackPressed()
            return
        }
        
        setupUI()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    private fun setupUI() {
        binding.btnTrim.setOnClickListener {
            // Implement trim functionality
            Toast.makeText(this, "Trim feature coming soon", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnAddText.setOnClickListener {
            // Implement text overlay functionality
            Toast.makeText(this, "Text overlay feature coming soon", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnSaveEdit.setOnClickListener {
            // Save edited video
            Toast.makeText(this, "Saving edited video", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}
