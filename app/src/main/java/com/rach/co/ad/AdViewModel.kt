package com.rach.co.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rach.co.utils.K
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor(@ApplicationContext private val context: Context)
    : ViewModel(){

    var interstitialAd: InterstitialAd? = null
    private var isLoading = false  // ← prevents duplicate load calls

    // ← NEW: tracks if ad is currently loading
    private val _isAdReady = mutableStateOf(false)
    val isAdReady: State<Boolean> = _isAdReady


    init {
        loadAd()
    }

    fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        if (isLoading || interstitialAd != null) {
            Log.d("AdDebug", "Ad already loading or loaded — skipping")
            return
        }

        isLoading = true
        _isAdReady.value = false
        Log.d("AdDebug", "loadAd() called")

        InterstitialAd.load(
            context,
            K.AD_ID, // TEST AD UNIT
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("AdMob", "Ad Loaded Successfully")
                    interstitialAd = ad
                    isLoading = false
                    _isAdReady.value = true   // ← ad is now ready
                    Log.d("AdDebug", "✅ Ad loaded successfully")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d("AdMob", "Ad Failed: ${error.message}")
                    interstitialAd = null
                    isLoading = false
                    _isAdReady.value = false   // ← ad is now ready
                    Log.e("AdDebug", "❌ Ad failed — code: ${error.code}, msg: ${error.message}")
                }
            }
        )
    }

    // show Ad
//    fun showAd(activity: Activity, onAdClosed: () -> Unit) {
//
//        if (interstitialAd != null) {
//
//            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//
//                override fun onAdDismissedFullScreenContent() {
//                    interstitialAd = null
//                    onAdClosed()
//                }
//
//                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                    onAdClosed()
//                }
//            }
//
//            interstitialAd?.show(activity)
//
//        } else {
//            onAdClosed()
//        }
//    }
    fun showAd(activity: Activity, onAdClosed: () -> Unit) {
        Log.d("AdDebug", "showAd() called — adReady: ${_isAdReady.value}")

        if (interstitialAd == null) {
            Log.e("AdDebug", "❌ Ad not ready — navigating directly")
            onAdClosed()
            return
        }

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                Log.d("AdDebug", "✅ Ad dismissed")
                interstitialAd = null
                _isAdReady.value = false
                onAdClosed()
                loadAd()   // ← reload for next tap
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("AdDebug", "❌ Ad failed to show: ${error.message}")
                onAdClosed()
            }
        }

        interstitialAd?.show(activity)
    }
}