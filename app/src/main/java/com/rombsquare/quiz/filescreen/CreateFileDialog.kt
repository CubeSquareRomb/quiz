package com.rombsquare.quiz.filescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rombsquare.quiz.db.FileViewModel

@Composable
fun CreateFileDialog(
    fileViewModel: FileViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create File") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                fileViewModel.insert(name)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {}
    )
}