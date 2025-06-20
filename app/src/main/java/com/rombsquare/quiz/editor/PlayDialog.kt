package com.rombsquare.quiz.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PlayDialog(
    onPlay: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Choose mode")},
        text = {
            Column {
                TextButton(onClick = {onPlay("card-game")}) { Text("Training mode") }
                TextButton(onClick = {onPlay("option-game")}) { Text("Option mode") }
                TextButton(onClick = {onPlay("writing-game")}) { Text("Writing mode") }
                TextButton(onClick = {onPlay("true-false-game")}) { Text("True/False mode") }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}