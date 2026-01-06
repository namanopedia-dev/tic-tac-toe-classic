# Installation Guide for Android Studio

## Option 1: Install via Homebrew (Recommended)

Run the installation script:

```bash
bash install_android_studio.sh
```

This script will:
1. Install Homebrew (if not already installed)
2. Install Android Studio via Homebrew Cask

**Note**: You'll need to enter your administrator password during installation.

## Option 2: Manual Installation

### Step 1: Install Homebrew

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

For Apple Silicon (M4 Pro), add Homebrew to PATH:
```bash
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zshrc
eval "$(/opt/homebrew/bin/brew shellenv)"
```

### Step 2: Install Android Studio

```bash
brew install --cask android-studio
```

### Step 3: Download and Install Manually (Alternative)

If you prefer not to use Homebrew:

1. Visit: https://developer.android.com/studio
2. Download Android Studio for macOS (Apple Silicon)
3. Open the downloaded `.dmg` file
4. Drag Android Studio to Applications folder
5. Open Android Studio from Applications

## After Installation

### 1. First Launch Setup

1. Open Android Studio from Applications
2. Complete the setup wizard:
   - Choose "Standard" installation
   - Select installation location (default is fine)
   - Wait for SDK components to download

### 2. Install Required SDK Components

1. In Android Studio, go to: **Preferences → Appearance & Behavior → System Settings → Android SDK**
2. In **SDK Platforms** tab:
   - Check **Android 14.0 (API 34)**
   - Click "Apply" to install
3. In **SDK Tools** tab, ensure these are checked:
   - ✅ Android SDK Build-Tools
   - ✅ Android SDK Platform-Tools
   - ✅ Android Emulator
   - ✅ Google Play services
   - Click "Apply" to install

### 3. Accept Licenses

Run in terminal:
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses
```

Or accept through Android Studio's SDK Manager.

### 4. Set Up Environment Variables

Add to `~/.zshrc`:
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
```

Then reload:
```bash
source ~/.zshrc
```

### 5. Create Android Virtual Device (AVD)

1. In Android Studio, click **Tools → Device Manager**
2. Click **Create Device**
3. Select a device (e.g., Pixel 7)
4. Select system image: **API 34 (Android 14)** - **ARM64** (for M4 Pro)
5. Click **Finish**

### 6. Open and Build the Project

1. Open Android Studio
2. Select **Open an Existing Project**
3. Navigate to `/Users/namanopedia/First_app`
4. Wait for Gradle sync to complete (may take a few minutes on first run)
5. Build the project:
   - Click **Build → Make Project** (or press `Cmd+F9`)
   - Or use terminal: `./gradlew build`

## Verify Installation

Check if everything is set up correctly:

```bash
# Check Java
java -version

# Check Android SDK
adb version

# Check Gradle (after opening project in Android Studio)
./gradlew --version
```

## Troubleshooting

### Java not found
- Android Studio includes JDK 17
- Set JAVA_HOME: `export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"`

### Gradle sync fails
- Check internet connection
- In Android Studio: **File → Invalidate Caches / Restart**

### Emulator is slow
- Use ARM64 system images (not x86)
- Increase RAM allocation in AVD settings

### Build errors
- Ensure all SDK components are installed
- Check that `ANDROID_HOME` is set correctly
- Try: **File → Sync Project with Gradle Files**

