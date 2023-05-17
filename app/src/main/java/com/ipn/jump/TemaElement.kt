package com.ipn.jump

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ipn.jump.data.Tema

@Composable
fun TemaElement(
    tema: Tema,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = Modifier
            .clip(RoundedCornerShape(10))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick(tema.path) }
        ) {
            Text(
                text = tema.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            )
            //Display them in alphabetical order
            val sortedSubtemas = tema.subtemas.toList().sortedBy{ it.second }.toList()
            sortedSubtemas.forEach{ pair ->
                val absolutePath = "${tema.path}/subtemas/${pair.first}"
                SubtemaCard(pair.second, { onClick(absolutePath)} ) { onLongClick("${tema.path}/subtemas/${pair.first}") }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SubtemaCard(
    subtema: String,
    onSubtemaClicked: () -> Unit,
    onLongClick: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .combinedClickable(
                onClick = onSubtemaClicked ,
                onLongClick = onLongClick
            )
            .fillMaxWidth()
    ) {
        Icon(painter = painterResource(R.drawable.play_circle),
            contentDescription = "Estudiar",
            modifier = Modifier.size(36.dp).padding(start = 12.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = subtema,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}