#!/bin/bash

# Installation script for Android Studio on macOS
# Run this script with: bash install_android_studio.sh

set -e

echo "=========================================="
echo "Android Studio Installation Script"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo -e "${YELLOW}Homebrew not found. Installing Homebrew...${NC}"
    echo "This will require your administrator password."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    
    # Add Homebrew to PATH for Apple Silicon
    if [[ -f "/opt/homebrew/bin/brew" ]]; then
        echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zshrc
        eval "$(/opt/homebrew/bin/brew shellenv)"
    fi
else
    echo -e "${GREEN}✓ Homebrew is already installed${NC}"
    brew --version
fi

echo ""
echo "Installing Android Studio via Homebrew..."
brew install --cask android-studio

echo ""
echo -e "${GREEN}✓ Android Studio installation complete!${NC}"
echo ""
echo "Next steps:"
echo "1. Open Android Studio from Applications"
echo "2. Complete the setup wizard:"
echo "   - Install Android SDK (API 34)"
echo "   - Install Android SDK Platform-Tools"
echo "   - Install Android SDK Build-Tools"
echo "   - Accept licenses"
echo "3. Open this project in Android Studio"
echo "4. Wait for Gradle sync to complete"
echo "5. Build the project (Build → Make Project)"
echo ""

