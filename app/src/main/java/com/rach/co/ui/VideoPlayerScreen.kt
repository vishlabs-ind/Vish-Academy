package com.rach.co.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.rach.co.ad.NativeAdView

@Composable
fun VideoPlayerScreen(
    navController: NavHostController,
    ytlink: String,
    pdfLink: String
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val activity = context as? Activity

    // Fullscreen state
    var isFullscreen by remember {
        mutableStateOf(false)
    }

    // Loading state
    var isLoading by remember {
        mutableStateOf(true)
    }

    // Extract video id
    val videoId = remember(ytlink) {
        extractYoutubeVideoId(ytlink)
    }

    Log.d("YouTubePlayer", "Link: $ytlink")
    Log.d("YouTubePlayer", "VideoId: $videoId")

    // Invalid Link
    if (videoId == null) {

        LaunchedEffect(Unit) {
            Toast.makeText(
                context,
                "Invalid YouTube Link",
                Toast.LENGTH_SHORT
            ).show()
        }

        return
    }

    // Youtube Player
    val playerView = remember(videoId) {

        YouTubePlayerView(context).apply {

            addYouTubePlayerListener(

                object : AbstractYouTubePlayerListener() {

                    override fun onReady(player: YouTubePlayer) {

                        isLoading = false

                        // Better than autoplay
                        player.cueVideo(videoId, 0f)

                    }
                }

            )
        }
    }

    // Lifecycle handling
    DisposableEffect(playerView) {

        lifecycleOwner.lifecycle.addObserver(playerView)

        val observer = LifecycleEventObserver { _, event ->

            when (event) {

                Lifecycle.Event.ON_PAUSE -> {
                    playerView.release()
                }

                else -> Unit
            }

        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {

            lifecycleOwner.lifecycle.removeObserver(playerView)

            lifecycleOwner.lifecycle.removeObserver(observer)

            // Reset orientation
            activity?.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            playerView.release()
        }
    }

    // Back Press Handle
    BackHandler(isFullscreen) {

        isFullscreen = false

        activity?.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    ) {

        // Video Section
        Box(

            modifier = Modifier
                .fillMaxWidth()

                .then(

                    if (isFullscreen) {

                        Modifier.fillMaxSize()

                    } else {

                        Modifier.height(260.dp)

                    }

                )

        ) {

            // Youtube View
            AndroidView(

                modifier = Modifier.fillMaxSize(),

                factory = {
                    playerView
                }

            )

            // Loading
            if (isLoading) {

                CircularProgressIndicator(

                    modifier = Modifier.align(Alignment.Center),

                    color = Color.White

                )
            }

            // Fullscreen Button
            IconButton(

                onClick = {

                    isFullscreen = !isFullscreen

                    activity?.requestedOrientation =

                        if (isFullscreen) {

                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                        } else {

                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                        }

                },

                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)

            ) {

                Icon(

                    imageVector =

                        if (isFullscreen)

                            Icons.Default.FullscreenExit

                        else

                            Icons.Default.Fullscreen,

                    contentDescription = null,

                    tint = Color.White

                )

            }

        }

        // Hide Bottom UI in Fullscreen
        if (!isFullscreen) {

            Spacer(modifier = Modifier.height(20.dp))

            // Notes Button
            Button(

                onClick = {

                    if (pdfLink.isEmpty()) {

                        Toast.makeText(
                            context,
                            "Notes not available",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        val encodedPdf = Uri.encode(pdfLink)

                        navController.navigate(
                            "pdfscreen/$encodedPdf"
                        )

                    }

                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .height(55.dp),

                shape = RoundedCornerShape(10.dp)

            ) {

                Text("Open Notes")

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Native Ad
            NativeAdView()

        }

    }

}

// Extract Youtube Video ID
fun extractYoutubeVideoId(url: String): String? {

    return try {

        val uri = url.toUri()

        when {

            uri.host?.contains("youtu.be") == true -> {

                uri.lastPathSegment
            }

            uri.getQueryParameter("v") != null -> {

                uri.getQueryParameter("v")
            }

            uri.path?.contains("/shorts/") == true ||
                    uri.path?.contains("/embed/") == true -> {

                uri.lastPathSegment
            }

            else -> null
        }

    } catch (e: Exception) {

        null
    }
}