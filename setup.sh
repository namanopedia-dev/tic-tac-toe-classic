#!/bin/bash

# Setup script for Android Tic Tac Toe App Development
# MacBook Pro M4 Pro - Development Environment Setup

echo "=========================================="
echo "Android Tic Tac Toe App - Setup Script"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# 1. Check Homebrew
echo "1. Checking Homebrew..."
if command_exists brew; then
    echo -e "${GREEN}✓ Homebrew is installed${NC}"
    brew --version
else
    echo -e "${YELLOW}✗ Homebrew not found${NC}"
    echo "Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Homebrew installed successfully${NC}"
        # Add Homebrew to PATH for Apple Silicon
        echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zshrc
        eval "$(/opt/homebrew/bin/brew shellenv)"
    else
        echo -e "${RED}✗ Homebrew installation failed${NC}"
        exit 1
    fi
fi
echo ""

# 2. Check Git
echo "2. Checking Git..."
if command_exists git; then
    echo -e "${GREEN}✓ Git is installed${NC}"
    git --version
    if [ -z "$(git config --global user.name)" ]; then
        echo -e "${YELLOW}⚠ Git user.name not configured${NC}"
        echo "Run: git config --global user.name 'Your Name'"
    fi
    if [ -z "$(git config --global user.email)" ]; then
        echo -e "${YELLOW}⚠ Git user.email not configured${NC}"
        echo "Run: git config --global user.email 'your.email@example.com'"
    fi
else
    echo -e "${YELLOW}✗ Git not found${NC}"
    echo "Installing Git via Homebrew..."
    brew install git
fi
echo ""

# 3. Check Java
echo "3. Checking Java..."
if command_exists java; then
    echo -e "${GREEN}✓ Java is installed${NC}"
    java -version
else
    echo -e "${YELLOW}⚠ Java not found in PATH${NC}"
    echo "Note: Android Studio includes JDK 17. If Android Studio is installed, Java should be available."
    echo "Make sure Android Studio is installed and configured."
fi
echo ""

# 4. Check Android SDK
echo "4. Checking Android SDK..."
if [ -d "$HOME/Library/Android/sdk" ]; then
    echo -e "${GREEN}✓ Android SDK directory found${NC}"
    ANDROID_HOME="$HOME/Library/Android/sdk"
    if [ -f "$ANDROID_HOME/platform-tools/adb" ]; then
        echo -e "${GREEN}✓ ADB found${NC}"
        "$ANDROID_HOME/platform-tools/adb" version
    else
        echo -e "${YELLOW}⚠ ADB not found${NC}"
    fi
else
    echo -e "${YELLOW}⚠ Android SDK not found${NC}"
    echo "Please install Android Studio and complete the setup wizard."
    echo "Download from: https://developer.android.com/studio"
fi
echo ""

# 5. Setup Environment Variables
echo "5. Setting up environment variables..."
ZSHRC="$HOME/.zshrc"
ANDROID_HOME_VAR='export ANDROID_HOME=$HOME/Library/Android/sdk'
PATH_VARS=(
    'export PATH=$PATH:$ANDROID_HOME/emulator'
    'export PATH=$PATH:$ANDROID_HOME/platform-tools'
    'export PATH=$PATH:$ANDROID_HOME/tools'
    'export PATH=$PATH:$ANDROID_HOME/tools/bin'
)

if ! grep -q "ANDROID_HOME" "$ZSHRC" 2>/dev/null; then
    echo "" >> "$ZSHRC"
    echo "# Android SDK" >> "$ZSHRC"
    echo "$ANDROID_HOME_VAR" >> "$ZSHRC"
    for path_var in "${PATH_VARS[@]}"; do
        echo "$path_var" >> "$ZSHRC"
    done
    echo -e "${GREEN}✓ Environment variables added to ~/.zshrc${NC}"
    echo "Run 'source ~/.zshrc' or restart terminal to apply changes."
else
    echo -e "${GREEN}✓ Environment variables already configured${NC}"
fi
echo ""

# 6. Check Android Studio
echo "6. Checking Android Studio..."
if [ -d "/Applications/Android Studio.app" ]; then
    echo -e "${GREEN}✓ Android Studio is installed${NC}"
else
    echo -e "${YELLOW}⚠ Android Studio not found${NC}"
    echo "Please download and install Android Studio from:"
    echo "https://developer.android.com/studio"
    echo ""
    echo "After installation:"
    echo "1. Complete the setup wizard"
    echo "2. Install Android SDK Platform 34"
    echo "3. Install Android SDK Build-Tools"
    echo "4. Create an Android Virtual Device (AVD)"
fi
echo ""

# Summary
echo "=========================================="
echo "Setup Summary"
echo "=========================================="
echo ""
echo "Next steps:"
echo "1. If Android Studio is not installed, download and install it"
echo "2. Open Android Studio and complete the setup wizard"
echo "3. Install Android SDK Platform 34 (API 34)"
echo "4. Create an Android Virtual Device"
echo "5. Run 'source ~/.zshrc' to apply environment variables"
echo "6. Verify installation: adb version"
echo ""
echo "For GitHub setup:"
echo "1. Create a GitHub account (if needed)"
echo "2. Generate SSH key: ssh-keygen -t ed25519 -C 'your.email@example.com'"
echo "3. Add SSH key to GitHub account"
echo "4. Test: ssh -T git@github.com"
echo ""
echo "=========================================="

