package com.ipn.jump

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ParcialScreen(
    viewModel: VM = viewModel(),
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
){
    val temas by viewModel.temas.collectAsStateWithLifecycle()
    Log.i("Temas", temas.toString())

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        item {
            val parcial = viewModel.unidad
            ParcialElement(parcial = parcial, onParcialClicked = { onClick(parcial.path) } )
        }
        items(temas){ tema ->
            TemaElement(tema, onClick, onLongClick)
        }
    }
}