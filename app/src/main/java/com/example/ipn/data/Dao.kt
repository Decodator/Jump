package com.example.ipn.data

import android.icu.text.IDNA
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Dao {

    val materiasSample = listOf(
        Materia("12f3y", "Biología", "El estudio de los seres vivos", "", "Tronco Común"),
        Materia("h537a", "Computación", "Destaca en Word, Excel y HTML", "", "Tronco Común"),
        Materia("ga3tg", "Álgebra", "X = random", "", "Tronco Común"),
        Materia(
            "g4ag3",
            "Geometría y Trigonometría",
            "La rama de las figuras",
            "",
            "Tronco Común"
        ),
        Materia("fvy3r", "Técnicas", "Conduce investigaciones de campo", "", "Tronco Común")
    )


    /*TODO Add real functions that download the actual content from the database*/

    fun getParciales(materiaId: String): List<Parcial>{
        return listOf(
            Parcial("","", 1, "Células | La unidad de la vida"),
            Parcial("","", 2, "Continuidad de los seres vivos"),
            Parcial("", "", 3, "Evolución")
        )
    }
}