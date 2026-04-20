package com.rach.co.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rach.co.auth.data.Model.UserPrefs
import com.rach.co.utils.K
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AdViewModel @Inject constructor(
    private val userPrefs: UserPrefs
) : ViewModel() {

    var interstitialAd: InterstitialAd? = null
    var isPremium = userPrefs.isPremium

    private var premiumUser: Boolean = false

    init {
        viewModelScope.launch {
            userPrefs.isPremium.collect {
                premiumUser = it
            }
        }
    }

    fun loadAd(context: Context) {


            val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            K.INTERSITIAL_ID,
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

        if (premiumUser) {
            onAdClosed()  // 🚫 SKIP AD
            return
        }

        if (interstitialAd != null) {

            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        interstitialAd = null
                        loadAd(activity)   // ✅ preload next ad
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

    fun loadAd2(context: Context) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            K.INTERSITIAL_ID_QUIZ_SECTION,
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
    fun showAd2(activity: Activity, onAdClosed: () -> Unit) {

        if (premiumUser) {
            onAdClosed()  // 🚫 SKIP AD
            return
        }

        if (interstitialAd != null) {

            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        interstitialAd = null
                        loadAd(activity)   // ✅ preload next ad
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