package com.ipn.jump.data

data class Materia(
    var id: String = "101",
    var path: String? = null,

    val name: String = "Materia",
    val description: String?= "Sample description",
    val img_url: String? = null,
    val carrera: String = "Tronco Común",
    val semestre: String = "Primer Semestre"
)
