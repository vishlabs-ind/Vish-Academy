package com.rach.co.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

@Composable
fun VideoPlayerScreen() {

    var videoUrl by remember { mutableStateOf("") }
    var videoId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = videoUrl,
            onValueChange = { videoUrl = it },
            label = { Text("Paste YouTube Link") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                val id = extractYoutubeVideoId(videoUrl)

                if (id == null) {
                    Toast.makeText(context, "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                } else {
                    videoId = id
                    Toast.makeText(context, "Video ID = $id", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Play Video")
        }

        Spacer(modifier = Modifier.height(16.dp))

        videoId?.let { id ->

            val playerView = remember {
                YouTubePlayerView(context)
            }

            DisposableEffect(playerView) {

                lifecycleOwner.lifecycle.addObserver(playerView)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(playerView)
                    playerView.release()
                }
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),

                factory = { playerView }
            )

            LaunchedEffect(id) {
                playerView.addYouTubePlayerListener(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(player: YouTubePlayer) {
                            player.loadVideo(id, 0f)
                        }
                    }
                )
            }
        }
    }
}


fun extractYoutubeVideoId(url: String): String? {
    return try {
        val uri = android.net.Uri.parse(url)

        when {
            uri.host?.contains("youtu.be") == true ->
                uri.lastPathSegment

            uri.getQueryParameter("v") != null ->
                uri.getQueryParameter("v")

            uri.path?.contains("embed") == true ->
                uri.lastPathSegment

            else -> null
        }
    } catch (e: Exception) {
        null
    }
}