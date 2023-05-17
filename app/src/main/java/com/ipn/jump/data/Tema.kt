package com.ipn.jump.data

data class Tema(
    var id: String = "",
    var path: String = "",
    val name: String = "Null",
    val img_url: String ?= null,
    val subtemas: HashMap<String, String> = hashMapOf()
    )
