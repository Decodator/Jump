package com.example.ipn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun MateriaScreen(
    viewModel: VM = viewModel(),
    onParcialClicked: (String) -> Unit,
    onStudyClicked: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val materia = viewModel.materia
    val parciales by viewModel.unidades.collectAsStateWithLifecycle()
    
    LazyColumn(modifier) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = materia.img_url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.aspectRatio(16f/9f)
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        text = materia.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = materia.carrera,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.paddingFromBaseline(top = 8.dp)
                    )
                    ElevatedButton(onClick = {onStudyClicked(materia.path!!)}) {
                        Text("Estudiar materia", modifier = Modifier.paddingFromBaseline(8.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = stringResource(R.string.aprendizaje_especifico),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.parcial),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        items(parciales){parcial ->
            ParcialElement(parcial, { onParcialClicked(parcial.path!!) })
        }
    }
}