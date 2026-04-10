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
import com.rach.co.utils.K

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
                adUnitId = K.BANNER_ID

                loadAd(AdRequest.Builder().build())
            }
        }
    )
}