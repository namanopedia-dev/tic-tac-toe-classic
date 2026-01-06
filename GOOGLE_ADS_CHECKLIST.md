# Google Ads (AdMob) Setup Checklist

Quick checklist for setting up Google Ads in your app.

## AdMob Account Setup

- [ ] AdMob account created at https://apps.admob.com/
- [ ] Account verified (payment info added)
- [ ] App added to AdMob Console
  - [ ] Package name: `com.tictactoe` (must match exactly)
  - [ ] App ID obtained: `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`
- [ ] Interstitial Ad Unit created
  - [ ] Ad Unit ID obtained: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`

## Code Updates

- [ ] **AndroidManifest.xml** updated with production App ID
  - File: `app/src/main/AndroidManifest.xml`
  - Replace test App ID with production App ID
  
- [ ] **AdManager.kt** updated with production Ad Unit ID
  - File: `app/src/main/java/com/tictactoe/AdManager.kt`
  - Replace test Ad Unit ID with production Interstitial Ad Unit ID

## Testing

- [ ] Test ads work correctly (using test IDs during development)
- [ ] Production ads tested (after switching to production IDs)
- [ ] Ads load without blocking app functionality
- [ ] Ads display correctly on different devices
- [ ] App works correctly if ads fail to load
- [ ] No test ad IDs in production build

## Play Store Requirements

- [ ] Privacy policy URL created and mentions AdMob
- [ ] Privacy policy URL added in Play Console
- [ ] Data safety form completed in Play Console
  - [ ] Declared ad serving
  - [ ] Declared data collection (if applicable)

## Pre-Publishing Final Check

- [ ] Production App ID in AndroidManifest.xml (not test ID)
- [ ] Production Ad Unit ID in AdManager.kt (not test ID)
- [ ] Ads tested and working
- [ ] Privacy policy accessible and mentions AdMob
- [ ] Ready to publish!

---

## Quick Reference

**Test Ad Unit IDs** (for development):
- Interstitial: `ca-app-pub-3940256099942544/1033173712`

**Files to Update:**
1. `app/src/main/AndroidManifest.xml` - App ID
2. `app/src/main/java/com/tictactoe/AdManager.kt` - Ad Unit ID

**Important Links:**
- AdMob Console: https://apps.admob.com/
- Full Setup Guide: See `GOOGLE_ADS_SETUP.md`

---

**⚠️ Remember:**
- Use test IDs during development
- Switch to production IDs only before release
- Ad units may take 24-48 hours to be fully active
- Keep test IDs for future testing

