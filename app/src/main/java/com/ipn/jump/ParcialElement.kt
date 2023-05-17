package com.ipn.jump

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ipn.jump.data.Parcial

@Composable
fun ParcialElement(
    parcial: Parcial,
    onParcialClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onParcialClicked(parcial.id) }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(),
        ) {
            AsyncImage(
                model = parcial.img_url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(10))
            )
        }
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = getParcialString(parcial.numero),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = parcial.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
            Nivel(Modifier.padding(top = 12.dp))
        }
    }
}

fun getParcialString(number: Int): String{
    var string = "Primer Parcial"
    if (number == 2){
        string = "Segundo Parcial"
    }else if (number == 3){
        string = "Tercer Parcial"
    }
    return string
}