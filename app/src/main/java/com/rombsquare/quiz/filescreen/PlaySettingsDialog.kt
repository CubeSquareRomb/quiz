package com.rombsquare.quiz.filescreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PlaySettingsDialog(
    onChooseAllCards: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val context = LocalContext.current
    var taskCountString by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onChooseAllCards,
        title = { Text("Play Settings") },
        text = {
            Column {
                OutlinedTextField(
                    value = taskCountString,
                    onValueChange = {
                        taskCountString = it
                    },
                    label = { Text("Task Count") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (taskCountString.isEmpty()) {
                    Toast.makeText(context, "Enter the task count", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                if (taskCountString.toInt() < 4) {
                    Toast.makeText(context, "Minimal number of tasks: 4", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                onConfirm(taskCountString.toInt())
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onChooseAllCards) {
                Text("Choose All Cards")
            }
        }
    )
}
