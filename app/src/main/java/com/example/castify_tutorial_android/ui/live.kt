package com.example.castify_tutorial_android.ui

import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import jp.castify.api.*


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LiveScreen(app: CastifyApp) {
  val state = rememberMultiplePermissionsState(listOf(
    android.Manifest.permission.CAMERA,
    android.Manifest.permission.RECORD_AUDIO
  ))
  if (state.allPermissionsGranted) {
    Live(app)
  }
  else {
    Button(onClick = state::launchMultiplePermissionRequest) {
      Text("カメラ／マイクのアクセスを許可")
    }
  }
}

@Composable
fun Live(app: CastifyApp) {
  val observer = remember { MyBroadcasterObserver() }
  val broadcaster = rememberCloseable {
    app.newBroadcaster().apply {
      callback = observer
      audio = AudioSource.Microphone()
      video = VideoSource.Camera.findByFacing(VideoSource.Camera.Facing.FRONT)
      audioEncoderSetting = AudioEncoderSetting.AAC()
      videoEncoderSetting = VideoEncoderSetting.H264(bps = 1_500_000)
    }
  }
  val context = LocalContext.current
  LaunchedEffect(observer.state) {
    Toast.makeText(context, "State: ${observer.state.name}", Toast.LENGTH_SHORT).show()
  }
  AndroidView(factory = {
    SurfaceView(it).apply { broadcaster.previews.add(holder) }
  })
  var started by remember { mutableStateOf(false) }
  Button(onClick = { started = !started }) {
    Text(if (started) "Pause" else "Start")
  }
  LaunchedEffect(started) {
    if (started) {
      broadcaster.start()
    }
    else {
      broadcaster.pause()
    }
  }
}

class MyBroadcasterObserver : Broadcaster.Callback {

  var state by mutableStateOf(Broadcaster.State.Closed)

  override fun onStateChange(host: Broadcaster, state: Broadcaster.State, cause: Throwable?) {
    this.state = state
    if (cause != null) {
      Log.e("demo", "Broadcaster has stopped due to an error!", cause)
    }
  }

  var broadcast by mutableStateOf(null as Broadcast?)

  override fun onBroadcast(host: Broadcaster, broadcast: Broadcast) {
    this.broadcast = broadcast
  }
}
