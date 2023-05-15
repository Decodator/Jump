package com.example.ipn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ipn.ui.theme.IPNTheme

@Composable
fun HomeScreen(
    onNavigateToMateria: (String) -> Unit,
    viewModel: VM = viewModel(),
    modifier: Modifier = Modifier
){
    val materias by viewModel.materias.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        item {
            DailyCard()
        }
        items(materias){materia->
            MateriaCard(materia, onCardClicked = onNavigateToMateria )
        }
    }
}

@Composable
fun DailyCard(){
    Box(modifier = Modifier.clip(RoundedCornerShape(10))){
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            painter = painterResource(R.drawable.darkhole),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(18.dp)) {
            Text(
                text = "Repaso diario",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface
            )
            Text(
                text = "Solamente tomará 5 minutos",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface
            )
            ElevatedButton(
                modifier = Modifier.padding(top = 12.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Comenzar!")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMatters() {
    IPNTheme {
        HomeScreen({})
    }
}