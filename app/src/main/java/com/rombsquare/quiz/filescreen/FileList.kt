package com.rombsquare.quiz.filescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.quiz.db.FileEntity
import com.rombsquare.quiz.db.FileViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FileList(
    fileViewModel: FileViewModel,
    onFileClick: (FileEntity) -> Unit
) {
    val files by fileViewModel.files.collectAsState()
//    val cards by cardViewModel.cards.collectAsState()
//
//    val cardCounts = remember(files, cards) {
//        files.map { file ->
//            cards.count { card -> card.fileId == file.id }
//        }
//    }

    if (files.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No quizzes yet",
                fontSize = 20.sp
            )
        }
        return
    }

    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
    ) {

            itemsIndexed(files) { index, file ->
                FileItem(file/*, cardCounts.getOrNull(index) ?: 0*/) {
                    onFileClick(file)
                }

                if (index != files.lastIndex) {
                    HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha=0.5f))
                }

        }

    }
}