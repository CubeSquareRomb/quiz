package com.rombsquare.quiz.optiongame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rombsquare.quiz.GameViewModel
import com.rombsquare.quiz.GameViewModelFactory
import com.rombsquare.quiz.db.CardViewModel

@Composable
fun OptionGameScreen(cardViewModel: CardViewModel, fileId: Int, taskCount: Int, navController: NavController) {
    val factory = remember { GameViewModelFactory(fileId, "options", cardViewModel) }
    val gameViewModel: GameViewModel = viewModel(factory = factory)

    LaunchedEffect(Unit) {
        gameViewModel.reset()
    }

    val lvl by gameViewModel.lvl.collectAsState()
    val options by gameViewModel.optionIndices.collectAsState()
    val cards by gameViewModel.cards.collectAsState()
    val score by gameViewModel.score.collectAsState()

    if (cards.size < 4) {
        Text("Only ${cards.size} cards! Loading...")
        return;
    }

    if (cards[0].fileId != fileId) {
        Text("Cards from other files found! Waiting for clearing it...")
        return
    }

    if ((taskCount == -1 && lvl == cards.size) || (taskCount != -1 && lvl == taskCount)) {
        EndGameDialog(score, if(taskCount == -1) cards.size else taskCount) {
            navController.navigate("main")
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Task #${lvl+1}",
                fontSize = 24.sp,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(if (lvl in cards.indices) cards[lvl].side1 else "???")
        }

        Spacer(modifier = Modifier.weight(1f))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(options) { i, optionIndex ->
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                    ,
                    onClick = {
                        gameViewModel.next(i)
                }) {
                    var text: String
                    if (cards[lvl].fixedOptions) {
                        text = when (optionIndex) {
                            1 -> cards[lvl].incorrectOption1
                            2 -> cards[lvl].incorrectOption2
                            3 -> cards[lvl].incorrectOption3
                            else -> cards[lvl].side2
                        }
                    } else {
                        text = cards[optionIndex].side2
                    }

                    Text(text)

                }
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
            }
        }
    }
}
