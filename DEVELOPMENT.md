# Development Guide

This document provides guidance for developers working on the Screen Recorder app.

## Getting Started

### Prerequisites
- Android Studio 2023.1 or higher
- JDK 17 or higher
- Android SDK with API 34
- Gradle 8.4

### Clone and Setup
```bash
git clone <repository-url>
cd ScreenRecorder
git checkout feat-android-screen-recorder-core-mediaprojection-mediarecorder
./gradlew clean build
```

### Run Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires emulator or device)
./gradlew connectedAndroidTest

# Both
./gradlew testDebug
```

## Code Style

### Naming Conventions
- **Classes**: PascalCase (e.g., `ScreenRecorder`)
- **Functions**: camelCase (e.g., `startRecording()`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `NOTIFICATION_ID`)
- **Variables**: camelCase (e.g., `isRecording`)
- **Layout files**: snake_case with activity prefix (e.g., `activity_main.xml`)
- **Drawable files**: snake_case with type prefix (e.g., `ic_recorder.xml`)

### Kotlin Style
- Use val instead of var when possible
- Use extension functions for utility operations
- Use data classes for model objects
- Use scope functions (let, apply, run) appropriately
- Avoid null with non-null assertions (!!)

### Comments
- Add comments for complex logic
- Use KDoc for public functions: `/** ... */`
- Keep comments up-to-date with code changes

## Architecture

### Service-Based Recording
```
User Action
    ↓
MainActivity
    ↓
RecordingService (Foreground)
    ↓
ScreenRecorder (Core Logic)
    ↓
MediaProjection + MediaRecorder
```

### Editing Pipeline
```
Original Recording
    ↓
VideoEditor.trimVideo()
    ↓
MediaExtractor (Read)
    ↓
MediaMuxer (Write)
    ↓
Edited Recording
```

## Adding New Features

### Adding a Video Quality Preset
1. Update `VideoQuality` enum in `RecordingSettings.kt`
2. Add appropriate values for width, height, fps, bitrate
3. Update quality spinner in `activity_settings.xml` if needed
4. Test in SettingsActivity

### Adding a Recording Preset
1. Add to `PresetManager.getDefaultPresets()` in `RecordingPreset.kt`
2. Define `RecordingSettings` for the preset
3. Add to preset selection dialog in `AdvancedEditorActivity`

### Adding a New Editing Feature
1. Create new class in `core/` package
2. Implement async callback pattern for UI responsiveness
3. Add UI component in appropriate Activity
4. Test with sample videos
5. Document in README.md

### Adding a New Permission
1. Add `<uses-permission>` in `AndroidManifest.xml`
2. Update `checkPermissions()` in MainActivity if runtime permission needed
3. Handle permission denial gracefully
4. Test on API 23+ (runtime permissions)

## Testing

### Unit Test Structure
```kotlin
class MyFeatureTest {
    @Test
    fun featureName_condition_expectedResult() {
        // Arrange
        val input = ...
        
        // Act
        val result = myFunction(input)
        
        // Assert
        assertEquals(expected, result)
    }
}
```

### Instrumented Test Structure
```kotlin
@RunWith(AndroidJUnit4::class)
class MyActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MyActivity::class.java)
    
    @Test
    fun testActivityStartup() {
        // Use activityRule.scenario to test UI
    }
}
```

### What to Test
- File operations (creation, deletion, sharing)
- Settings persistence
- Model data transformations
- Permission handling
- Error cases and edge cases

## Debugging

### Logcat Filters
```bash
# Only app logs
adb logcat com.example.screenrecorder:V *:S

# Recording-related
adb logcat | grep -E "(ScreenRecorder|RecordingService)"

