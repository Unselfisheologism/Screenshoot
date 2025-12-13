package com.example.screenrecorder.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.screenrecorder.R
import com.example.screenrecorder.core.AutoEditEngine
import com.example.screenrecorder.core.MotionBlurEffect
import com.example.screenrecorder.core.WatermarkOverlay
import com.example.screenrecorder.databinding.ActivityAdvancedEditorBinding
import com.example.screenrecorder.model.PresetManager
import com.example.screenrecorder.model.RecordingPreset

class AdvancedEditorActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdvancedEditorBinding
    private lateinit var autoEditEngine: AutoEditEngine
    private lateinit var motionBlurEffect: MotionBlurEffect
    private lateinit var watermarkOverlay: WatermarkOverlay
    private var filePath: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvancedEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Advanced Editor"
        
        autoEditEngine = AutoEditEngine()
        motionBlurEffect = MotionBlurEffect()
        watermarkOverlay = WatermarkOverlay()
        
        filePath = intent.getStringExtra("file_path")
        
        if (filePath == null) {
            Toast.makeText(this, "Invalid file", Toast.LENGTH_SHORT).show()
            onBackPressed()
            return
        }
        
        setupUI()
        analyzeSuggestions()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    private fun setupUI() {
        binding.btnApplyWatermark?.setOnClickListener {
            applyWatermark()
        }
        
        binding.btnApplyMotionBlur?.setOnClickListener {
            applyMotionBlur()
        }
        
        binding.btnAutoEdit?.setOnClickListener {
            performAutoEdit()
        }
        
        binding.btnViewPresets?.setOnClickListener {
            showPresets()
        }
    }
    
    private fun analyzeSuggestions() {
        filePath?.let { path ->
            val suggestions = autoEditEngine.suggestEnhancements(path)
            val chapters = autoEditEngine.generateChapters(path)
            
            val suggestionText = suggestions.joinToString("\n") { 
                "• ${it.title}: ${it.description}"
            }
            
            Toast.makeText(
                this,
                "Found ${suggestions.size} suggestions for improvement",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun applyWatermark() {
        Toast.makeText(this, "Watermark feature coming soon", Toast.LENGTH_SHORT).show()
    }
    
    private fun applyMotionBlur() {
        Toast.makeText(this, "Motion blur feature coming soon", Toast.LENGTH_SHORT).show()
    }
    
    private fun performAutoEdit() {
        filePath?.let { path ->
            Toast.makeText(this, "Auto-editing video...", Toast.LENGTH_SHORT).show()
            
            val silences = autoEditEngine.detectSilences(path)
            val chapters = autoEditEngine.generateChapters(path)
            
            Toast.makeText(
                this,
                "Found ${silences.size} silences and created ${chapters.size} chapters",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun showPresets() {
        val presets = PresetManager.getDefaultPresets()
        val presetNames = presets.map { it.name }.toTypedArray()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Recording Presets")
            .setItems(presetNames) { _, which ->
                val preset = presets[which]
                Toast.makeText(this, "Preset '${preset.name}' selected", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
