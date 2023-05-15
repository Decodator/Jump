package com.example.ipn

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.ipn.data.Card
import com.example.ipn.data.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter

class SessionVM : ViewModel() {

    private val db = Firebase.firestore

    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var user: FirebaseUser

    private var _path: String = ""
    val path get() = _path

    private val _cards = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val cards: StateFlow<MutableList<Card>> = _cards.asStateFlow()

    private val _session: MutableList<Review> = mutableListOf()
    val session get() = _session

    init {
        auth = Firebase.auth
        user = auth.currentUser!!
    }

    fun updatePath(path: String) {
        _path = path
    }

    fun retrieveCards() {
        if (cards.value.isEmpty()) {
            val collection = db.collection("cards").whereGreaterThanOrEqualTo("ubicacion", path)
                .whereLessThan("ubicacion", path + "\uf8ff")
            collection.get().addOnSuccessListener {
                val list = mutableListOf<Card>()
                for (doc in it!!.documents) {
                    val card = doc.toObject(Card::class.java)
                    card?.id = doc.id
                    if (card != null) {
                        val shuffledKeys = card.opciones.keys.shuffled()
                        card.shuffledKeys = shuffledKeys
                        list.add(card)
                    }
                }
                if (list.isNotEmpty()) {
                    _cards.value = list
                    retrieveUserCards()
                } else {
                    val options = hashMapOf(
                        Pair("2g82", "Adolfo de la Huerta"),
                        Pair("fgk3", "Emilio Portes Gil"),
                        Pair("g832", "Abelardo L. Rodríguez")
                    )
                    val card = Card(
                        type = Reorder,
                        pregunta = "Ordena los gobiernos del caudillismo en orden cronológico",
                        opciones = options,
                        respuestas = mutableListOf("2g82", "g832", "fgk3"),
                        userAnswers = options.keys.shuffled().toMutableList()
                    )
                    _cards.value = mutableListOf(card)
                }
                Log.i("cards", _cards.value.toString())

            }.addOnFailureListener {
                Log.e("VM Exception", it.toString())
            }
        }
    }

    fun retrieveUserCards() {
        val cardsIds = mutableListOf<String>()
        for (card in cards.value) {
            cardsIds.add(card.id)
            Log.i("Card ids", cardsIds.toString())
        }
        val collection = db.collection("usuarios").document(user.uid).collection("cards")
            .whereIn("card_id", cardsIds)
        collection.get().addOnSuccessListener {
            Log.i("documents size", it.documents.size.toString())
            for (doc in it!!.documents) {
                val cardId: String = doc["card_id"] as String
                val status = doc["status"] as Long
                Log.i("Id", cardId)
                _cards.value.first { card -> card.id == cardId }.status = status.toInt()
            }
            filterCards()
        }.addOnFailureListener {
            Log.i("Exception", it.toString())
        }
    }

    fun filterCards() {
        /*Set the actual status value*/
        val cleanCards = cards.value.filter { it.status < 5 }
        _cards.value = cleanCards as MutableList<Card>
        Log.i("Cards", "Updated")
    }

    fun updateCard(card: Card) {
        val userCardsReference = db.collection("usuarios").document(user.uid).collection("cards")
        val data = hashMapOf(Pair("card_id", card.id), Pair("status", card.status))
        userCardsReference.document(card.id).set(data)
            .addOnSuccessListener {
                Log.i("Session", "Card updated correctly")
            }.addOnFailureListener {
                Log.e(it.toString(), it.toString())
            }
    }

    companion object {
        //Types of questions
        const val OneChoice = 1
        const val Checkbox = 2
        const val OpenAnswer = 3
        const val FillInTheBlank = 4
        const val Reorder = 5
        const val ColumnRelationship = 6

        //Card Status
        const val Blue = 0
        const val Red = 1
        const val Yellow = 2
        const val Green = 3
        const val God = 4
    }
}