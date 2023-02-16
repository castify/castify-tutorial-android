package com.example.castify_tutorial_android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
inline fun <T: AutoCloseable> rememberCloseable(vararg keys: Any?, crossinline calculation: () -> T): T {
  val value = remember(calculation)
  DisposableEffect(* keys) {
    onDispose { value.close() }
  }
  return value
}
