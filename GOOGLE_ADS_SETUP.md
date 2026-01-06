# Google Ads (AdMob) Setup Guide

This guide will help you set up and optimize Google Ads (AdMob) for your TicTacToe app.

## Current Implementation Status

✅ **Already Integrated:**
- Google Mobile Ads SDK (v22.6.0) added to dependencies
- `AdManager` class implemented with interstitial ad support
- Ads shown after each game ends (win or draw)
- ProGuard rules configured for release builds
- Test Ad Unit IDs currently in use

## Table of Contents

1. [AdMob Account Setup](#admob-account-setup)
2. [Creating Ad Units](#creating-ad-units)
3. [Updating Your App with Production IDs](#updating-your-app-with-production-ids)
4. [Ad Types Available](#ad-types-available)
5. [Best Practices & Optimization](#best-practices--optimization)
6. [Testing Ads](#testing-ads)
7. [Revenue Optimization Tips](#revenue-optimization-tips)
8. [Troubleshooting](#troubleshooting)

---

## AdMob Account Setup

### Step 1: Create AdMob Account

1. **Visit AdMob Console:**
   - Go to https://apps.admob.com/
   - Sign in with your Google account (use the same account as Play Console)

2. **Complete Registration:**
   - Accept terms and conditions
   - Provide payment information (for receiving ad revenue)
   - Verify your account (may require phone verification)

### Step 2: Add Your App to AdMob

1. **Click "Apps" in the left sidebar**
2. **Click "Add app"**
3. **Select platform:** Android
4. **App name:** "Tic Tac Toe" (or your app name)
5. **Package name:** `com.tictactoe` (must match your app's package name)
6. **Click "Add app"**

**Important:** The package name must exactly match your `applicationId` in `build.gradle.kts`

### Step 3: Get Your App ID

After adding your app, AdMob will provide an **App ID** in this format:
```
ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX
```

**Save this App ID** - you'll need it in the next step.

---

## Creating Ad Units

### Interstitial Ad Unit (Currently Used)

1. **In AdMob Console, go to your app**
2. **Click "Ad units" tab**
3. **Click "Add ad unit"**
4. **Select ad format:** Interstitial
5. **Ad unit name:** "Game Over Interstitial" (or any name you prefer)
6. **Click "Create ad unit"**
7. **Copy the Ad Unit ID** (format: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`)

### Optional: Banner Ad Unit

If you want to add banner ads to your main menu:

1. **Click "Add ad unit"**
2. **Select ad format:** Banner
3. **Ad unit name:** "Main Menu Banner"
4. **Ad size:** Standard (320x50) or Adaptive
5. **Click "Create ad unit"**
6. **Copy the Ad Unit ID**

### Optional: Rewarded Ad Unit

For future features (e.g., remove ads, extra features):

1. **Click "Add ad unit"**
2. **Select ad format:** Rewarded
3. **Ad unit name:** "Rewarded Ad"
4. **Click "Create ad unit"**
5. **Copy the Ad Unit ID**

---

## Updating Your App with Production IDs

### Step 1: Update AndroidManifest.xml

1. **Open:** `app/src/main/AndroidManifest.xml`
2. **Find the AdMob App ID meta-data:**
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-3940256099942544~3347511713" />
   ```
3. **Replace with your production App ID:**
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX" />
   ```

### Step 2: Update AdManager.kt

1. **Open:** `app/src/main/java/com/tictactoe/AdManager.kt`
2. **Find the AD_UNIT_ID constant:**
   ```kotlin
   private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
   ```
3. **Replace with your production Interstitial Ad Unit ID:**
   ```kotlin
   private const val AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
   ```

### Step 3: Verify Build Configuration

Ensure your `app/build.gradle.kts` has the Google Mobile Ads SDK:

```kotlin
dependencies {
    // Google Mobile Ads SDK
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    // ... other dependencies
}
```

---

## Ad Types Available

### Currently Implemented: Interstitial Ads

**What they are:**
- Full-screen ads shown between content
- Currently shown after each game ends

**Best practices:**
- ✅ Show after natural break points (game ends)
- ✅ Don't show too frequently (users may uninstall)
- ✅ Pre-load ads for smooth experience (already implemented)

### Optional: Banner Ads

**What they are:**
- Small rectangular ads at top or bottom of screen
- Always visible while content is shown

**Implementation example:**
```kotlin
// In MainActivity or GameActivity
private lateinit var adView: AdView

private fun loadBannerAd() {
    adView = findViewById(R.id.adView)
    val adRequest = AdRequest.Builder().build()
    adView.loadAd(adRequest)
}
```

**Layout XML:**
```xml
<com.google.android.gms.ads.AdView
    android:id="@+id/adView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal|bottom"
    app:adSize="BANNER"
    app:adUnitId="ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX" />
```

### Optional: Rewarded Ads

**What they are:**
- Full-screen ads users can watch for rewards
- Good for: removing ads, unlocking features, extra lives

**Use cases for TicTacToe:**
- Remove ads for 1 hour
- Unlock different game modes
- Customize game appearance

---

## Best Practices & Optimization

### 1. Ad Frequency

**Current implementation:** Shows ad after every game
- ✅ Good for initial monetization
- ⚠️ Consider: Show ad every 2-3 games to improve user experience

**Recommended improvement:**
```kotlin
private var gamesPlayed = 0
private val AD_FREQUENCY = 2 // Show ad every 2 games

private fun checkGameResult() {
    // ... existing code ...
    when (result) {
        is GameResult.Win, is GameResult.Draw -> {
            gamesPlayed++
            if (gamesPlayed >= AD_FREQUENCY) {
                adManager.showInterstitialAd()
                gamesPlayed = 0
            }
        }
        // ...
    }
}
```

### 2. Ad Loading Strategy

**Current implementation:** ✅ Good
- Pre-loads ads in `AdManager` init
- Loads new ad after current one is dismissed
- Handles ad failures gracefully

### 3. User Experience

**Do:**
- ✅ Show ads at natural break points (game ends)
- ✅ Pre-load ads to avoid delays
- ✅ Handle ad failures gracefully (don't block user)
- ✅ Test ads thoroughly before release

**Don't:**
- ❌ Show ads too frequently
- ❌ Show ads during gameplay
- ❌ Block user actions waiting for ads
- ❌ Use test ads in production

### 4. Privacy & Compliance

**Required:**
- ✅ Privacy policy URL (required for Play Store)
- ✅ Data safety declaration in Play Console
- ✅ GDPR compliance (if targeting EU users)

**Privacy Policy should mention:**
- That you use Google AdMob
- What data is collected (AdMob collects device info, location, etc.)
- How data is used
- User rights

---

## Testing Ads

### Test Ad Unit IDs

AdMob provides test ad unit IDs that always return test ads:

**Interstitial:**
```
ca-app-pub-3940256099942544/1033173712
```

**Banner:**
```
ca-app-pub-3940256099942544/6300978111
```

**Rewarded:**
```
ca-app-pub-3940256099942544/5224354917
```

### Testing Checklist

Before publishing:

- [ ] Test ads load correctly
- [ ] Test ads display properly
- [ ] Test ad dismissal works
- [ ] Test app continues normally after ad
- [ ] Test on different devices
- [ ] Test with no internet (graceful failure)
- [ ] Verify production Ad Unit IDs are used (not test IDs)
- [ ] Test ad frequency is acceptable

### Testing in Production

1. **Use test devices:**
   - Add your device's advertising ID to AdMob test devices
   - Go to AdMob → Settings → Test devices
   - Add your device ID

2. **Use test ad unit IDs during development:**
   - Keep test IDs while developing
   - Switch to production IDs only before release

---

## Revenue Optimization Tips

### 1. Ad Placement

**Current:** After each game ✅
- Good placement for interstitials
- Natural break point

**Consider:**
- Add banner on main menu (continuous revenue)
- Add rewarded ads for premium features

### 2. Ad Mediation

**What it is:**
- AdMob automatically uses multiple ad networks
- Higher fill rates = more revenue

**Setup:**
1. In AdMob Console, go to "Mediation"
2. Create mediation group
3. Add ad networks (AdMob does this automatically)
4. Enable for your ad units

### 3. eCPM Optimization

**Factors affecting revenue:**
- User location (US/UK users = higher rates)
- Ad format (interstitials > banners)
- Time of day
- User engagement

**Tips:**
- Target engaged users (they click more)
- Use interstitials (higher eCPM than banners)
- Consider rewarded ads (users choose to watch)

### 4. User Retention

**Better retention = more ad views:**
- Make game fun and engaging
- Don't show ads too frequently
- Consider ad-free option (via in-app purchase)

### 5. Analytics

**Track in AdMob Console:**
- Ad requests
- Impressions
- Clicks
- Revenue
- eCPM (earnings per 1000 impressions)

**Monitor:**
- Which ad units perform best
- User engagement patterns
- Revenue trends

---

## Troubleshooting

### Ads Not Showing

**Check:**
1. ✅ Internet connection
2. ✅ Ad Unit IDs are correct (not test IDs in production)
3. ✅ App ID in AndroidManifest.xml is correct
4. ✅ AdMob account is active
5. ✅ Ad units are approved (can take 24-48 hours)
6. ✅ Check Logcat for error messages

**Common errors:**
```
ERROR_CODE_NETWORK_ERROR (2)
→ Check internet connection

ERROR_CODE_NO_FILL (3)
→ No ads available (normal, especially for new apps)

ERROR_CODE_INTERNAL_ERROR (8)
→ AdMob SDK issue, usually temporary
```

### Ads Showing Test Ads in Production

**Problem:** Test ads showing after release

**Solution:**
1. Verify production Ad Unit IDs are in code
2. Check AndroidManifest.xml has production App ID
3. Rebuild and reinstall app
4. Wait a few minutes for changes to propagate

### Low Revenue

**Possible causes:**
- New app (low traffic = low revenue)
- Low fill rate (not enough ads available)
- Users in low-value regions
- Too few ad impressions

**Solutions:**
- Wait for more users (revenue increases with traffic)
- Enable ad mediation
- Consider additional ad placements
- Improve user retention

### Ad Approval Delays

**Timeline:**
- New ad units: Usually instant
- New apps: 24-48 hours
- Policy violations: Can take longer

**What to do:**
- Use test ads during development
- Submit app for review early
- Check AdMob policy compliance

---

## Quick Reference

### File Locations

**Update these files with production IDs:**

1. **AndroidManifest.xml:**
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="YOUR_APP_ID" />
   ```

2. **AdManager.kt:**
   ```kotlin
   private const val AD_UNIT_ID = "YOUR_INTERSTITIAL_AD_UNIT_ID"
   ```

### Test Ad Unit IDs

Keep these for testing:

- **Interstitial:** `ca-app-pub-3940256099942544/1033173712`
- **Banner:** `ca-app-pub-3940256099942544/6300978111`
- **Rewarded:** `ca-app-pub-3940256099942544/5224354917`

### Important Links

- **AdMob Console:** https://apps.admob.com/
- **AdMob Help:** https://support.google.com/admob
- **AdMob Policies:** https://support.google.com/admob/answer/6128543
- **SDK Documentation:** https://developers.google.com/admob/android

---

## Pre-Publishing Checklist

Before releasing to Play Store:

- [ ] AdMob account created and verified
- [ ] App added to AdMob with correct package name
- [ ] Production App ID added to AndroidManifest.xml
- [ ] Production Ad Unit IDs added to AdManager.kt
- [ ] Test ads work correctly
- [ ] Privacy policy mentions AdMob
- [ ] Data safety form completed in Play Console
- [ ] Ads tested on multiple devices
- [ ] No test ad IDs in production build
- [ ] Ad frequency is acceptable
- [ ] App works correctly if ads fail to load

---

## Next Steps

1. **Set up AdMob account** (if not done)
2. **Create ad units** in AdMob Console
3. **Update production IDs** in your app
4. **Test thoroughly** before release
5. **Monitor revenue** after launch
6. **Optimize** based on analytics

Good luck with monetization! 🚀💰

