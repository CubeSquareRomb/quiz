package com.rombsquare.quiz.filescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rombsquare.quiz.db.FileEntity
import com.rombsquare.quiz.db.FileViewModel

@Composable
fun FileList(
    viewModel: FileViewModel,
    onFileClick: (FileEntity) -> Unit
) {
    val files by viewModel.files.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    if (files.isEmpty()) {
        Text("No files yet")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            itemsIndexed(files) { index, file ->
                Text(
                    text = file.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFileClick(file) }
                        .padding(vertical = 28.dp, horizontal = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                if (index < files.lastIndex) {
                    HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha=0.5f))
                }
            }
        }
    }
}