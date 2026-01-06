# Publishing to Google Play Store - Complete Guide

This guide will walk you through the entire process of publishing your TicTacToe Android app to the Google Play Store.

## Prerequisites

Before you begin, ensure you have:

1. **Google Play Console Account**
   - One-time registration fee: $25 USD (one-time, lifetime)
   - Visit: https://play.google.com/console/signup
   - Use the same Google account you'll use for AdMob

2. **AdMob Account** (for ads) ⚠️ **REQUIRED for this app**
   - Visit: https://apps.admob.com/
   - Create an account and register your app
   - Get your App ID and Ad Unit IDs
   - **See [GOOGLE_ADS_SETUP.md](GOOGLE_ADS_SETUP.md) for detailed AdMob setup**

3. **Privacy Policy URL** (required for apps with ads)
   - Create a privacy policy page
   - Host it on a website (GitHub Pages, your own site, etc.)
   - Must mention AdMob data collection

---

## Step 1: Prepare Your App for Release

### 1.1 Generate a Release Keystore

The keystore is used to sign your app. **Keep this file safe - you'll need it for all future updates!**

```bash
# Navigate to your project directory
cd /Users/namanopedia/First_app

# Create the release directory
mkdir -p app/release

# Generate the keystore
keytool -genkey -v -keystore app/release/tictactoe-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias tictactoe-key
```

**Important Information to Save:**
- Keystore file location: `app/release/tictactoe-release-key.jks`
- Key alias: `tictactoe-key`
- Store password: (the one you entered)
- Key password: (the one you entered)

**⚠️ CRITICAL:** Store this information securely. If you lose the keystore, you cannot update your app on Play Store!

### 1.2 Configure Keystore Properties

1. Copy the example file:
   ```bash
   cp keystore.properties.example keystore.properties
   ```

2. Edit `keystore.properties` with your actual values:
   ```properties
   storeFile=app/release/tictactoe-release-key.jks
   keyAlias=tictactoe-key
   storePassword=your-actual-store-password
   keyPassword=your-actual-key-password
   ```

3. **Verify** that `keystore.properties` is in `.gitignore` (it should be)

### 1.3 Update AdMob Configuration

**📚 For detailed AdMob setup, see [GOOGLE_ADS_SETUP.md](GOOGLE_ADS_SETUP.md)**

**Quick steps:**

1. **Create AdMob Account:**
   - Go to https://apps.admob.com/
   - Sign in with your Google account
   - Complete registration

2. **Add Your App to AdMob:**
   - Click "Apps" → "Add app"
   - Select Android platform
   - Package name: `com.tictactoe`
   - Get your App ID (format: `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`)

