package com.example.castify_tutorial_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.castify_tutorial_android.ui.LiveScreen
import com.example.castify_tutorial_android.ui.PlayScreen
import com.example.castify_tutorial_android.ui.theme.CastifyTutorialTheme
import jp.castify.api.CastifyApp

class MainActivity : ComponentActivity() {

  companion object {
    private val apiToken: String = TODO()
  }

  private val castifyApp by lazy {
    CastifyApp(apiToken, applicationContext)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CastifyTutorialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "top") {
          composable("top") {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
              Button(onClick = { navController.navigate("live") }) {
                Text("ライブ配信のサンプル")
              }
              Button(onClick = { navController.navigate("play") }) {
                Text("プレイヤーのサンプル")
              }
            }
          }
          composable("live") { LiveScreen(castifyApp) }
          composable("play") { PlayScreen(castifyApp) }
        }
      }
    }
  }
}
