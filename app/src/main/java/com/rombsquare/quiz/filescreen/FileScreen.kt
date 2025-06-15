package com.rombsquare.quiz.filescreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.db.FileEntity
import com.rombsquare.quiz.db.FileViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FileScreen(cardViewModel: CardViewModel, viewModel: FileViewModel, navController: NavController) {
    val context = LocalContext.current
    var selectedFile by remember { mutableStateOf<FileEntity?>(null) }
    var selectedMode by remember { mutableStateOf("")}

    var showActionDialog by remember { mutableStateOf(false) }
    var showPlaySettingsDialog by remember { mutableStateOf(false) }
    var showCreateFileDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            PlusFab {showCreateFileDialog = true}
        },
        content = {
            FileList(viewModel) {
                selectedFile = it
                cardViewModel.getByFileId(it.id)
                showActionDialog = true
            }
        }
    )

    if (showActionDialog && selectedFile != null) {
        FileActionsDialog(
            fileName = selectedFile!!.name,
            onPlay = {
                selectedMode = it
                cardViewModel.getByFileId(selectedFile!!.id)
                if (cardViewModel.cards.value.size < 4) {
                    Toast.makeText(context, "You need at least 4 cards", Toast.LENGTH_SHORT).show()
                    return@FileActionsDialog
                }
                showActionDialog = false
                showPlaySettingsDialog = true

            },
            onEdit = {
                navController.navigate("editor/${selectedFile!!.id}")
                showActionDialog = false
            },
            onDismiss = { showActionDialog = false }
        )
    }

    if (showPlaySettingsDialog) {
        PlaySettingsDialog(
            onChooseAllCards = {
                navController.navigate("${selectedMode}/${selectedFile!!.id}/-1")
                showPlaySettingsDialog = false
            }
        ) {
            navController.navigate("${selectedMode}/${selectedFile!!.id}/$it")
            showPlaySettingsDialog = false
        }
    }

    if (showCreateFileDialog) {
        CreateFileDialog(viewModel) {
            showCreateFileDialog = false
        }
    }

}

@Composable
fun Toast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}