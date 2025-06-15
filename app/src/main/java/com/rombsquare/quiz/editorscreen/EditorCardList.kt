package com.rombsquare.quiz.editorscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel

@Composable
fun EditorCardList(
    modifier: Modifier,
    viewModel: CardViewModel,
    onCardClick: (CardEntity) -> Unit,
    fileId: Int
) {
    val cards by viewModel.cards.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getByFileId(fileId)
    }

    if (cards.isEmpty()) {
        Text("No cards yet")
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                if (card.fileId == fileId) {
                    EditorCard(
                        card = card,
                        onClick = onCardClick,
                    )
                }
            }
        }
    }
}