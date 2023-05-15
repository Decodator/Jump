package com.example.ipn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ipn.data.CardPriorities
import com.example.ipn.data.Materia
import com.example.ipn.ui.theme.IPNTheme


@Composable
fun MateriaCard(
    materia: Materia,
    onCardClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .clickable { onCardClicked(materia.path!!) }
            .clip(
                RoundedCornerShape(10)
            )
            .padding(top = 12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = materia.img_url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(10))
                    .size(120.dp)
            )
            Column(
                modifier = Modifier
                    .padding(PaddingValues(horizontal = 16.dp))
            ) {
                Text(
                    text = materia.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = materia.carrera,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .paddingFromBaseline(top = 6.dp)
                )
                CardStatus(CardPriorities(28, 12, 5, 9))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCard(){
    IPNTheme {
        MateriaCard(
            materia = Materia("f21r", "fef", "Biología", "El estudio de la vida", null, "Tronco Común"),
            onCardClicked = {/*TODO*/}
        )
    }
}