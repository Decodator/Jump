package com.ipn.jump

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Nivel(modifier: Modifier = Modifier){
    Row(modifier) {
        Text(
            text = "Nivel",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}