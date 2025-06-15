package com.rombsquare.quiz.optiongame

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EndGameDialog(
    score: Int,
    taskCount: Int,
    onHome: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onHome,
        title = { Text("Game Over") },
        text = {
            Text("Result: $score/$taskCount")
        },
        confirmButton = {
            Button(onClick = onHome) {
                Text("Home")
            }
        },
        dismissButton = {}
    )
}