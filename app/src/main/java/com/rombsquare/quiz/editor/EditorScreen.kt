package com.rombsquare.quiz.editor

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.db.FileEntity
import com.rombsquare.quiz.db.FileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(navController: NavController, fileId: Int, cardViewModel: CardViewModel, fileViewModel: FileViewModel) {
    val context = LocalContext.current

    var showCreateCardDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember {mutableStateOf(false)}
    var showPlayDialog by remember {mutableStateOf(false)}
    var showPlaySettingsDialog by remember {mutableStateOf(false)}
    var showDeleteDialog by remember {mutableStateOf(false)}
    var showTagDialog by remember {mutableStateOf(false)}

    var selectedCard by remember { mutableStateOf<CardEntity?>(null) }
    var selectedMode by remember {mutableStateOf("")}

    val files by fileViewModel.files.collectAsState()
    val file = files.find {it.id == fileId} ?: return

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("main")
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showTagDialog = true
                    }) {
                        Icon(Icons.Default.Tag, contentDescription = "Tags")
                    }

                    IconButton(onClick = {
                        showDeleteDialog = true
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }

                    IconButton(onClick = {
                        showRenameDialog = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }

                    IconButton(onClick = {
                        coroutineScope.launch {
                            fileViewModel.set(file.copy(isFav = !file.isFav))
                            fileViewModel.getAllNonTrashed()
                        }
                    }) {
                        Icon(if(file.isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    cardViewModel.getAll()
                    if (cardViewModel.cards.value.filter { it.fileId == fileId }.size < 4) {
                        Toast.makeText(context, "You need at least 4 cards", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    showPlayDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    ) { paddingValues ->
        EditorCardList(
            modifier = Modifier.padding(paddingValues),
            viewModel = cardViewModel,
            fileId = fileId,
            onCardClick = {
                selectedCard = it
                showEditDialog = true
            },
            onAddClick = {
                showCreateCardDialog = true
            }
        )

        if (showCreateCardDialog) {
            CreateCardDialog(
                onDismiss = {showCreateCardDialog = false}
            ) { side1, side2 ->
                if ((cardViewModel.cards.value.filter {it.fileId == fileId}.map {it.side2}).contains(side2.trim())) {
                    Toast.makeText(context, "2 cards can't have the same answer", Toast.LENGTH_SHORT).show()
                    return@CreateCardDialog
                }
                cardViewModel.insert(CardEntity(
                    side1 = side1,
                    side2 = side2,
                    fileId = fileId,
                    fixedOptions = false,
                    incorrectOption1 = "",
                    incorrectOption2 = "",
                    incorrectOption3 = "",
                ))
                showCreateCardDialog = false
            }
        }

        if (showEditDialog) {
            EditCardDialog(
                currentCard = selectedCard!!,
                onDismiss = {showEditDialog = false},
                onDelete = {
                    cardViewModel.delete(selectedCard!!)
                    showEditDialog = false
                },
                onConfirm = { fixedOptions, side1, side2, incorrectOption1, incorrectOption2, incorrectOption3 ->
                    if ((cardViewModel.cards.value.filter {it.fileId == fileId}.map {it.side2}).contains(side2.trim()) &&
                        selectedCard!!.side2 != side2) {
                        Toast.makeText(context, "2 cards can't have the same answer", Toast.LENGTH_SHORT).show()
                        return@EditCardDialog
                    }
                    cardViewModel.set(CardEntity(
                        id = selectedCard!!.id,
                        side1 = side1,
                        side2 = side2,
                        fileId = fileId,
                        fixedOptions = fixedOptions,
                        incorrectOption1 = incorrectOption1,
                        incorrectOption2 = incorrectOption2,
                        incorrectOption3 = incorrectOption3,
                    ))
                    showEditDialog = false
                }
            )
        }

        val coroutineScope = rememberCoroutineScope()
        if (showRenameDialog) {
            RenameDialog(
                onConfirm = {
                    if (it.isEmpty()) {
                        Toast.makeText(context, "Enter a new quiz name", Toast.LENGTH_SHORT).show()
                        return@RenameDialog
                    }

                    coroutineScope.launch {
                        fileViewModel.set(FileEntity(
                            id = fileId,
                            name = it
                        ))
                    }

                    showRenameDialog = false
                },
                onDismiss = {
                    showRenameDialog = false
                }
            )
        }

        if (showPlayDialog) {
            PlayDialog(
                onDismiss = {
                    showPlayDialog = false
                },
                onPlay = {
                    selectedMode = it
                    showPlayDialog = false
                    showPlaySettingsDialog = true
                }
            )
        }

        if (showPlaySettingsDialog) {
            PlaySettingsDialog(
                onChooseAllCards = {
                    navController.navigate("$selectedMode/$fileId/-1")
                },
                onConfirm = {
                    navController.navigate("$selectedMode/$fileId/$it")
                }
            )
        }

        if (showDeleteDialog) {
            DeleteDialog(
                onConfirm = {
                    coroutineScope.launch {
                        fileViewModel.set(FileEntity(
                            id = file.id,
                            name = file.name,
                            tags = file.tags,
                            isFav = file.isFav,
                            isTrashed = true
                        ))
                    }

                    //fileViewModel.delete(FileEntity(fileId, ""))
                    navController.navigate("main")
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }

        if (showTagDialog) {
            TagDialog(
                file = file,
                onConfirm = {
                    coroutineScope.launch {
                        fileViewModel.set(FileEntity(
                            file.id,
                            file.name,
                            it,
                            file.isFav,
                            file.isTrashed
                        ))
                    }
                    showTagDialog = false
                },
                onDismiss = {
                    showTagDialog = false
                }
            )
        }
    }
}