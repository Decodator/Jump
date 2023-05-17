package com.ipn.jump

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ListItem
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BrowserScreen(viewModel: VM, onClick: (String) -> Unit){
    val cards by viewModel.cardsPreview.collectAsStateWithLifecycle()
    LazyColumn{
        item{
            Text("Total: ${cards.size}", modifier = Modifier.padding(start = 12.dp))
        }
        items(cards){ card ->
            ListItem (
                headlineContent = { Text(card.pregunta) },
                trailingContent = {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Editar",
                    )
                },
                modifier = Modifier.clickable { onClick(card.id) }
            )
            Divider()
        }
    }
}