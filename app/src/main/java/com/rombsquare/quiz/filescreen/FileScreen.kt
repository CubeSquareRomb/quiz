package com.rombsquare.quiz.filescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.db.FileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileScreen(cardViewModel: CardViewModel, fileViewModel: FileViewModel, navController: NavController) {
    var showCreateFileDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        cardViewModel.getAll()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quizzes") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.LibraryAddCheck,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick =  {showCreateFileDialog = true},
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        content = {
            Box(Modifier.padding(it)) {
                FileList(fileViewModel) {
                    navController.navigate("editor/${it.id}")
                }
            }

        }
    )

    if (showCreateFileDialog) {
        CreateFileDialog(fileViewModel) {
            showCreateFileDialog = false
        }
    }

}