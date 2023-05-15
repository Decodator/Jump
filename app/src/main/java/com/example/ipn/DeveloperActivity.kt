package com.example.ipn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ipn.session.DeveloperScreen
import com.example.ipn.session.generateRandomId
import com.example.ipn.ui.theme.IPNTheme
import kotlinx.coroutines.launch

class DeveloperActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val path = intent.getStringExtra("path")
        val id = intent.getStringExtra("id")
        setContent {
            IPNTheme {
                Log.i("Path", path.toString())
                Log.i("Id", id.toString())
                Developer(path, id){
                    finish()
                }
            }
        }
    }
}

@Composable
fun Developer(path: String?, id: String?, onClose: () -> Unit) {
    val viewModel: VM = viewModel()
    if (path != null){
        viewModel.updatePath(path)
    }else if (id != null){
        viewModel.loadCard(id)
    }
    DeveloperContent(viewModel, id, onClose)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeveloperContent(viewModel: VM, id: String?, onClose: () -> Unit){
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val snackbarHostState = remember { SnackbarHostState() }

    var optionKey = ""
    val optionText = remember { mutableStateOf("")}

    ModalBottomSheetLayout(sheetContent = {
        BottomSheetContent(
            textValue = optionText,
            onClose = { scope.launch { modalSheetState.hide() } },
            onSave = {
                val options = viewModel.card.value.opciones
                options[optionKey] = optionText.value
                //Update the Id temporally to trigger a change in the UI
                val newCard = viewModel.card.value.copy(opciones = options, ubicacion = generateRandomId())
                viewModel.setCard(newCard)
                scope.launch { modalSheetState.hide() }
            }
        )
    },
        sheetState = modalSheetState
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { DeveloperTopBar(
                onUpload = {
                    val error = viewModel.card.value.error()
                    if (error == null){
                        if (id == null){
                            viewModel.uploadCard()
                        }else {
                            viewModel.updateCard(id)
                        }
                    }else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Pregunta inválida: $error")
                        }
                    }
                },
                onClose = onClose
            )
            },
            content = { padding ->
                DeveloperScreen(
                    viewModel = viewModel,
                    onEdit = { key, option_text ->
                        scope.launch{
                            optionKey = key
                            optionText.value = option_text
                            modalSheetState.show()
                        }
                    },
                    paddingValues = padding
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperTopBar(onUpload: () -> Unit, onClose: () -> Unit){
    TopAppBar(
        title = { Text("Agregar pregunta") },
        navigationIcon = {
            IconButton(onClick =  onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
            }
        },
        actions = {
            TextButton(onClick = onUpload) {
                Text(text = "Subir")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(textValue: MutableState<String>, onClose: () -> Unit, onSave: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .clip(RoundedCornerShape(topStartPercent = 25, topEndPercent = 25))
    ) {
        OutlinedTextField(
            value = textValue.value,
            onValueChange = { newString ->
                textValue.value = newString
            },
            label = { Text("Opción") }
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(
                onClick = onClose
            ) {
                Text("Cancelar")
            }
            TextButton(
                onClick = { onSave() }
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    IPNTheme {
    }
}