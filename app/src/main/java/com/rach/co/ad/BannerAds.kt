package com.rach.co.ad

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView() {

    val context = LocalContext.current

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // standard banner height
        factory = {

            AdView(it).apply {
                setAdSize(AdSize.BANNER)

                // ✅ TEST Banner Ad ID
                adUnitId = "ca-app-pub-3940256099942544/6300978111"

                loadAd(AdRequest.Builder().build())
            }
        }
    )
}