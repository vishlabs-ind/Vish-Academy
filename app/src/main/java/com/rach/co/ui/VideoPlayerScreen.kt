package com.rach.co.ui

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideoPlayerScreen(navController: NavHostController, ytlink: String, pdfLink: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Video ID extract
    val videoId = remember(ytlink) {
        extractYoutubeVideoId(ytlink)
    }

    Log.d("YouTubePlayer", "Link: $ytlink | Extracted ID: $videoId")

    if (videoId == null) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Invalid YouTube link", Toast.LENGTH_SHORT).show()
        }
        return
    }

    // PlayerView create + listener turant add (timing fix yahin hai)
    val playerView = remember {
        YouTubePlayerView(context).apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(player: YouTubePlayer) {
                    Log.d("YouTubePlayer", "Player Ready → Loading video: $videoId")
                    player.loadVideo(videoId, 0f)
                }
            })
        }
    }

    // Lifecycle (pause/resume ke liye zaroori)
    DisposableEffect(playerView) {
        lifecycleOwner.lifecycle.addObserver(playerView)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(playerView)
            playerView.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { playerView }
        )


        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                if (pdfLink.isEmpty()) {
                    Toast.makeText(context, "Notes not available", Toast.LENGTH_SHORT).show()
                } else {
                    val encodedPdf = Uri.encode(pdfLink)

                    navController.navigate("pdfscreen/$encodedPdf")

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text("Open Notes")
        }    }
}


fun extractYoutubeVideoId(url: String): String? {
    return try {
        val uri = url.toUri()
        when {
            uri.host?.contains("youtu.be") == true -> uri.lastPathSegment
            uri.getQueryParameter("v") != null -> uri.getQueryParameter("v")
            uri.path?.contains("/shorts/") == true ||
                    uri.path?.contains("/embed/") == true -> {
                uri.lastPathSegment?.takeIf { it.length > 8 } // shorts ID usually 11 chars
            }

            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

