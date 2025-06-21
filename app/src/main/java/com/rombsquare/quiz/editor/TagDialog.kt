package com.rombsquare.quiz.editor

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
import androidx.compose.ui.text.font.FontWeight
import com.rombsquare.quiz.db.FileEntity

@Composable
fun TagDialog(
    file: FileEntity,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(
        if(file.tags.isEmpty()) "" else "#" + file.tags.replace("|", " #")
    ) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tags") },
        text = {
            Column {
                Text("Edit your tags to this quiz. Example:")
                Text("#biology #hard #flora", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Tags") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val list = text
                    .substring(1)
                    .replace(" ", "")
                    .split("#")

                list.forEach {
                    if (it.length > 15) {
                        Toast.makeText(context, "Some tags are too long", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    if (it in listOf("all", "favorite", "trash")) {
                        Toast.makeText(context, "Some tags have special names", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    if (it.isEmpty()) {
                        Toast.makeText(context, "Empty tags", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                }

                onConfirm(text
                    .substring(1)
                    .replace(" ", "")
                    .replace("#", "|")
                )
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