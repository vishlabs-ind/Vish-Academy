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
import javax.inject.Inject  // ✅ correct import

@HiltViewModel
class AdViewModel @Inject constructor(
    @ApplicationContext private val context: Context // ✅ Hilt provides this automatically
) : ViewModel() {

    // --- Ad 1 — for Exam/Course section ---
    private var interstitialAd1: InterstitialAd? = null
    private var isAd1Loading = false
    private val _isAd1Ready = mutableStateOf(false)
    val isAd1Ready: State<Boolean> = _isAd1Ready

    // --- Ad 2 — for Quiz section ---
    private var interstitialAd2: InterstitialAd? = null
    private var isAd2Loading = false
    private val _isAd2Ready = mutableStateOf(false)
    val isAd2Ready: State<Boolean> = _isAd2Ready

    // --- Rewarded Ad — for QuizCourseScreen (course selection) ---
    private var rewardedAd: RewardedAd? = null
    private var isRewardedLoading = false
    private val _isRewardedAdReady = mutableStateOf(false)
    val isRewardedAdReady: State<Boolean> = _isRewardedAdReady


    init {
        // ✅ Start loading everything immediately when the app starts
        loadAd(context)
        loadAd2(context)
        loadRewardedAd(context)
    }


    // ─────────────────────────────────────────
    // Ad 1  (Exam / Course section)
    // ─────────────────────────────────────────

    fun loadAd(context: Context) {
        if (isAd1Loading || interstitialAd1 != null) {
            Log.d("AdMob", "Ad1 already loading or loaded — skipping")
            return
        }
        isAd1Loading = true
        _isAd1Ready.value = false
        Log.d("AdMob", "loadAd1() called")

        InterstitialAd.load(
            context,
            K.INTERSITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd1 = ad
                    isAd1Loading = false
                    _isAd1Ready.value = true
                    Log.d("AdMob", "✅ Ad1 loaded")
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd1 = null
                    isAd1Loading = false
                    _isAd1Ready.value = false
                    Log.e("AdMob", "❌ Ad1 failed: ${error.message}")
                }
            }
        )
    }

    fun showAd(activity: Activity, onAdClosed: () -> Unit) {
        Log.d("AdMob", "showAd1() called — ready: ${_isAd1Ready.value}")

        if (interstitialAd1 == null) {
            Log.e("AdMob", "❌ Ad1 not ready — proceeding")
            onAdClosed()
            return
        }

        interstitialAd1?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd1 = null
                _isAd1Ready.value = false
                onAdClosed()
                loadAd(activity)  // ✅ reload Ad1 for next time
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("AdMob", "❌ Ad1 failed to show: ${error.message}")
                onAdClosed()
            }
        }
        interstitialAd1?.show(activity)
    }


    // ─────────────────────────────────────────
    // Ad 2  (Quiz section)
    // ─────────────────────────────────────────

    fun loadAd2(context: Context) {
        if (isAd2Loading || interstitialAd2 != null) {
            Log.d("AdMob", "Ad2 already loading or loaded — skipping")
            return
        }
        isAd2Loading = true
        _isAd2Ready.value = false
        Log.d("AdMob", "loadAd2() called")

        InterstitialAd.load(
            context,
            K.INTERSITIAL_ID_QUIZ_SECTION,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd2 = ad
                    isAd2Loading = false
                    _isAd2Ready.value = true
                    Log.d("AdMob", "✅ Ad2 loaded")
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd2 = null
                    isAd2Loading = false
                    _isAd2Ready.value = false
                    Log.e("AdMob", "❌ Ad2 failed: ${error.message}")
                }
            }
        )
    }

    fun showAd2(activity: Activity, onAdClosed: () -> Unit) {
        Log.d("AdMob", "showAd2() called — ready: ${_isAd2Ready.value}")

        if (interstitialAd2 == null) {
            Log.e("AdMob", "❌ Ad2 not ready — proceeding")
            onAdClosed()
            return
        }

        interstitialAd2?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd2 = null
                _isAd2Ready.value = false
                onAdClosed()
                loadAd2(activity)  // ✅ reload Ad2 for next time
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("AdMob", "❌ Ad2 failed to show: ${error.message}")
                onAdClosed()
            }
        }
        interstitialAd2?.show(activity)
    }


    // ─────────────────────────────────────────
    // Rewarded Ad  (QuizCourseScreen)
    // ─────────────────────────────────────────

    fun loadRewardedAd(ctx: Context = context) {
        if (isRewardedLoading || rewardedAd != null) {
            Log.d("AdDebug", "Rewarded ad already loading or loaded — skipping")
            return
        }
        isRewardedLoading = true
        _isRewardedAdReady.value = false
        Log.d("AdDebug", "loadRewardedAd() called")

        RewardedAd.load(
            ctx,
            K.REWARDED_AD_ID,
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

    fun showRewardedAd(
        activity: Activity,
        onUserEarnedReward: (rewardAmount: Int) -> Unit,
        onAdDismissed: () -> Unit
    ) {
        Log.d("AdDebug", "showRewardedAd() called — ready: ${_isRewardedAdReady.value}")

        if (rewardedAd == null) {
            Log.e("AdDebug", "❌ Rewarded ad not ready")
            onAdDismissed()  // proceed without reward
            return
        }

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                Log.d("AdDebug", "✅ Rewarded ad showing")
            }
            override fun onAdDismissedFullScreenContent() {
                Log.d("AdDebug", "✅ Rewarded ad dismissed")
                rewardedAd = null
                _isRewardedAdReady.value = false
                onAdDismissed()
                loadRewardedAd(activity)  // ✅ reload for next time
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e("AdDebug", "❌ Rewarded ad failed to show: ${error.message}")
                rewardedAd = null
                _isRewardedAdReady.value = false
                loadRewardedAd(activity)
                onAdDismissed()
            }
        }

        rewardedAd?.show(activity) { rewardItem ->
            Log.d("AdDebug", "✅ User earned reward — amount: ${rewardItem.amount}, type: ${rewardItem.type}")
            onUserEarnedReward(rewardItem.amount)
        }
    }
}