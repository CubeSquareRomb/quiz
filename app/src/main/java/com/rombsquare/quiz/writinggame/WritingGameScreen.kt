package com.rombsquare.quiz.writinggame

import com.rombsquare.quiz.cardgame.ShowAnswerDialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import com.rombsquare.quiz.optiongame.EndGameDialog
import com.rombsquare.quiz.GameViewModel
import com.rombsquare.quiz.GameViewModelFactory

@Composable
fun WritingGameScreen(cardViewModel: CardViewModel, fileId: Int, taskCount: Int, navController: NavController) {
    val factory = remember { GameViewModelFactory(fileId, "writing", cardViewModel) }
    val gameViewModel: GameViewModel = viewModel(factory = factory)

    LaunchedEffect(Unit) {
        gameViewModel.reset()
    }

    val lvl by gameViewModel.lvl.collectAsState()
    val cards by gameViewModel.cards.collectAsState()
    val score by gameViewModel.score.collectAsState()

    var showAnswerDialog by remember { mutableStateOf(false) }

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
            .imePadding()
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
            Text(
                if (lvl in cards.indices) cards[lvl].side1 else "???",
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        AnswerInput {
            gameViewModel.next(it)
        }
    }

    if (showAnswerDialog) {
        ShowAnswerDialog(cards[lvl].side2) {
            gameViewModel.next(it)
            showAnswerDialog = false
        }
    }
}
