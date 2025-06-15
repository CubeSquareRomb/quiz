package com.rombsquare.quiz.editorscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.rombsquare.quiz.InputDialog
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.db.FileEntity
import com.rombsquare.quiz.db.FileViewModel
import com.rombsquare.quiz.filescreen.PlusFab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(navController: NavController, fileId: Int, cardViewModel: CardViewModel, fileViewModel: FileViewModel) {
    var showCreateCardDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember {mutableStateOf(false)}

    var selectedCard by remember { mutableStateOf<CardEntity?>(null) }

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
                        fileViewModel.delete(FileEntity(fileId, ""))
                        navController.navigate("main")
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                    IconButton(onClick = {
                        showRenameDialog = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        },

        floatingActionButton = {
            PlusFab {
                showCreateCardDialog = true
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
            }
        )

        if (showCreateCardDialog) {
            CreateCardDialog(
                onDismiss = {showCreateCardDialog = false}
            ) { side1, side2 ->
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

        if (showRenameDialog) {
            InputDialog(
                title = "Rename",
                label = "New name",
                onConfirm = {
                    fileViewModel.set(FileEntity(
                        id = fileId,
                        name = it
                    ))
                    showRenameDialog = false
                },
                onDismiss = {
                    showRenameDialog = false
                }
            )
        }
    }
}