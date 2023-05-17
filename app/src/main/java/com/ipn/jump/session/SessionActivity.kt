package com.ipn.jump.session

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipn.jump.SessionScreen
import com.ipn.jump.SessionVM
import com.ipn.jump.ui.theme.JumpTheme
import kotlinx.coroutines.delay

class SessionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val path = intent.getStringExtra("path")!!
        setContent {
            JumpTheme {
                var sessionState by remember { mutableStateOf(SessionState.LOADING) }
                Log.i("Session", sessionState.toString())
                val viewModel: SessionVM = viewModel()
                viewModel.updatePath(path)
                viewModel.retrieveCards()

                when (sessionState) {
                    SessionState.LOADING -> {
                        LoadingScreen()
                        LaunchedEffect(key1 = "loading") {
                            delay(1500)
                            sessionState = SessionState.RUNNING
                        }
                    }

                    SessionState.RUNNING -> {
                        SessionScreen(viewModel,
                            onClose = { finish() },
                            onEnding = { sessionState = SessionState.ENDING }
                        )
                    }

                    SessionState.ENDING -> {
                        EndingScreen( onClose = { finish() })
                    }
                }
            }
        }
    }
}