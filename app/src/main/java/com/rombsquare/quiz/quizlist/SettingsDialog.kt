package com.rombsquare.quiz.quizlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rombsquare.quiz.PrefManager
import com.rombsquare.quiz.PrefViewModel
import com.rombsquare.quiz.PrefViewModelFactory

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val prefManager = remember { PrefManager(context) }

    val prefViewModel: PrefViewModel = viewModel(
        factory = PrefViewModelFactory(prefManager)
    )

    val showAnswer by prefViewModel.showAnswer.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Settings") },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = showAnswer,
                        onCheckedChange = { prefViewModel.setShowAnswer(it) }
                    )

                    Text("Show answer after each task")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}