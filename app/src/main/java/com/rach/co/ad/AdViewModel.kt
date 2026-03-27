package com.rach.co.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rach.co.utils.K
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor() : ViewModel(){

    var interstitialAd: InterstitialAd? = null

    fun loadAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            K.INTERSITIAL_ID, // TEST AD UNIT
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("AdMob", "Ad Loaded Successfully")
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d("AdMob", "Ad Failed: ${error.message}")
                    interstitialAd = null
                }
            }
        )
    }

    // show Ad
    fun showAd(activity: Activity, onAdClosed: () -> Unit) {

        if (interstitialAd != null) {

            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        interstitialAd = null
                        onAdClosed()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        onAdClosed()
                    }
                }

            interstitialAd?.show(activity)

        } else {
            onAdClosed()
        }
    }
}