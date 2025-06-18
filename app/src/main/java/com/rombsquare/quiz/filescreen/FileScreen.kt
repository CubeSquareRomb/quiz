package com.rombsquare.quiz.filescreen

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.db.FileViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FileScreen(cardViewModel: CardViewModel, fileViewModel: FileViewModel, navController: NavController) {
    var showCreateFileDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        cardViewModel.getAll()
    }

    Scaffold(
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
            FileList(fileViewModel) {
                navController.navigate("editor/${it.id}")
            }
        }
    )

    if (showCreateFileDialog) {
        CreateFileDialog(fileViewModel) {
            showCreateFileDialog = false
        }
    }

}