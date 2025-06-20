package com.rombsquare.quiz.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel

@Composable
fun EditorCardList(
    modifier: Modifier,
    viewModel: CardViewModel,
    onCardClick: (CardEntity) -> Unit,
    onAddClick: () -> Unit,
    fileId: Int
) {
    val cards by viewModel.cards.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getByFileId(fileId)
    }

    if (cards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onAddClick
            ) {
                Text("Add first card")
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        itemsIndexed(cards) { index, card ->
            if (card.fileId == fileId) {
                EditorCard(
                    card = card,
                    onClick = onCardClick,
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
                ,
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onAddClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF35343A)),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }

}