package com.example.screenrecorder.ads

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

class AdManager(private val activity: Activity) {
    
    private var bannerAd: AppCompatTextView? = null
    private var adCallback: AdCallback? = null
    
    fun initializeAds() {
        Log.d("AdManager", "Ads initialized (Media.net integration ready)")
    }
    
    fun loadBannerAd(container: ViewGroup): Boolean {
        return try {
            bannerAd = AppCompatTextView(activity).apply {
                text = "[ Ads ]"
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    50
                )
                setBackgroundColor(android.graphics.Color.LTGRAY)
                setTextColor(android.graphics.Color.BLACK)
                gravity = android.view.Gravity.CENTER
                textSize = 12f
            }
            
            container.removeAllViews()
            container.addView(bannerAd)
            
            adCallback?.onAdLoaded()
            true
        } catch (e: Exception) {
            Log.e("AdManager", "Error loading banner ad", e)
            adCallback?.onAdFailedToLoad(e.message ?: "Unknown error")
            false
        }
    }
    
    fun showInterstitialAd(callback: (Boolean) -> Unit) {
        try {
            Log.d("AdManager", "Showing interstitial ad")
            adCallback?.onAdShown()
            callback(true)
        } catch (e: Exception) {
            Log.e("AdManager", "Error showing interstitial", e)
            callback(false)
        }
    }
    
    fun setAdCallback(callback: AdCallback) {
        adCallback = callback
    }
    
    fun destroyAds() {
        try {
            bannerAd?.let { ad ->
                (ad.parent as? ViewGroup)?.removeView(ad)
            }
            bannerAd = null
            Log.d("AdManager", "Ads destroyed")
        } catch (e: Exception) {
            Log.e("AdManager", "Error destroying ads", e)
        }
    }
    
    interface AdCallback {
        fun onAdLoaded()
        fun onAdFailedToLoad(error: String)
        fun onAdShown()
        fun onAdDismissed()
    }
}
