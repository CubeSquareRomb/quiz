package com.rombsquare.quiz.editorscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateCardDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var side1 by remember { mutableStateOf("") }
    var side2 by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create your card") },
        text = {
            Column {
                OutlinedTextField(
                    value = side1,
                    onValueChange = { side1 = it },
                    label = { Text("Question: ") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = side2,
                    onValueChange = { side2 = it },
                    label = { Text("Answer: ") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(side1, side2)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}