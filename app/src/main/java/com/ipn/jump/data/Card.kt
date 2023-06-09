package com.ipn.jump.data

import com.ipn.jump.SessionVM
import com.ipn.jump.SessionVM.Companion.Blue
import com.ipn.jump.SessionVM.Companion.OneChoice

data class Card(
    var id: String = "",

    var instruccion: String? = null,
    var pregunta: String = "",
    var type: Int = OneChoice,
    val opciones: HashMap<String, String> = hashMapOf(),
    var shuffledKeys: List<String> = emptyList(),
    var respuestas: MutableList<String> = mutableListOf(""),

    var userAnswers: MutableList<String> = mutableListOf(""),
    var state: CardState = CardState.Unchecked,

    var status: Int = Blue
) {
    fun getExplicitAnswers(): String {
        var correctAnswers = mutableListOf<String>()
        if (type == SessionVM.OpenAnswer){
            correctAnswers = respuestas
        }else {
            for (answer in respuestas){
                correctAnswers.add(opciones[answer] ?: "Null")
            }
        }
        return correctAnswers.toString()
    }
}