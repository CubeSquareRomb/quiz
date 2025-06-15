package com.rombsquare.quiz.filescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FileActionsDialog(
    fileName: String,
    onPlay: (String) -> Unit,
    onEdit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = fileName,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
        },
        text = {
            Column {
                TextButton(onClick = {onPlay("card-game")}) { Text("Card mode") }
                TextButton(onClick = {onPlay("option-game")}) { Text("Option mode") }
                TextButton(onClick = {onPlay("writing-game")}) { Text("Writing mode") }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}