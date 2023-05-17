package com.ipn.jump

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ipn.jump.data.CardPriorities

@Composable
fun CardStatus(cardPriorities: CardPriorities){
    Row {
       Text(
           text = "00",
           color = Color.Blue,
           modifier = Modifier.padding(end = 6.dp)
       )
        Text(
            text = "00",
            color = Color.Red,
            modifier = Modifier.padding(end = 6.dp)
        )
        Text(
            text = "00",
            color = Color.Yellow,
            modifier = Modifier.padding(end = 6.dp)
        )
        Text(
            text = "00",
            color = Color.Green,
            modifier = Modifier.padding(end = 6.dp)
        )
    }
}