package com.example.ipn

import androidx.compose.runtime.Composable
import com.example.ipn.SessionVM.Companion.Reorder
import com.example.ipn.data.Card

@Composable
fun ExploreScreen(){
    val options = hashMapOf(Pair("2g82", "Adolfo de la Huerta"), Pair("fgk3", "Emilio Portes Gil"), Pair("g832", "Abelardo L. Rodríguez"))
    val card = Card(
        type = Reorder,
        pregunta = "Ordena los gobiernos del caudillismo en orden cronológico",
        opciones = options,
        respuestas = mutableListOf("2g82", "g832", "fgk3"),
        userAnswers = options.keys.shuffled().toMutableList()
    )
    ReorderCard(card)
}