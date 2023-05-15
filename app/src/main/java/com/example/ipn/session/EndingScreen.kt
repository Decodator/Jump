package com.example.ipn.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EndingScreen() {
    Column(
        Modifier
            .padding(all = 16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Felicidades!", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Has completado una sesión de estudio",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}