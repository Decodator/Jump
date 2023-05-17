package com.ipn.jump.data

data class Parcial(
    var id: String = "",
    var path: String = "",
    val numero: Int = 1,
    val descripcion: String = "empty",
    val img_url: String ?= null
)
