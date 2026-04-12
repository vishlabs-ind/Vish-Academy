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
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
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

    private var rewardedAd: RewardedAd? = null
    private var isRewardedLoading = false
    private val _isRewardedAdReady = mutableStateOf(false)
    val isRewardedAdReady: State<Boolean> = _isRewardedAdReady


    init {
        loadAd()
        loadRewardedAd()
    }


    fun loadRewardedAd() {
        if (isRewardedLoading || rewardedAd != null) {
            Log.d("AdDebug", "Rewarded ad already loading or loaded — skipping")
            return
        }

        isRewardedLoading = true
        _isRewardedAdReady.value = false
        Log.d("AdDebug", "loadRewardedAd() called")

        RewardedAd.load(
            context,
            K.REWARDED_AD_ID,  // ← add this to your K object
               AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isRewardedLoading = false
                    _isRewardedAdReady.value = true
                    Log.d("AdDebug", "✅ Rewarded ad loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isRewardedLoading = false
                    _isRewardedAdReady.value = false
                    Log.e("AdDebug", "❌ Rewarded ad failed — ${error.message}")
                }
            }
        )
    }


    // --- Show Rewarded Ad ---
     fun showRewardedAd(
        activity: Activity, onUserEarnedReward: (rewardAmount: Int) -> Unit, onAdDismissed: () -> Unit) {
    Log.d("AdDebug", "showRewardedAd() called — ready: ${_isRewardedAdReady.value}")

    if (rewardedAd == null) {
        Log.e("AdDebug", "❌ Rewarded ad not ready")
        onAdDismissed()  // proceed without reward
         return        }

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                Log.d("AdDebug", "✅ Rewarded ad showing")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d("AdDebug", "✅ Rewarded ad dismissed")
                rewardedAd = null
                _isRewardedAdReady.value = false
                onAdDismissed()
                loadRewardedAd()  // ← reload for next time
            }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    Log.e("AdDebug", "❌ Rewarded ad failed to show: ${error.message}")

                    rewardedAd = null
                    _isRewardedAdReady.value = false
                    loadRewardedAd()
                    onAdDismissed()
                }
            }

        rewardedAd?.show(activity) { rewardItem ->
            Log.d("AdDebug", "✅ User earned reward — amount: ${rewardItem.amount}, type: ${rewardItem.type}")
            onUserEarnedReward(rewardItem.amount)
        }    }


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
            K.INTERSITIAL_ID, // TEST AD UNIT
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