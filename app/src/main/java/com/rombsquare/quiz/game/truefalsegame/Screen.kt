package com.rombsquare.quiz.game.truefalsegame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rombsquare.quiz.PrefManager
import com.rombsquare.quiz.PrefViewModel
import com.rombsquare.quiz.PrefViewModelFactory
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.game.AnswerDialog
import com.rombsquare.quiz.game.EndGameDialog

@Composable
fun TrueFalseGameScreen(cardViewModel: CardViewModel, fileId: Int, taskCount: Int, navController: NavController) {
    // Get gameViewModel - the logic of the game
    val factory = remember { GameViewModelFactory(fileId, cardViewModel) }
    val gameViewModel: GameViewModel = viewModel(factory = factory)

    val context = LocalContext.current
    val prefViewModel: PrefViewModel = viewModel(factory = PrefViewModelFactory(PrefManager(context)))

    // Reset gameViewModel
    LaunchedEffect(Unit) {
        gameViewModel.reset()
    }

    // Get vars from gameViewModel
    val lvl by gameViewModel.lvl.collectAsState()
    val cards by gameViewModel.cards.collectAsState()
    val score by gameViewModel.score.collectAsState()
    val hypotheticalAnswer by gameViewModel.hypotheticalAnswer.collectAsState()
    val showAnswer by prefViewModel.showAnswer.collectAsState()

    var showAnswerDialog by remember { mutableStateOf(false) }
    var wasCorrect by remember { mutableStateOf(false) }

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    cards[lvl].side1,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Could the answer be:\n$hypotheticalAnswer?",
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Button(
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                    ,
                    onClick = {
                        wasCorrect = gameViewModel.next(true)
                        showAnswerDialog = true
                    },
                    shape = RoundedCornerShape(10),
                ) {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Yes",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(255, 138, 128, 255),
                    ),
                    onClick = {
                        wasCorrect = gameViewModel.next(false)
                        showAnswerDialog = true
                    },
                    shape = RoundedCornerShape(10),
                ) {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = "No",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }

    if (showAnswerDialog && showAnswer) {
        AnswerDialog(
            answer = if (wasCorrect) "Yes" else "No",
            isCorrect = wasCorrect
        ) {
            showAnswerDialog = false
        }
    }
}
