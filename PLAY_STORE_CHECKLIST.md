# Google Play Store Publishing Checklist

Use this checklist to ensure you've completed all steps before publishing.

## Pre-Build Checklist

- [ ] Google Play Console account created ($25 one-time fee paid)
- [ ] AdMob account created and app registered
  - [ ] App added to AdMob with package name `com.tictactoe`
  - [ ] Production App ID obtained
  - [ ] Interstitial Ad Unit created and ID obtained
- [ ] Privacy policy URL created and accessible (must mention AdMob)
- [ ] Release keystore generated (`app/release/tictactoe-release-key.jks`)
- [ ] `keystore.properties` file created with correct values
- [ ] AdMob App ID updated in `AndroidManifest.xml` (not test ID)
- [ ] Ad Unit IDs updated in `AdManager.kt` (not test IDs)
- [ ] Version code and version name updated in `build.gradle.kts`
- [ ] App tested thoroughly on device/emulator
- [ ] Ads tested and working correctly

## Build Checklist

- [ ] Release AAB built successfully: `./gradlew bundleRelease`
- [ ] AAB file verified at: `app/build/outputs/bundle/release/app-release.aab`
- [ ] AAB file size is reasonable (typically 1-5 MB)

## Play Console Setup Checklist

- [ ] App created in Play Console
- [ ] App name, description, and category set
- [ ] Store listing 100% complete:
  - [ ] App name (30 chars max)
  - [ ] Short description (80 chars max)
  - [ ] Full description (4000 chars max)
  - [ ] App icon (512x512 PNG)
  - [ ] Feature graphic (1024x500 PNG)
  - [ ] At least 2 screenshots uploaded
- [ ] Privacy policy URL added
- [ ] Content rating completed
- [ ] Data safety form completed
- [ ] App access section completed

## Upload Checklist

- [ ] AAB uploaded to Production (or Testing track first)
- [ ] Release notes added
- [ ] All warnings/errors resolved
- [ ] Ready for review

## Pre-Submit Final Check

- [ ] All checkboxes above completed
- [ ] App tested on multiple devices/Android versions
- [ ] No crashes or critical bugs
- [ ] Ads displaying correctly (if applicable)
- [ ] App icon and graphics look professional
- [ ] Store listing text is error-free

## After Submission

- [ ] Monitor Play Console for review status
- [ ] Respond to any review feedback
- [ ] Prepare to respond to user reviews after launch

---

**Quick Commands:**

```bash
# Build release AAB
./gradlew bundleRelease

# Build release APK (for testing)
./gradlew assembleRelease

# Install test APK
adb install app/build/outputs/apk/release/app-release.apk
```

**File Locations:**
- AAB: `app/build/outputs/bundle/release/app-release.aab`
- APK: `app/build/outputs/apk/release/app-release.apk`
- Keystore: `app/release/tictactoe-release-key.jks`

**Important Links:**
- Play Console: https://play.google.com/console
- AdMob: https://apps.admob.com/
- Full Guide: See `PLAY_STORE_PUBLISHING.md`

