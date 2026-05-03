package com.rach.co.homescreen.presentation.Screen

import android.app.Activity
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PdfScreen(pdfUrl: String) {

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