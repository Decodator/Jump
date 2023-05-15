package com.example.ipn

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ipn.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VM: ViewModel() {

    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private var _path: String = ""
    val path get() = _path

    private val _materias = MutableStateFlow<List<Materia>>(emptyList())
    val materias: StateFlow<List<Materia>> = _materias.asStateFlow()

    lateinit var materia: Materia

    private val _unidades = MutableStateFlow<List<Parcial>>(emptyList())
    val unidades: StateFlow<List<Parcial>> = _unidades.asStateFlow()

    lateinit var unidad: Parcial

    private val _temas = MutableStateFlow<List<Tema>>(emptyList())
    val temas: StateFlow<List<Tema>> = _temas.asStateFlow()

    private val _cardsPreview = MutableStateFlow<List<CardPreview>>(emptyList())
    val cardsPreview: StateFlow<List<CardPreview>> = _cardsPreview.asStateFlow()

    init {
        retrieveMaterias()
        auth = Firebase.auth
        logIn()
    }

    fun updatePath(path: String){
        _path = path
    }

    private fun logIn(){
        if (auth.currentUser == null){
            auth.signInAnonymously()
                .addOnSuccessListener {
                    user = auth.currentUser!!
                }
                .addOnFailureListener {
                    Log.e("Exception", it.toString())
                }
        }
    }

    fun getMateriaFromPath(){
        materia = materias.value.first { it.path == path }
    }

    private fun retrieveMaterias(){
        val collection = db.collection("materias")
        collection.get().addOnSuccessListener {
            val list = mutableListOf<Materia>()
            for (doc in it!!.documents) {
                val materia = doc.toObject(Materia::class.java)
                materia?.id = doc.id
                materia?.path = doc.reference.path
                if (materia != null) {
                    list.add(materia)
                }
            }
            _materias.value = list
        }.addOnFailureListener{
            Log.e("VM Exception", it.toString())
        }
    }

    fun getUnidadFromPath(){
        unidad =  unidades.value.first { it.path == path }
    }

    fun retrieveUnidades(){
        val collection = db.collection("$path/unidades")

        collection.get().addOnSuccessListener {
            val list = mutableListOf<Parcial>()
            for (doc in it!!.documents){
                val parcial = doc.toObject(Parcial::class.java)
                parcial?.id = doc.id
                parcial?.path = doc.reference.path
                if (parcial != null){
                    list.add(parcial)
                }
            }
            _unidades.value = list
        }.addOnFailureListener{
            Log.e("VM Exception", it.toString())
        }
    }

    fun retrieveTemas(){
        val collection = db.collection("$path/temas")

        collection.get().addOnSuccessListener {
            val temas = mutableListOf<Tema>()
            for (doc in it!!.documents){
                val tema = doc.toObject(Tema::class.java)
                tema?.id = doc.id
                tema?.path = doc.reference.path
                if (tema != null){
                    temas.add(tema)
                }
            }
            _temas.value = temas.sortedBy { tema -> tema.name }

        }.addOnFailureListener{
            Log.e("VM Exception", it.toString())
        }
    }

    fun retrieveCardsPreview(){
        val collection = db.collection("cards").whereGreaterThanOrEqualTo("ubicacion", path)
            .whereLessThan("ubicacion", path + "\uf8ff")
        collection.get().addOnSuccessListener {
            val tempList = mutableListOf<CardPreview>()
            for (doc in it!!.documents){
                val card = doc.toObject(CardPreview::class.java)
                card?.id = doc.id
                if (card != null){
                    tempList.add(card)
                }
            }
            _cardsPreview.value = tempList
        }
    }

    // VIEW MODEL FOR DEVELOPER
    private val _card = MutableStateFlow(DeveloperCard())
    val card = _card.asStateFlow()

    fun loadCard(id: String){
        val document = db.collection("cards").document(id)
        document.get().addOnSuccessListener {
            if (it.exists()) {
                val card = it.toObject(DeveloperCard::class.java)!!
                _card.value = card
                _path = card.ubicacion
            } else {
                Log.i("Card loading: ", "Document doesn't exist")
            }
        }.addOnFailureListener {
            Log.e("Card loading", it.toString())
        }
    }

    fun setCard(card: DeveloperCard){
        _card.value = card
    }

    fun uploadCard() {
        _card.value.ubicacion = path
        val collection = db.collection("cards")
        collection.add(card.value)
            .addOnSuccessListener {
                Log.i("Could Firestore", "Card uploaded successfully")
                _card.value = DeveloperCard()
            }.addOnFailureListener{
                Log.e("Cloud Firestore","Card uploaded unsuccessfully")
            }
    }

    fun updateCard(id: String){
        val document = db.collection("cards").document(id)
        document.set(card.value)
            .addOnSuccessListener {
                Log.i("Cloud Firestore", "Card updated successfully")
            }.addOnFailureListener {
                Log.i("Cloud Firestore", "Card not updated")
            }
    }

    companion object {
        const val TAG = "Main View Model"
    }
}