3. **Create Interstitial Ad Unit:**
   - Go to your app → "Ad units" → "Add ad unit"
   - Select "Interstitial"
   - Copy the Ad Unit ID (format: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`)

4. **Update AndroidManifest.xml:**
   - Open `app/src/main/AndroidManifest.xml`
   - Replace the test App ID with your production App ID:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX" />
   ```

5. **Update AdManager.kt:**
   - Open `app/src/main/java/com/tictactoe/AdManager.kt`
   - Replace the test Ad Unit ID with your production Interstitial Ad Unit ID:
   ```kotlin
   private const val AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
   ```

**⚠️ Important:** 
- Use test IDs during development
- Switch to production IDs only before release
- Ad units may take 24-48 hours to be fully active

### 1.4 Update Version Information

Before each release, update version in `app/build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 1  // Increment by 1 for each release
    versionName = "1.0.0"  // Human-readable version
    // ...
}
```

**Version Code Rules:**
- Must be an integer
- Must increase with each release
- Play Store uses this to determine which version is newer

**Version Name Rules:**
- Can be any string (e.g., "1.0.0", "2.1.3")
- For users to see

### 1.5 Test Your Release Build

Before uploading, test the release build:

```bash
# Build release APK for testing
./gradlew assembleRelease

# Install on a device/emulator
adb install app/build/outputs/apk/release/app-release.apk

# Test thoroughly:
# - Game functionality
# - Ads display correctly
# - No crashes
# - UI looks good
```

---

## Step 2: Build the Release AAB (Android App Bundle)

Google Play Store requires **AAB (Android App Bundle)** format, not APK.

```bash
# Set Java home (if needed)
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

# Build the release AAB
./gradlew bundleRelease
```

The AAB file will be at:
```
app/build/outputs/bundle/release/app-release.aab
```

**File size:** Typically 1-5 MB (much smaller than APK)

---

## Step 3: Set Up Google Play Console

### 3.1 Create Your Developer Account

1. Go to https://play.google.com/console/signup
2. Pay the one-time $25 registration fee
3. Complete your developer profile:
   - Developer name (appears in Play Store)
   - Email address
   - Website (optional)
   - Phone number

### 3.2 Create Your App

1. In Play Console, click **"Create app"**
2. Fill in the details:
   - **App name:** "Tic Tac Toe" (or your preferred name)
   - **Default language:** English (United States)
   - **App or game:** Game
   - **Free or paid:** Free
   - **Declarations:** Check all that apply (ads, etc.)

3. Click **"Create app"**

---

## Step 4: Complete App Content

### 4.1 App Access

1. Go to **Policy → App content**
2. Complete all required sections:
   - **Privacy Policy:** Add your privacy policy URL (required for ads)
   - **Target audience:** Select appropriate age group
   - **Content rating:** Complete questionnaire (typically "Everyone" for Tic Tac Toe)
   - **Data safety:** Declare what data you collect (if any)

### 4.2 Content Rating

1. Go to **Policy → Content rating**
2. Complete the questionnaire:
   - Game type: Board game
   - Violence: None
   - Language: None
   - Sexual content: None
   - Drugs: None
   - Gambling: None
3. Submit for rating (usually instant for simple games)

### 4.3 Data Safety

1. Go to **Policy → Data safety**
2. Answer questions about data collection:
   - **Does your app collect or share any of the required user data types?**
     - For basic Tic Tac Toe with ads: Typically "No" (unless you collect analytics)
   - If using AdMob: You may need to declare ad serving
3. Complete all sections

---

## Step 5: Create Store Listing

### 5.1 Required Assets

Go to **Store presence → Main store listing** and prepare:

1. **App name:** "Tic Tac Toe" (30 characters max)
2. **Short description:** 80 characters max
   - Example: "Classic Tic Tac Toe game with two-player and AI modes"
3. **Full description:** 4000 characters max
   ```
   Play the classic game of Tic Tac Toe on your Android device!
   
   Features:
   • Two-player mode - Play with a friend
   • Single-player mode - Challenge the AI
   • Clean, modern Material Design interface
   • Free to play
   
   Perfect for quick games and passing time. Challenge yourself or play with friends!
   ```

4. **App icon:** 512 x 512 px PNG (no transparency)
   - Create or use existing icon from `app/src/main/res/mipmap-xxxhdpi/ic_launcher.png`
   - Export at 512x512 for Play Store

5. **Feature graphic:** 1024 x 500 px PNG
   - Promotional banner shown at top of Play Store listing
   - Can include app name, screenshots, or graphics

6. **Screenshots:** At least 2, up to 8
   - Phone screenshots: 16:9 or 9:16 aspect ratio
   - Minimum: 320px, Maximum: 3840px
   - Recommended: 1080 x 1920 px (portrait) or 1920 x 1080 px (landscape)
   - Take screenshots from your app running on device/emulator

7. **Promotional video (optional):** YouTube URL

### 5.2 How to Take Screenshots

**Option 1: Using Android Studio**
1. Run app on emulator
2. Use emulator's screenshot tool
3. Or use: `adb shell screencap -p /sdcard/screenshot.png`
4. Pull: `adb pull /sdcard/screenshot.png`

**Option 2: Using Physical Device**
1. Run app on device
2. Take screenshots using device's screenshot feature
3. Transfer to computer

**Recommended Screenshots:**
- Main menu screen
- Game in progress (two-player)
- Game in progress (AI mode)
- Game over screen
- Settings (if any)

---

## Step 6: Upload Your App

### 6.1 Create a Release

1. Go to **Production** (or **Testing → Internal testing** for testing first)
2. Click **"Create new release"**
3. Upload your AAB file:
   - Click **"Upload"**
   - Select `app/build/outputs/bundle/release/app-release.aab`
   - Wait for upload and processing (may take a few minutes)

### 6.2 Release Notes

Add release notes for users:
```
Version 1.0.0
- Initial release
- Two-player and single-player modes
- AI opponent with smart gameplay
- Clean Material Design interface
```

### 6.3 Review and Rollout

1. Review all information
2. Click **"Save"** (for testing) or **"Review release"** (for production)
3. For production, you'll need to complete:
   - Store listing (100% complete)
   - Content rating (completed)
   - Data safety (completed)
   - App access (privacy policy, etc.)

---

## Step 7: Testing Before Production

**Recommended:** Test your release before going to production.

### 7.1 Internal Testing

1. Go to **Testing → Internal testing**
2. Upload AAB to internal testing track
3. Add testers (your email)
4. Share testing link with yourself
5. Install and test thoroughly

### 7.2 Closed Testing (Optional)

1. Create a closed testing track
2. Add testers via email
3. Test with a small group before public release

---

## Step 8: Submit for Review

### 8.1 Final Checklist

Before submitting, verify:

- [ ] AAB uploaded successfully
- [ ] Store listing 100% complete
- [ ] Privacy policy URL added (required for ads)
- [ ] Content rating completed
- [ ] Data safety form completed
- [ ] App tested on multiple devices
- [ ] Screenshots look good
- [ ] App icon uploaded
- [ ] Feature graphic uploaded
- [ ] Release notes added
- [ ] AdMob App ID updated (not test ID)
- [ ] Ad Unit IDs updated (not test IDs)

### 8.2 Submit for Review

1. Go to **Production**
2. Click **"Review release"**
3. Review all warnings/errors
4. Click **"Start rollout to Production"**
5. Your app will be submitted for review

**Review Time:**
- Usually 1-3 days for new apps
- Can take up to 7 days
- You'll receive email notifications

---

## Step 9: After Publication

### 9.1 Monitor Your App

1. **Dashboard:** Check app statistics
2. **Reviews:** Respond to user reviews
3. **Crashes:** Monitor crash reports in Play Console
4. **Analytics:** Check installs, ratings, etc.

### 9.2 Update Your App

When you need to release an update:

1. Update `versionCode` and `versionName` in `build.gradle.kts`
2. Make your changes
3. Build new AAB: `./gradlew bundleRelease`
4. Upload new AAB to Play Console
5. Add release notes
6. Submit for review

**Important:** Use the same keystore file for all updates!

---

## Troubleshooting

### Build Errors

**Error: "Keystore file not found"**
- Check `keystore.properties` path is correct
- Ensure keystore file exists at specified path

**Error: "Signing config not found"**
- Verify `keystore.properties` exists
- Check all properties are filled correctly

### Play Console Errors

**"Privacy policy required"**
- Add privacy policy URL in App content section
- Must be accessible publicly

**"Content rating incomplete"**
- Complete the content rating questionnaire
- Wait for rating to be processed

**"Data safety form incomplete"**
- Complete all sections in Data safety
- Declare data collection accurately

### Ad Issues

**Ads not showing in production**
- Verify AdMob App ID is correct (not test ID)
- Check Ad Unit IDs are correct
- Wait for ad approval (can take 24-48 hours)
- Check AdMob console for ad serving status

---

## Additional Resources

- **Google Play Console Help:** https://support.google.com/googleplay/android-developer
- **AdMob Help:** https://support.google.com/admob
- **Android Developer Guide:** https://developer.android.com/distribute/googleplay/start
- **Material Design:** https://material.io/design

---

## Quick Reference Commands

```bash
# Build release AAB
./gradlew bundleRelease

# Build release APK (for testing)
./gradlew assembleRelease

# Test release APK
adb install app/build/outputs/apk/release/app-release.apk

# Check AAB location
ls -lh app/build/outputs/bundle/release/app-release.aab
```

---

## Important Reminders

1. **Keep your keystore safe** - Back it up securely
2. **Test thoroughly** before production release
3. **Update AdMob IDs** - Don't use test IDs in production
4. **Privacy policy required** - Must have URL for apps with ads
5. **Version code must increase** - For each new release
6. **Review can take time** - Be patient, usually 1-3 days

Good luck with your publication! 🚀

