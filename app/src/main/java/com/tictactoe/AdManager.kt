package com.tictactoe

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Manages Google Mobile Ads (AdMob) integration
 * Handles loading and showing interstitial ads
 */
class AdManager(private val activity: Activity) {
    
    companion object {
        private const val TAG = "AdManager"
        // Test Ad Unit ID - Replace with your actual Ad Unit ID from AdMob
        // Test IDs: ca-app-pub-3940256099942544/1033173712 (interstitial)
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    }
    
    private var interstitialAd: InterstitialAd? = null
    private var adLoadCallback: (() -> Unit)? = null
    
    init {
        // Initialize Mobile Ads SDK
        MobileAds.initialize(activity) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.d(TAG, "Adapter: $adapterClass, Status: ${status?.initializationState}")
            }
        }
        
        // Pre-load an ad
        loadInterstitialAd()
    }
    
    /**
     * Load an interstitial ad
     */
    fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        
        InterstitialAd.load(
            activity,
            AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded")
                    interstitialAd = ad
                    adLoadCallback?.invoke()
                    adLoadCallback = null
                    
                    // Set up the full screen content callback
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Ad dismissed")
                            interstitialAd = null
                            // Load a new ad for next time
                            loadInterstitialAd()
                        }
                        
                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            Log.e(TAG, "Ad failed to show: ${error.message}")
                            interstitialAd = null
                            // Load a new ad for next time
                            loadInterstitialAd()
                        }
                        
                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Ad showed")
                        }
                    }
                }
                
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Ad failed to load: ${error.message}, Code: ${error.code}")
                    interstitialAd = null
                    adLoadCallback = null
                }
            }
        )
    }
    
    /**
     * Show interstitial ad if available
     * @param onAdClosed Callback when ad is closed (dismissed or failed to show)
     */
    fun showInterstitialAd(onAdClosed: (() -> Unit)? = null) {
        if (interstitialAd != null) {
            interstitialAd?.show(activity)
            // Set callback for when ad is closed
            if (onAdClosed != null) {
                val currentAd = interstitialAd
                currentAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad dismissed")
                        interstitialAd = null
                        onAdClosed.invoke()
                        loadInterstitialAd()
                    }
                    
                    override fun onAdFailedToShowFullScreenContent(error: AdError) {
                        Log.e(TAG, "Ad failed to show: ${error.message}")
                        interstitialAd = null
                        onAdClosed.invoke()
                        loadInterstitialAd()
                    }
                    
                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed")
                    }
                }
            }
        } else {
            Log.d(TAG, "Ad not ready, loading new ad...")
            // If ad is not ready, wait for it to load
            adLoadCallback = {
                showInterstitialAd(onAdClosed)
            }
            loadInterstitialAd()
            // If still not ready after loading attempt, just continue
            onAdClosed?.invoke()
        }
    }
    
    /**
     * Check if an ad is ready to show
     */
    fun isAdReady(): Boolean {
        return interstitialAd != null
    }
}

