package com.example.ipn.session

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ipn.SessionScreen
import com.example.ipn.SessionVM
import com.example.ipn.ui.theme.IPNTheme
import kotlinx.coroutines.delay

class SessionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val path = intent.getStringExtra("path")!!
        setContent {
            IPNTheme {
                var sessionState by remember { mutableStateOf(SessionState.LOADING) }
                val viewModel: SessionVM = viewModel()
                viewModel.updatePath(path)
                viewModel.retrieveCards()

                when (sessionState) {
                    SessionState.LOADING -> {
                        LoadingScreen()
                        LaunchedEffect(key1 = "loading") {
                            delay(2000)
                            sessionState = SessionState.RUNNING
                        }
                    }

                    SessionState.RUNNING -> {
                        SessionScreen(viewModel, onClose = { finish() },
                            onEnding = { sessionState = SessionState.ENDING })
                    }

                    SessionState.ENDING -> {
                        EndingScreen()
                    }
                }
            }
        }
    }
}