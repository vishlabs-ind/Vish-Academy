package com.rach.co.ad

import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.rach.co.utils.K

@Composable
fun NativeAdView(
    modifier: Modifier = Modifier,
    viewModel: AdViewModel = hiltViewModel()

) {

    val isPremium by viewModel.isPremium.collectAsState(initial = false)

    if (isPremium) {
        return
    }

    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {

        val adLoader = AdLoader.Builder(context, K.NATIVE_ADVANCE_ID_1)
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            nativeAd?.destroy()
        }
    }

    nativeAd?.let { ad ->

        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            factory = {

                val adView = NativeAdView(context)

                val headline = TextView(context)
                headline.setTextColor(android.graphics.Color.BLACK)
                headline.textSize = 16f

                val body = TextView(context)
                body.setTextColor(android.graphics.Color.GRAY)
                body.textSize = 14f

                val button = Button(context).apply {
                    setBackgroundColor(android.graphics.Color.BLUE)
                    setTextColor(android.graphics.Color.WHITE)
                }

                val layout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(16, 16, 16, 16)
                    addView(headline)
                    addView(body)
                    addView(button)
                }

                adView.addView(layout)

                adView.headlineView = headline
                adView.bodyView = body
                adView.callToActionView = button

                headline.text = ad.headline
                body.text = ad.body
                button.text = ad.callToAction
                adView.setNativeAd(ad)
                adView
            }
        )
    }
}