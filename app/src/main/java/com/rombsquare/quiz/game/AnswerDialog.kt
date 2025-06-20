package com.rombsquare.quiz.game

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun AnswerDialog(
    answer: String,
    isCorrect: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Answer: $answer", fontSize = 20.sp)
        },
        text = {
            if (isCorrect) {
                Text("Correct!")
            } else {
                Text("Your answer was incorrect")
            }

        },
        confirmButton = {
            Row {
                TextButton(onClick = onDismiss) { Text("Got it") }
            }
        },
        dismissButton = {}
    )
}