package com.example.ipn.data

data class DeveloperCard(
    var ubicacion: String = "",

    var instruccion: String? = null,
    var pregunta: String = "",
    var type: Int = 1,
    val opciones: HashMap<String, String> = hashMapOf(),
    var respuestas: MutableList<String> = mutableListOf("")
) {
    fun error(): String?{
        if (pregunta.isEmpty()) return "Añade una pregunta primero"
        //if (opciones.isEmpty()) return "Añade opciones"
        if (respuestas.isEmpty()) return "Añade respuestas"
        return null
    }
}
