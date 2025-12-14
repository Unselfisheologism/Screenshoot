# Android Screen Recorder App

A comprehensive Android screen recording application with advanced editing capabilities, using Android's MediaProjection API and MediaRecorder for high-quality video capture.

## Features

### Phase 1: Core Features ✅
- **Screen Recording**: Full-screen capture using MediaProjection API
- **Start/Stop Mechanism**: Easy-to-use controls for recording
- **Audio Capture**: Options to record system audio, microphone, or both
- **Video Quality Settings**:
  - Low: 480p at 30fps, 2Mbps bitrate
  - Medium: 720p at 30fps, 5Mbps bitrate (default)
  - High: 1080p at 60fps, 10Mbps bitrate
  - Custom: User-configurable resolution, FPS, and bitrate
- **Recording Duration Limits**: Set maximum recording duration or unlimited
- **Pause/Resume**: Pause recording to skip unwanted sections
- **Storage Management**:
  - MP4 format with H.264 video and AAC audio
  - Scoped storage compliant (/Android/data/...)
  - Timestamp-based file naming
  - Automatic gallery integration
- **Basic Editing**:
  - Trim videos with SeekBar interface
  - Support for text overlays (framework in place)
  - Crop/Resize capabilities (framework in place)

### Phase 2: Advanced Features 🚀
- **Automatic Zoom on Interactions**: Detects taps, swipes, and long presses for automatic zoom effects
- **Motion Blur Effects**: Smooth motion blur for cinematic look on fast movements
- **Multi-Window Support**: Framework for split-screen recording (ready for enhancement)
- **Customizable Watermarks**: Add text or image watermarks with animation support
- **Enhanced Video Editing**: 
  - Advanced trimming
  - Speed ramps
  - Annotations (framework in place)
  - Auto-caption support (framework in place)
- **Shareable Presets**: Pre-configured recording profiles:
  - Tutorial Mode: High quality with zoom enhancements
  - Gaming Mode: High FPS for smooth gameplay
  - Presentation Mode: Optimized for demos
  - Quick Share: Low quality for fast sharing
- **Facecam Overlay**: Front camera integration for reactions/commentary
- **AI-Powered Auto-Edits**:
  - Automatic silence detection
  - Chapter generation
  - Enhancement suggestions
  - Color grading recommendations

### Ads Integration
- Non-intrusive banner ads on main screen
- Interstitial ads after recording (framework ready)
- Media.net compatible integration structure

## Architecture

### Core Components

#### Services
- **RecordingService**: Foreground service managing the recording lifecycle
  - Handles start/stop/pause/resume
  - Manages MediaProjection lifecycle
  - Sends notifications and updates UI
  
- **OverlayService**: Floating overlay UI during recording
  - Timer display (Chronometer)
  - Pause/Resume buttons
  - Stop button with easy access

#### Core Classes
- **ScreenRecorder**: Main recording engine
  - MediaProjection + MediaRecorder integration
  - Quality and audio configuration
  - Pause/Resume functionality
  
- **VideoEditor**: Post-recording editing
  - Video trimming using MediaExtractor
  - Text overlay support
  - Video duration extraction
  
- **AutoEditEngine**: AI-powered editing suggestions
  - Silence detection
  - Chapter generation
  - Enhancement recommendations
  
- **InteractionDetector**: Touch event analysis
  - Tap detection
  - Swipe detection
  - Long press detection
  - Zoom area calculation
  
- **MotionBlurEffect**: Motion blur implementation
  - Configurable intensity
  - Motion detection
  - Frame analysis

#### UI Activities
- **MainActivity**: Main app interface with recording controls and ad container
- **SettingsActivity**: Configure video quality, audio, and duration
- **GalleryActivity**: Browse, play, edit, share, and delete recordings
- **EditorActivity**: Basic video editing with trimming
- **AdvancedEditorActivity**: Advanced editing tools and AI features

#### Utilities
- **FileUtils**: File operations, storage management, sharing
- **SettingsManager**: SharedPreferences-based settings persistence
- **AdManager**: Ads integration and lifecycle

#### Managers
- **PresetManager**: Access to pre-configured recording profiles

## Permissions

- `RECORD_AUDIO`: Record audio from microphone
- `WRITE_EXTERNAL_STORAGE`: Save recordings (API < 33)
- `READ_EXTERNAL_STORAGE`: Read for sharing
- `FOREGROUND_SERVICE`: Keep service running while recording
- `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`: Prevent battery optimizations from stopping recording
- `CAPTURE_AUDIO_OUTPUT`: Capture system audio
- `SYSTEM_ALERT_WINDOW`: Display recording overlay
- `CAMERA`: Front camera for facecam overlay

## Technical Stack

- **Language**: Kotlin
- **API Level**: 21+ (Android 5.0 and above)
- **Build System**: Gradle 8.4
- **JDK**: Java 17
- **Architecture**: Service-based with foreground services

### Key Dependencies
- AndroidX Core & AppCompat
- Material Components
- ConstraintLayout
- Lifecycle Components
- RecyclerView

## Building

### Prerequisites
- JDK 17 or higher
- Android SDK with API 34
- Gradle 8.4

### Build Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## CI/CD

GitHub Actions workflow configured for:
- Unit tests
- Instrumented tests
- Debug and release APK builds
- APK signing
- Artifact upload
- Automatic GitHub releases

## Testing

### Unit Tests Included
- `FileUtilsTest`: File size formatting tests
- `VideoQualityTest`: Video quality settings validation
- `RecordingSettingsTest`: Settings model tests

### Test Coverage
- File operations and utilities
- Video quality configurations
- Settings persistence
- Model validations

## File Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/screenrecorder/
│   │   │   ├── core/           # Core recording and editing logic
│   │   │   ├── service/        # Android services
│   │   │   ├── ui/             # Activities and adapters
│   │   │   ├── util/           # Utilities and managers
│   │   │   ├── model/          # Data classes and models
│   │   │   ├── ads/            # Ad integration
│   │   │   └── ...
│   │   ├── res/
│   │   │   ├── layout/         # UI layouts
│   │   │   ├── drawable/       # Icons and drawables
│   │   │   ├── values/         # Strings, colors, themes
│   │   │   ├── xml/            # Configurations
│   │   │   └── mipmap-*/       # App icons
│   │   └── AndroidManifest.xml
│   └── test/                   # Unit tests
├── build.gradle                # App-level build config
└── proguard-rules.pro         # ProGuard rules
```

## Future Enhancements

1. **Machine Learning Integration**: On-device ML for auto-enhancements
2. **Cloud Storage**: Optional cloud backup and sync
3. **Advanced Codec Support**: AV1, VP9 codec options
4. **Hardware Acceleration**: Better GPU utilization for recording
5. **Real-time Filters**: Live filters during recording
6. **Batch Processing**: Process multiple videos in queue
7. **Timeline Editor**: Multi-track editing interface
8. **Green Screen**: Virtual background support

## Notes

- All recordings are stored locally in scoped storage
- No cloud services or user authentication required
- Non-intrusive advertising via Media.net
- Battery optimization handled with foreground service
- Respects device storage constraints
- Graceful handling of permissions and interruptions

## License

This project is provided as-is for educational and development purposes.

## Support

For issues, questions, or feature requests, please refer to the GitHub repository.
