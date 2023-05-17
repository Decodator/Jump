package com.ipn.jump

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ipn.jump.SessionVM.Companion.Blue
import com.ipn.jump.SessionVM.Companion.Checkbox
import com.ipn.jump.SessionVM.Companion.FillInTheBlank
import com.ipn.jump.SessionVM.Companion.Green
import com.ipn.jump.SessionVM.Companion.OneChoice
import com.ipn.jump.SessionVM.Companion.OpenAnswer
import com.ipn.jump.SessionVM.Companion.Red
import com.ipn.jump.SessionVM.Companion.Reorder
import com.ipn.jump.SessionVM.Companion.Yellow
import com.ipn.jump.data.Card
import com.ipn.jump.data.CardState
import com.ipn.jump.session.Option
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(viewModel: SessionVM, onClose: () -> Unit, onEnding: () -> Unit) {
    val cards by viewModel.cards.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState()
    val currentPosition = pagerState.currentPage

    val coroutineScope = rememberCoroutineScope()
    if (cards.isNotEmpty()) {
        Scaffold(
            topBar = {
                SessionTopBar(
                    calculateProgress(cards.size, pagerState.currentPage),
                    onClose
                )
            },
            content = { padding ->
                QuestionsPager(
                    padding, cards,
                    pagerState
                )
            },
            bottomBar = {
                val currentCard = cards[currentPosition]
                // create a variable that recomposes the bottom bar whenever it changes its value
                var cardState by remember { mutableStateOf(CardState.Unchecked) }
                cardState = currentCard.state
                QuestionsBottomBar(
                    cardState = cardState,
                    correctAnswer = currentCard.getExplicitAnswers(),
                    addQuestionToTheEnd = {
                        val newCards = mutableListOf<Card>()
                        newCards.addAll(cards)
                        /* Create a copy of the current question to repeat it at the end of the session
                          until the user gets it right */
                        val repeatingQuestion = currentCard.copy(
                            state = CardState.Unchecked,
                            userAnswers = mutableListOf("")
                        )
                        cards.add(repeatingQuestion)
                    },
                    onCheckAnswer = {
                        val newState = currentCard.checkState()
                        currentCard.state = newState
                        cardState = newState

                        if (currentCard.state == CardState.WrongAnswer) {
                            currentCard.status = Red
                        } else if (currentCard.state == CardState.RightAnswer) {
                            currentCard.status = currentCard.status + 1
                        }
                        //viewModel.updateCard(currentCard)
                    },
                    onContinue = {
                        Log.i("Session", "pager position: ${pagerState.currentPage}, last index: ${cards.lastIndex}")
                        if (pagerState.currentPage + 1 < cards.lastIndex) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            Log.i("Session", "On Ending")
                            onEnding()
                        }
                    }
                )
            }
        )
    } else {
        Text(
            text = "Parece que está vacío",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionsPager(padding: PaddingValues, cards: List<Card>, pagerState: PagerState) {
    HorizontalPager(pageCount = cards.size, state = pagerState, contentPadding = padding) { page ->
        Column(Modifier.padding(16.dp)) {
            val card = cards[page]
            if (card.instruccion == null) {
                card.instruccion = getInstruction(card.type)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Color(1)
                Surface(
                    shape = RoundedCornerShape(100),
                    color = getColorFromStatus(card.status),
                    modifier = Modifier.size(12.dp)
                ) {}
                Text(
                    text = card.instruccion!!,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Text(
                text = card.pregunta,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            when (card.type) {
                OneChoice -> OneChoiceCard(card)
                Checkbox -> MultiChoiceCard(card)
                OpenAnswer -> OpenCard(card)
                FillInTheBlank -> FillInTheBlankCard(card)
                Reorder -> ReorderCard(card)
                //ColumnRelationship -> ColumnRelationshipCard(card)
            }
        }
    }
}

@Composable
fun OneChoiceCard(card: Card) {
    var selectedOptionId by rememberSaveable { mutableStateOf(card.userAnswers.firstOrNull()) }
    card.shuffledKeys.forEach { id ->
        val option = card.opciones[id]!!
        val isSelected = selectedOptionId == id
        val isRight = if (card.state == CardState.Unchecked) null else card.respuestas.contains(id)
        Option(text = option, isSelected = isSelected, isRight = isRight, onSelected = {
            // Block changes after question has been answered
            if (card.state == CardState.Unchecked) {
                card.userAnswers = mutableListOf(id)
                selectedOptionId = id
            }
        })
        Spacer(Modifier.height(10.dp))
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MultiChoiceCard(card: Card) {
    var selectedIds by remember { mutableStateOf(card.userAnswers) }
    card.shuffledKeys.forEach { id ->
        val text = card.opciones[id]!!
        var isSelected = selectedIds.contains(id)
        val isRight = if (card.state == CardState.Unchecked) null else card.respuestas.contains(id)

        Option(
            text = text,
            isSelected = isSelected,
            isRight = isRight,
            isCheckbox = true,
            onSelected = {
                if (card.state == CardState.Unchecked) {
                    val index = selectedIds.indexOf(id)
                    isSelected = !isSelected
                    val newIds = mutableListOf<String>()
                    newIds.addAll(selectedIds)
                    if (isSelected) {
                        if (index == -1) {
                            newIds.add(id)
                        } else {
                            newIds[index] = id
                        }
                    } else {
                        newIds.remove(id)
                    }
                    selectedIds = newIds
                    // Update the viewModel values
                    card.userAnswers = selectedIds
                }
            })
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun OpenCard(card: Card) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(card.userAnswers.first()))
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            card.userAnswers = mutableListOf(text.text)
            text = it
        },
        label = { Text("Respuesta") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillInTheBlankCard(card: Card) {
    val reactive = card.pregunta
    val parts = reactive.split("%_%")
    Column {
        parts.forEachIndexed { index, part ->
            Text(text = part)
            if (index < parts.size - 1) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Fill in the blank") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ReorderCard(card: Card) {
    var userAnswers by remember { mutableStateOf(card.userAnswers.toList()) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        userAnswers = userAnswers.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        card.userAnswers = userAnswers.toMutableList()
    })
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(userAnswers, { it }) { key ->
            val text = card.opciones[key] ?: "Null"
            ReorderableItem(state, key) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                DraggableItem(text, elevation)
            }
        }
    }
}

@Composable
fun DraggableItem(text: String, elevation: State<Dp>) {
    Row(
        modifier = Modifier
            .shadow(elevation.value)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
            .clip(RoundedCornerShape(20))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(R.drawable.drag_icon), contentDescription = "mover")
        Text(text)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionTopBar(progress: Float, onEnding: () -> Unit) {
    TopAppBar(
        title = {
            LinearProgressIndicator(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(50))
                    .semantics(mergeDescendants = true) {},
                progress = progress
            )
        },
        navigationIcon = {
            IconButton(onClick = onEnding) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cerrar)
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.flag_icon),
                    contentDescription = stringResource(R.string.reportar)
                )
            }
        }
    )
}

fun calculateProgress(size: Int, currentPage: Int): Float {
    return 1f / size * (currentPage + 1)
}

@Composable
fun QuestionsBottomBar(
    cardState: CardState,
    correctAnswer: String,
    addQuestionToTheEnd: () -> Unit,
    onCheckAnswer: () -> Unit,
    onContinue: () -> Unit
) {
    val surfaceColor: Color
    val barMessage: String
    val buttonColor: ButtonColors
    val buttonMsg: String
    val onClick: () -> Unit
    when (cardState) {
        CardState.RightAnswer -> {
            surfaceColor = Color(0xFFD6FFDB)
            barMessage = "Respuesta correcta"
            buttonColor = ButtonDefaults.buttonColors(containerColor = Color(0xFF29C13C))
            buttonMsg = "Continuar"
            onClick = onContinue
        }

        CardState.WrongAnswer -> {
            surfaceColor = Color(0xFFFFD2D2)
            barMessage = "Respuesta incorrecta"
            buttonColor = ButtonDefaults.buttonColors(containerColor = Color(0xFFD30B0B))
            buttonMsg = "Continuar"
            onClick = {
                addQuestionToTheEnd()
                onContinue()
            }
        }

        else -> {
            surfaceColor = MaterialTheme.colorScheme.surface
            barMessage = ""
            buttonColor =
                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            buttonMsg = "Comprobar"
            onClick = {
                Log.i("Question checked", "True")
                onCheckAnswer()
            }
        }
    }

    Surface(
        color = surfaceColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = barMessage,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .paddingFromBaseline(top = 32.dp)
            )
            if (cardState != CardState.Unchecked) {
                Text(
                    text = "Respuesta correcta:",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.paddingFromBaseline(top = 12.dp)
                )
                Text(
                    text = correctAnswer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                onClick = onClick,
                colors = buttonColor
            ) {
                Text(text = buttonMsg)
            }
        }
    }
}

fun Card.checkState(): CardState {
    userAnswers.removeIf { it.isBlank() }
    Log.i("Check state", "Actual answer: $respuestas, User Answer: $userAnswers")
    if (userAnswers.isEmpty()) {
        return CardState.Unchecked
    }
    return when (type) {
        OneChoice -> if (userAnswers.first() == respuestas.first()) CardState.RightAnswer else CardState.WrongAnswer
        Checkbox -> if (respuestas.containsAll(userAnswers) && userAnswers.containsAll(respuestas)) CardState.RightAnswer else CardState.WrongAnswer
        OpenAnswer -> if (respuestas.contains(userAnswers.first())) CardState.RightAnswer else CardState.WrongAnswer
        FillInTheBlank -> if (userAnswers == respuestas) CardState.RightAnswer else CardState.WrongAnswer
        Reorder -> if (userAnswers == respuestas) CardState.RightAnswer else CardState.WrongAnswer
        else -> {
            CardState.Unchecked
        }
    }
}

fun getColorFromStatus(status: Int): Color {
    return when (status) {
        Blue -> Color(0xFF303F9F)
        Yellow -> Color(0xFFFBC02D)
        Red -> Color(0xFFDB0000)
        Green -> Color(0xFF29C13C)
        else -> Color(0xFF000000)
    }
}

fun getInstruction(type: Int): String {
    return when (type) {
        OneChoice -> "Selecciona la opción correcta"
        Checkbox -> "Selecciona las opciones correctas"
        OpenAnswer -> "Escribe la respuesta correcta"
        FillInTheBlank -> "Rellena los espacios en blanco"
        Reorder -> "Ordena los elementos como se indíca"
        else -> {
            "Selecciona la opción correcta"
        }
    }
}