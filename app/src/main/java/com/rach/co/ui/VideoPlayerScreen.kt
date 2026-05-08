package com.rach.co.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideoPlayerScreen(
    navController: NavHostController,
    ytlink: String,
    pdfLink: String
) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val activity = context as Activity

    // Fullscreen state
    var isFullscreen by remember {

        mutableStateOf(false)

    }

    // Extract video id
    val videoId = remember(ytlink) {

        extractYoutubeVideoId(ytlink)

    }

    Log.d("YOUTUBE_LINK", ytlink)

    Log.d("YOUTUBE_ID", videoId.toString())

    // Invalid link
    if (videoId == null) {

        Toast.makeText(
            context,
            "Invalid Youtube Link",
            Toast.LENGTH_SHORT
        ).show()

        return
    }

    // Youtube Player
    val playerView = remember {

        YouTubePlayerView(context).apply {

            addYouTubePlayerListener(

                object : AbstractYouTubePlayerListener() {

                    override fun onReady(player: YouTubePlayer) {

                        // Load video
                        player.loadVideo(videoId, 0f)

                    }

                }
            )

        }

    }

    // Lifecycle handling
    DisposableEffect(playerView) {

        lifecycleOwner.lifecycle.addObserver(playerView)

        onDispose {

            lifecycleOwner.lifecycle.removeObserver(playerView)

            playerView.release()

        }

    }

    // Main UI
    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))

    ) {

        // Top title
        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Text(

                text = "Video Player",

                style = MaterialTheme.typography.titleLarge,

                color = Color.White

            )

        }

        // Video Card
        Card(

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),

            shape = RoundedCornerShape(18.dp)

        ) {

            Box(

                modifier = Modifier
                    .fillMaxWidth()

                    .then(

                        if (isFullscreen) {

                            Modifier.fillMaxSize()

                        } else {

                            Modifier.height(250.dp)

                        }

                    )

            ) {

                // Android Youtube View
                AndroidView(

                    modifier = Modifier.fillMaxSize(),

                    factory = {

                        playerView

                    }

                )

                // Fullscreen button
                IconButton(

                    onClick = {

                        isFullscreen = !isFullscreen

                        if (isFullscreen) {

                            activity.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                        } else {

                            activity.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                        }

                    },

                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(50)
                        )

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

        }

        // Bottom content hidden in fullscreen
        if (!isFullscreen) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

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
                    .height(58.dp),

                shape = RoundedCornerShape(16.dp)

            ) {

                Text(

                    text = "Open Notes",

                    style = MaterialTheme.typography.titleMedium

                )

            }

        }

    }

}

// Youtube Video Id Extract Function
fun extractYoutubeVideoId(url: String): String? {

    return try {

        val uri = url.toUri()

        when {

            uri.host?.contains("youtu.be") == true ->

                uri.lastPathSegment

            uri.getQueryParameter("v") != null ->

                uri.getQueryParameter("v")

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
