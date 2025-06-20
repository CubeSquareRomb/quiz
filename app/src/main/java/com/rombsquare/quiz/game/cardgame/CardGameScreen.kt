package com.rombsquare.quiz.game.cardgame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.game.EndGameDialog

@Composable
fun CardGameScreen(cardViewModel: CardViewModel, fileId: Int, taskCount: Int, navController: NavController) {
    // Get gameViewModel - the logic of the game
    val factory = remember { GameViewModelFactory(fileId, cardViewModel) }
    val gameViewModel: GameViewModel = viewModel(factory = factory)

    // Reset gameViewModel
    LaunchedEffect(Unit) {
        gameViewModel.reset()
    }

    // Get vars from gameViewModel
    val lvl by gameViewModel.lvl.collectAsState()
    val cards by gameViewModel.cards.collectAsState()
    val score by gameViewModel.score.collectAsState()

    var showAnswerDialog by remember { mutableStateOf(false) }

    // If cards haven't loaded yet
    if (cards.size < 4) {
        Text("Loading...")
        return
    }

    // End game condition
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
                textAlign = TextAlign.Center,
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
            ,
            onClick = {
                showAnswerDialog = true
            }
        ) {
            Text("Show Answer")
        }
    }

    // Show AnswerDialog
    if (showAnswerDialog) {
        ShowAnswerDialog(cards[lvl].side2) {
            gameViewModel.next(it)
            showAnswerDialog = false
        }
    }
}
