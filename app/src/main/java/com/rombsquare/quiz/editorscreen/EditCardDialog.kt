package com.rombsquare.quiz.editorscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rombsquare.quiz.db.CardEntity

@Composable
fun EditCardDialog(
    currentCard: CardEntity,
    onConfirm: (Boolean, String, String, String, String, String) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var side1 by remember { mutableStateOf(currentCard.side1) }
    var side2 by remember { mutableStateOf(currentCard.side2) }
    var incorrectOption1 by remember { mutableStateOf(currentCard.incorrectOption1) }
    var incorrectOption2 by remember { mutableStateOf(currentCard.incorrectOption2) }
    var incorrectOption3 by remember { mutableStateOf(currentCard.incorrectOption3) }
    var fixedOptions by remember { mutableStateOf(currentCard.fixedOptions) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Card") },
        text = {
            Column {
                OutlinedTextField(
                    value = side1,
                    onValueChange = { side1 = it },
                    label = { Text("Change question") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = side2,
                    onValueChange = { side2 = it },
                    label = { Text("Change answer") }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = fixedOptions,
                        onCheckedChange = { fixedOptions = it }
                    )

                    Text("Fixed Options")
                }


                if (!fixedOptions) {
                    return@Column
                }

                OutlinedTextField(
                    value = incorrectOption1,
                    onValueChange = { incorrectOption1 = it },
                    label = { Text("Incorrect option #1") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = incorrectOption2,
                    onValueChange = { incorrectOption2 = it },
                    label = { Text("Incorrect option #2") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = incorrectOption3,
                    onValueChange = { incorrectOption3 = it },
                    label = { Text("Incorrect option #3") }
                )

            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(fixedOptions, side1, side2, incorrectOption1, incorrectOption2, incorrectOption3)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Text("Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Discard")
                }
            }
        }
    )
}