package com.example.screenrecorder.model

data class RecordingSettings(
    val videoQuality: VideoQuality = VideoQuality.MEDIUM,
    val audioOption: AudioOption = AudioOption.BOTH,
    val maxDurationMinutes: Int = 0,
    val customResolution: Int = 720,
    val customFps: Int = 30,
    val customBitrate: Int = 5000000
)

enum class VideoQuality(val width: Int, val height: Int, val fps: Int, val bitrate: Int) {
    LOW(480, 854, 30, 2000000),
    MEDIUM(720, 1280, 30, 5000000),
    HIGH(1080, 1920, 60, 10000000)
}

enum class AudioOption {
    SYSTEM_AUDIO,
    MICROPHONE_AUDIO,
    BOTH,
    NO_AUDIO
}

data class RecordingFile(
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val duration: Long,
    val timestamp: Long
)
