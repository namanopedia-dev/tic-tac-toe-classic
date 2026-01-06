# Tic Tac Toe Android App

A native Android Tic Tac Toe game built with Kotlin, featuring two-player and single-player (vs AI) modes, with Google Ads integration.

## Features

- **Two Player Mode**: Play locally with a friend
- **Single Player Mode**: Play against an AI opponent with minimax algorithm
- **Google Ads**: Interstitial ads shown after each game
- **Material Design**: Modern UI with Material Design 3 components
- **Play Store Ready**: Configured for Google Play Store deployment

## Required Installations (MacBook Pro M4 Pro)

Since this is a new MacBook Pro M4 Pro, you need to install the following tools:

### 1. Homebrew (Package Manager)
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
brew --version
```

### 2. Git
```bash
# Check if installed
git --version

# If not installed
brew install git

# Configure Git
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### 3. Android Studio
1. Download Android Studio Hedgehog or later from [developer.android.com](https://developer.android.com/studio)
2. Install for Apple Silicon (M4 Pro)
3. During first launch:
   - Complete setup wizard
   - Install Android SDK (API 34 recommended)
   - Install Android SDK Platform-Tools
   - Install Android SDK Build-Tools
   - Accept licenses: `sdkmanager --licenses` (or through SDK Manager in Android Studio)

### 4. Java Development Kit (JDK)
- Android Studio includes JDK 17 (bundled)
- Verify: `java -version` (should show JDK 17 or later)

### 5. Android SDK Components
- Android SDK Platform 34 (Android 14)
- Android SDK Build-Tools 34.0.0
- Android SDK Command-line Tools
- Android Emulator (for testing)
- Use ARM64 system images for M4 Pro compatibility

### 6. Environment Variables Setup
Add to `~/.zshrc`:
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
```

Then reload: `source ~/.zshrc`

### 7. Verify Installations
```bash
git --version
java -version
adb version
```

### 8. Android Studio Configuration
1. Open Android Studio
2. Go to Preferences → Appearance & Behavior → System Settings → Android SDK
3. Ensure SDK Platforms tab has Android 14.0 (API 34) installed
4. Ensure SDK Tools tab has:
   - Android SDK Build-Tools
   - Android SDK Platform-Tools
   - Android Emulator
   - Google Play services

### 9. Create Android Virtual Device (AVD)
1. Open AVD Manager in Android Studio
2. Create a new virtual device (e.g., Pixel 7 with API 34)
3. Use ARM64 system image (for M4 Pro compatibility)

### 10. GitHub Account Setup (for Private Repo)
```bash
# Generate SSH key
ssh-keygen -t ed25519 -C "your.email@example.com"

# Add SSH key to GitHub account (copy public key)
cat ~/.ssh/id_ed25519.pub

# Test connection
ssh -T git@github.com
```

## Setup Instructions

1. **Clone the repository** (or use this as a new project):
   ```bash
   git clone <your-repo-url>
   cd First_app
   ```

2. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project directory

3. **Sync Gradle**:
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Configure AdMob** (for production):
   - See **[GOOGLE_ADS_SETUP.md](GOOGLE_ADS_SETUP.md)** for detailed instructions
   - Quick steps:
     - Create AdMob account at https://apps.admob.com/
     - Add your app to AdMob (package name: `com.tictactoe`)
     - Create Interstitial Ad Unit
     - Update App ID in `AndroidManifest.xml`
     - Update Ad Unit ID in `AdManager.kt`

5. **Run the app**:
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio or press `Shift+F10`

## Building for Release

### 1. Generate Keystore
```bash
keytool -genkey -v -keystore app/release/tictactoe-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias tictactoe-key
```

### 2. Configure Signing
1. Copy `keystore.properties.example` to `keystore.properties` in the root directory
2. Fill in your keystore details:
   ```properties
   storeFile=app/release/tictactoe-release-key.jks
   keyAlias=tictactoe-key
   storePassword=your-store-password
   keyPassword=your-key-password
   ```

3. **Important**: Add `keystore.properties` to `.gitignore` (already included)

### 3. Build Release APK
```bash
./gradlew assembleRelease
```
The APK will be at: `app/build/outputs/apk/release/app-release.apk`

### 4. Build Release AAB (for Play Store)
```bash
./gradlew bundleRelease
```
The AAB will be at: `app/build/outputs/bundle/release/app-release.aab`

## Publishing to Google Play Store

📚 **Complete Publishing Guides:**
- **[PLAY_STORE_PUBLISHING.md](PLAY_STORE_PUBLISHING.md)** - Comprehensive step-by-step guide
- **[PLAY_STORE_CHECKLIST.md](PLAY_STORE_CHECKLIST.md)** - Quick checklist for publishing

## Google Ads (AdMob) Setup

📚 **Complete Google Ads Guides:**
- **[GOOGLE_ADS_SETUP.md](GOOGLE_ADS_SETUP.md)** - Comprehensive AdMob setup and optimization guide
- **[GOOGLE_ADS_CHECKLIST.md](GOOGLE_ADS_CHECKLIST.md)** - Quick checklist for AdMob setup

**Current Status:** ✅ Google Ads SDK integrated with interstitial ads shown after each game

### Quick Start

1. **Generate keystore:**
   ```bash
   keytool -genkey -v -keystore app/release/tictactoe-release-key.jks \
     -keyalg RSA -keysize 2048 -validity 10000 -alias tictactoe-key
   ```

2. **Configure `keystore.properties`** (copy from `keystore.properties.example`)

3. **Update AdMob IDs** in `AndroidManifest.xml` and `AdManager.kt`

4. **Build release AAB:**
   ```bash
   ./gradlew bundleRelease
   ```

5. **Upload to Play Console** at `app/build/outputs/bundle/release/app-release.aab`

See the full guides above for detailed instructions.

## Google Play Store Deployment Checklist

- [x] App signing configured
- [x] Version code/name set in `app/build.gradle.kts`
- [x] Min SDK: 21 (Android 5.0)
- [x] Target SDK: 34 (Android 14)
- [x] Internet permission declared in `AndroidManifest.xml`
- [x] AdMob App ID configured
- [x] ProGuard rules for ads in `app/proguard-rules.pro`
- [ ] App icon and assets (add to `app/src/main/res/mipmap-*/`)
- [ ] Privacy policy URL (required for ads)
- [ ] Content rating (complete in Play Console)
- [ ] Store listing (screenshots, description, etc.)

### Additional Requirements for Play Store

1. **Privacy Policy**: Required when using ads
   - Create a privacy policy page
   - Add URL in Play Console → App content → Privacy Policy

2. **App Icon**: 
   - Add app icon to `app/src/main/res/mipmap-*/ic_launcher.png`
   - Recommended sizes: 48dp, 72dp, 96dp, 144dp, 192dp

3. **Content Rating**: 
   - Complete questionnaire in Play Console
   - For a simple game like Tic Tac Toe, typically rated "Everyone"

4. **Store Listing**:
   - App name, short description, full description
   - Screenshots (phone, tablet if applicable)
   - Feature graphic
   - Promotional video (optional)

## Project Structure

```
First_app/
├── app/
│   ├── build.gradle.kts          # App-level build configuration
│   ├── proguard-rules.pro         # ProGuard rules for release builds
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/tictactoe/
│       │   │   ├── MainActivity.kt      # Home screen
│       │   │   ├── GameActivity.kt      # Game screen
│       │   │   ├── GameLogic.kt         # Game logic and state
│       │   │   ├── AIPlayer.kt          # AI implementation
│       │   │   └── AdManager.kt         # Google Ads integration
│       │   └── res/
│       │       ├── layout/              # UI layouts
│       │       ├── values/              # Strings, colors, themes
│       │       └── xml/                 # Backup rules
│       └── release/
│           └── keystore.properties.example
├── build.gradle.kts               # Project-level build configuration
├── settings.gradle.kts            # Project settings
├── gradle.properties              # Gradle properties
├── .gitignore                     # Git ignore rules
├── README.md                       # This file
└── setup.sh                       # Setup script for development environment
```

## Dependencies

- **AndroidX Core KTX**: 1.12.0
- **AndroidX AppCompat**: 1.6.1
- **Material Design Components**: 1.11.0
- **Google Mobile Ads SDK**: 22.6.0
- **Kotlin Coroutines**: 1.7.3
- **AndroidX Lifecycle**: 2.7.0

## Testing

The app uses test ad unit IDs by default. To test with real ads:
1. Register your app in AdMob
2. Create ad units
3. Replace test IDs with production IDs
4. Wait for ads to be approved (can take 24-48 hours)

## Troubleshooting

### Build Errors
- **Gradle sync failed**: Check internet connection, try "Invalidate Caches / Restart" in Android Studio
- **SDK not found**: Ensure Android SDK is properly installed and ANDROID_HOME is set
- **Signing errors**: Verify keystore.properties file exists and paths are correct

### Ad Issues
- **Ads not showing**: Check internet connection, verify Ad Unit IDs are correct
- **Test ads work but production ads don't**: Wait for ad approval in AdMob

### Emulator Issues (M4 Pro)
- Use ARM64 system images for better performance
- If emulator is slow, increase RAM allocation in AVD settings

## License

[Add your license here]

## Contributing

[Add contribution guidelines if applicable]

## Support

For issues and questions, please open an issue in the repository.

