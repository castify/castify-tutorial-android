package com.example.castify_tutorial_android.ui

import android.view.SurfaceView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import jp.castify.api.CastifyApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(app: CastifyApp) {
  val player = rememberCloseable { app.newPlayer() }
  var played by remember { mutableStateOf(false) }
  var broadcastId by remember { mutableStateOf("") }
  LaunchedEffect(played) {
    if (!played) {
      player.play(null)
      return@LaunchedEffect
    }
    val source = app.newSource(broadcastId)
    val metadata = source.load()
    if (metadata.stoppedAt == null) { // live
      player.play(source)
    }
    else {
      player.play(source, time = 0.0) // archive
    }
  }
  Column {
    Row {
      Button(onClick = { played = !played }) {
        Text(if (played) "Stop" else "Play")
      }
      TextField(value = broadcastId, onValueChange = { broadcastId = it }, placeholder = { Text("bc_xxx") })
    }
    AndroidView(factory = { SurfaceView(it).apply { player.previews.add(holder) } })
  }
}
