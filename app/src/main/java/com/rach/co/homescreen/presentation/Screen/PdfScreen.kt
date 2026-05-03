package com.rach.co.homescreen.presentation.Screen

import android.app.Activity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.rach.co.ad.AdViewModel
import com.rach.co.ad.BannerAdView
import com.rach.co.homescreen.presentation.home.presentation.viewmodelHome.HomeViewModel

@Composable
fun PdfScreen(pdfUrl: String ,viewModel: AdViewModel= hiltViewModel()) {

    val context = LocalContext.current




    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true

                webViewClient = WebViewClient()

                // 🔥 Google Docs viewer (works for most PDFs)
                loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfUrl")
            }
        }
    )
}