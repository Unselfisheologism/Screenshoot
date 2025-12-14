package com.example.screenrecorder.model

data class RecordingPreset(
    val name: String,
    val description: String,
    val settings: RecordingSettings,
    val isDefault: Boolean = false
)

object PresetManager {
    
    fun getDefaultPresets(): List<RecordingPreset> {
        return listOf(
            RecordingPreset(
                name = "Tutorial Mode",
                description = "Optimized for tutorials with zooms on interactions",
                settings = RecordingSettings(
                    videoQuality = VideoQuality.HIGH,
                    audioOption = AudioOption.BOTH,
                    maxDurationMinutes = 60
                ),
                isDefault = true
            ),
            RecordingPreset(
                name = "Gaming Mode",
                description = "High FPS for smooth gaming footage",
                settings = RecordingSettings(
                    videoQuality = VideoQuality.HIGH,
                    audioOption = AudioOption.MICROPHONE_AUDIO,
                    maxDurationMinutes = 120
                )
            ),
            RecordingPreset(
                name = "Presentation Mode",
                description = "Optimized for presentations and demos",
                settings = RecordingSettings(
                    videoQuality = VideoQuality.MEDIUM,
                    audioOption = AudioOption.BOTH,
                    maxDurationMinutes = 90
                )
            ),
            RecordingPreset(
                name = "Quick Share",
                description = "Low quality for quick sharing",
                settings = RecordingSettings(
                    videoQuality = VideoQuality.LOW,
                    audioOption = AudioOption.NO_AUDIO,
                    maxDurationMinutes = 30
                )
            )
        )
    }
    
    fun getPresetByName(name: String): RecordingPreset? {
        return getDefaultPresets().find { it.name == name }
    }
}
