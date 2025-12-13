package com.example.screenrecorder.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.screenrecorder.R
import com.example.screenrecorder.databinding.ActivityMainBinding
import com.example.screenrecorder.service.RecordingService

class MainActivity : AppCompatActivity(), RecordingService.RecordingCallback {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var recordingService: RecordingService? = null
    private var isServiceBound = false
    
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as RecordingService.RecordingBinder
            recordingService = binder.getService()
            recordingService?.setRecordingCallback(this@MainActivity)
            isServiceBound = true
            updateUI()
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
            recordingService = null
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        
        setupUI()
        checkPermissions()
    }
    
    override fun onStart() {
        super.onStart()
        Intent(this, RecordingService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }
    
    override fun onStop() {
        super.onStop()
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }
    
    private fun setupUI() {
        binding.btnStartRecording.setOnClickListener {
            requestScreenCapture()
        }
        
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        binding.btnGallery.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
    }
    
    private fun updateUI() {
        if (recordingService?.isRecording() == true) {
            binding.btnStartRecording.text = getString(R.string.stop_recording)
            binding.tvRecordingStatus.text = getString(R.string.recording_active)
        } else {
            binding.btnStartRecording.text = getString(R.string.start_recording)
            binding.tvRecordingStatus.text = ""
        }
    }
    
    private fun checkPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.RECORD_AUDIO
        )
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PermissionChecker.PERMISSION_GRANTED
        }
        
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }
    
    private fun requestScreenCapture() {
        if (recordingService?.isRecording() == true) {
            recordingService?.stopRecording()
        } else {
            val intent = mediaProjectionManager.createScreenCaptureIntent()
            startActivityForResult(intent, REQUEST_MEDIA_PROJECTION)
        }
    }
    
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_MEDIA_PROJECTION && resultCode == RESULT_OK) {
            val mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data!!)
            if (mediaProjection != null) {
                recordingService?.startRecording(mediaProjection)
            }
        }
    }
    
    override fun onRecordingStarted() {
        runOnUiThread {
            updateUI()
            Toast.makeText(this, R.string.recording_active, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onRecordingStopped(filePath: String) {
        runOnUiThread {
            updateUI()
            Toast.makeText(this, R.string.recording_saved, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onRecordingPaused() {
        runOnUiThread {
            Toast.makeText(this, R.string.pause_recording, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onRecordingResumed() {
        runOnUiThread {
            Toast.makeText(this, R.string.resume_recording, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onRecordingError(error: String) {
        runOnUiThread {
            updateUI()
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        }
    }
    
    companion object {
        private const val REQUEST_MEDIA_PROJECTION = 1
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
