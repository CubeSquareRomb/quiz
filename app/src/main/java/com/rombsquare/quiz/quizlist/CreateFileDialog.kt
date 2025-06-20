package com.rombsquare.quiz.quizlist

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import com.rombsquare.quiz.db.FileViewModel

@Composable
fun CreateFileDialog(
    fileViewModel: FileViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Quiz") },
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
                if (name.isEmpty()) {
                    Toast.makeText(context, "Enter a new quiz name", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }

                fileViewModel.insert(name)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {}
    )
}