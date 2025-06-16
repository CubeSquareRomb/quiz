package com.rombsquare.quiz.editorscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No cards yet",
                fontSize = 20.sp
            )
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
    }

}