package com.rombsquare.quiz.cardgame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ShowAnswerDialog(
    answer: String,
    onDismiss: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = {onDismiss(0)},
        title = {
            Text("Answer: $answer", fontSize = 20.sp)
        },
        text = {
            Column {
                Text("Is your answer you think correct?")
            }

        },
        confirmButton = {
            Row {
                TextButton(onClick = {onDismiss(1)}) { Text("Yes") }
                TextButton(onClick = {onDismiss(0)}) { Text("No") }
            }
        },
        dismissButton = {}
    )
}