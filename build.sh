#!/bin/bash

# Build script for Tic Tac Toe Android App
# This script sets up the environment and builds the project

set -e

echo "=========================================="
echo "Building Tic Tac Toe Android App"
echo "=========================================="
echo ""

# Try to find Java/Android Studio JDK
if [ -d "/Applications/Android Studio.app/Contents/jbr/Contents/Home" ]; then
    export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
    echo "✓ Found Android Studio JDK"
elif [ -d "/Applications/Android Studio.app/Contents/jre/Contents/Home" ]; then
    export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
    echo "✓ Found Android Studio JDK (legacy location)"
else
    # Try to use system Java
    if command -v java &> /dev/null; then
        export JAVA_HOME=$(/usr/libexec/java_home 2>/dev/null || echo "")
        if [ -z "$JAVA_HOME" ]; then
            echo "⚠ Java not found. Please install Android Studio first."
            echo "Run: bash install_android_studio.sh"
            exit 1
        fi
    else
        echo "⚠ Java not found. Please install Android Studio first."
        echo "Run: bash install_android_studio.sh"
        exit 1
    fi
fi

echo "Using JAVA_HOME: $JAVA_HOME"
echo ""

# Set Android SDK path if available
if [ -d "$HOME/Library/Android/sdk" ]; then
    export ANDROID_HOME="$HOME/Library/Android/sdk"
    export PATH=$PATH:$ANDROID_HOME/platform-tools
    echo "✓ Found Android SDK at $ANDROID_HOME"
else
    echo "⚠ Android SDK not found. Android Studio will set this up on first launch."
fi

echo ""
echo "Building project..."
echo ""

# Make gradlew executable
chmod +x ./gradlew

# Build the project
./gradlew build --no-daemon

echo ""
echo "=========================================="
echo "Build complete!"
echo "=========================================="
echo ""
echo "APK location: app/build/outputs/apk/debug/app-debug.apk"
echo ""