# Video Editor
adb logcat | grep VideoEditor
```

### Common Issues

#### Recording Won't Start
1. Check MediaProjection permission is granted
2. Verify foreground service permission
3. Ensure disk space > 100MB
4. Check battery level > 10%

#### Video File Corrupted
1. Verify MediaRecorder.stop() called
2. Check file isn't deleted before completion
3. Ensure sufficient disk space
4. Review MediaRecorder error callback

#### Permission Denied
1. Check AndroidManifest.xml includes permission
2. Request runtime permissions for API 23+
3. Verify user granted in settings
4. Test on different API levels

## Performance Tips

### Recording Optimization
- Use appropriate quality for device capabilities
- Pause recording if device gets too hot
- Monitor battery during long recordings
- Limit metadata writes

### Storage Optimization
- Delete old recordings periodically
- Use lower quality for storage-constrained devices
- Implement compression for sharing

### Memory Management
- Release MediaProjection when done
- Clear video cache files
- Use weak references for callbacks
- Monitor heap size during editing

## CI/CD

### GitHub Actions Workflow
Located: `.github/workflows/build-and-deploy.yml`

Runs on:
- Push to main branch
- Pull requests to main

Stages:
1. Checkout code
2. Setup JDK 17
3. Grant gradlew execute permission
4. Run unit tests
5. Run instrumented tests (emulator API 29)
6. Build debug APK
7. Build release APK
8. Sign release APK
9. Upload artifact
10. Create GitHub Release (main branch only)

### Secrets Required
- `SIGNING_KEY`: Base64 encoded keystore
- `ALIAS`: Keystore alias
- `KEY_STORE_PASSWORD`: Keystore password
- `KEY_PASSWORD`: Key password

### Manual Build
```bash
# Debug
./gradlew assembleDebug

# Release
./gradlew assembleRelease

# Sign (after build)
jarsigner -verbose -sigalg SHA256withRSA \
    -digestalg SHA-256 \
    -keystore keystore.jks \
    app/build/outputs/apk/release/app-release-unsigned.apk \
    your_alias
```

## Dependencies

### Core
- androidx.appcompat:appcompat:1.6.1
- androidx.core:core-ktx:1.12.0
- com.google.android.material:material:1.10.0

### UI
- androidx.constraintlayout:constraintlayout:2.1.4
- androidx.fragment:fragment-ktx:1.6.2

### Storage
- androidx.documentfile:documentfile:1.0.1

### Testing
- junit:junit:4.13.2
- androidx.test.ext:junit:1.1.5
- androidx.test.espresso:espresso-core:3.5.1

### Adding Dependencies
1. Add to `app/build.gradle` in appropriate category
2. Run `./gradlew build` to sync
3. Test on minimum supported API level

## Release Checklist

- [ ] Code review completed
- [ ] All tests passing
- [ ] No lint warnings
- [ ] ProGuard rules updated if needed
- [ ] Version number incremented
- [ ] CHANGELOG.md updated
- [ ] README.md reviewed
- [ ] Tested on devices (API 21, API 30, latest)
- [ ] Battery impact verified
- [ ] Storage impact verified
- [ ] Crash testing completed
- [ ] Permission testing on API 23+
- [ ] GitHub Release notes prepared

## Useful Commands

```bash
# Clean build
./gradlew clean build

# Run specific test
./gradlew test --tests "com.example.screenrecorder.util.FileUtilsTest.getFormattedFileSize*"

# Check dependencies
./gradlew dependencies

# Find ProGuard issues
./gradlew assembleRelease --debug

# Build and install
./gradlew installDebug

# Run on connected device
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Get current Git branch
git rev-parse --abbrev-ref HEAD

# Check status
git status
```

## Resources

### Official Documentation
- [Android Developers](https://developer.android.com/)
- [MediaProjection API](https://developer.android.com/reference/android/media/projection/MediaProjection)
- [MediaRecorder](https://developer.android.com/reference/android/media/MediaRecorder)
- [MediaExtractor](https://developer.android.com/reference/android/media/MediaExtractor)
- [Scoped Storage](https://developer.android.com/about/versions/11/privacy/storage)

### Kotlin Documentation
- [Kotlin Language](https://kotlinlang.org/docs/home.html)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

### Related Libraries
- MediaCodec for advanced video processing
- FFmpeg JNI bindings for complex editing
- ExoPlayer for advanced playback

## Contributing

1. Create feature branch from main
2. Implement feature with tests
3. Ensure all tests pass
4. Submit PR with description
5. Address review comments
6. Merge when approved

## Questions?

Refer to:
- This document
- Code comments
- CHANGELOG.md for feature history
- GitHub Issues for known problems
