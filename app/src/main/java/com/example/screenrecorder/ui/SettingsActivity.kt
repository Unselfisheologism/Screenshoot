package com.example.screenrecorder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.screenrecorder.R
import com.example.screenrecorder.databinding.ActivitySettingsBinding
import com.example.screenrecorder.model.VideoQuality
import com.example.screenrecorder.util.SettingsManager

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsManager: SettingsManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        settingsManager = SettingsManager(this)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.settings)
        
        setupUI()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    private fun setupUI() {
        val currentSettings = settingsManager.getSettings()
        
        binding.spinnerQuality.setSelection(currentSettings.videoQuality.ordinal)
        binding.spinnerAudio.setSelection(currentSettings.audioOption.ordinal)
        binding.etMaxDuration.setText(currentSettings.maxDurationMinutes.toString())
        
        binding.btnSaveSettings.setOnClickListener {
            saveSettings()
        }
    }
    
    private fun saveSettings() {
        val selectedQuality = VideoQuality.values()[binding.spinnerQuality.selectedItemPosition]
        val maxDuration = binding.etMaxDuration.text.toString().toIntOrNull() ?: 0
        
        val currentSettings = settingsManager.getSettings()
        val newSettings = currentSettings.copy(
            videoQuality = selectedQuality,
            maxDurationMinutes = maxDuration
        )
        
        settingsManager.saveSettings(newSettings)
        
        onBackPressed()
    }
}
