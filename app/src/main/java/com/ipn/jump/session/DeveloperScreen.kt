package com.ipn.jump.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipn.jump.R
import com.ipn.jump.SessionVM.Companion.FillInTheBlank
import com.ipn.jump.SessionVM.Companion.OneChoice
import com.ipn.jump.SessionVM.Companion.OpenAnswer
import com.ipn.jump.SessionVM.Companion.Checkbox
import com.ipn.jump.VM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(viewModel: VM = viewModel(), onEdit: (String, String) -> Unit, paddingValues: PaddingValues){
    val card by viewModel.card.collectAsStateWithLifecycle()
    var questionType by rememberSaveable{ mutableStateOf(card.type)}

    Column(
        Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = viewModel.path,
            style = MaterialTheme.typography.titleSmall
        )
        var questionText by remember { mutableStateOf(card.pregunta) }
        questionText = card.pregunta
        Section("Reactivo") { Reactive(question = questionText) { newQuestion ->
            card.pregunta = newQuestion
            questionText = newQuestion
        } }
        Section("Tipo de Pregunta") { TypeOfQuestion(questionType) { index ->
            card.type = index
            questionType = index
        } }

        var optionsChanges by remember { mutableStateOf(0) }

        Section("Opciones") {
            when (questionType) {
                OneChoice -> {
                    Text(
                        text = stringResource(R.string.radio_help),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (card.respuestas.size > 1) card.respuestas.clear()
                    Options(
                        options = card.opciones,
                        selectedIds = card.respuestas,
                        optionsChanges = optionsChanges,
                        onOptionSelected = { optionId ->
                            card.respuestas = mutableListOf(optionId)
                            optionsChanges += 1
                        },
                        onEdit = onEdit
                    )
                    Row {
                        OutlinedButton(onClick = {
                            card.opciones.clear()
                            card.respuestas.clear()
                            optionsChanges += 1
                        }) {
                            Text(text = stringResource(R.string.clean_options))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(onClick = {
                            val optionId = generateRandomId()
                            card.opciones[optionId] = "Opción"
                            if (card.opciones.size == 1){
                                card.respuestas = mutableListOf(optionId)
                            }
                            optionsChanges += 1
                            onEdit
                        }) {
                            Text(text = stringResource(R.string.añadir_opcion))
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                        }
                    }
                }
                Checkbox -> {
                    Text(
                        text = stringResource(R.string.checbox_help),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Options(
                        options = card.opciones,
                        selectedIds = card.respuestas,
                        isCheckbox = true,
                        optionsChanges = optionsChanges,
                        onOptionSelected = { optionId ->
                            if (card.respuestas.contains(optionId)){
                                card.respuestas.remove(optionId)
                            } else {
                                card.respuestas.add(optionId)
                            }
                            optionsChanges += 1
                        },
                        onEdit = onEdit
                    )
                    Row {
                        OutlinedButton(onClick = {
                            card.opciones.clear()
                            card.respuestas.clear()
                            optionsChanges += 1
                        }) {
                            Text(text = stringResource(R.string.clean_options))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(onClick = {
                            val optionId = generateRandomId()
                            card.opciones[optionId] = "Opción"
                            if (card.opciones.size == 1){
                                card.respuestas = mutableListOf(optionId)
                            }
                            optionsChanges += 1
                        }) {
                            Text(text = stringResource(R.string.añadir_opcion))
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                        }
                    }
                }
                OpenAnswer -> {
                    if (card.respuestas.isEmpty()) card.respuestas.add("")
                    var text by remember { mutableStateOf(card.respuestas.first()) }
                    OutlinedTextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                            card.respuestas[0] = newText
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                FillInTheBlank -> {}
            }
        }

    }
}

fun generateRandomId(): String {
    val charPool: List<Char> = ('0'..'9') + ('a'..'z') + ('A'..'Z')
    return (1..4)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit){
    Spacer(Modifier.height(16.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.paddingFromBaseline(bottom = 12.dp)
    )
    content()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Reactive(question: String, onTextChanges: (String) -> Unit) {
    OutlinedTextField(
        value = question,
        onValueChange = {
            onTextChanges(it)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun TypeOfQuestion(questionType: Int, onClick: (Int) -> Unit) {
    val typeNames = listOf("Selección multiple", "Checkbox", "Pregunta abierta", "Fill-in-the-blank")
    typeNames.forEachIndexed() { index, typeName ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .selectable(
                    selected = (questionType == index + 1),
                    onClick = { onClick(index + 1) },
                    role = Role.RadioButton
                )
        ) {
            RadioButton(
                selected = (questionType == index+1),
                onClick = null
            )
            Text(
                text = typeName,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun Options(
    options: HashMap<String, String>,
    selectedIds: List<String>,
    isCheckbox: Boolean = false,
    optionsChanges: Int,
    onOptionSelected: (String) -> Unit,
    onEdit: (String, String) -> Unit
){
    options.forEach{ option ->
        Option(
            text = option.value,
            isCheckbox = isCheckbox,
            isSelected = (selectedIds.contains(option.key)),
            onSelected = { onOptionSelected(option.key) },
            editable = true
        ) { onEdit(option.key, option.value) }
        Spacer(Modifier.height(14.dp))
    }

}