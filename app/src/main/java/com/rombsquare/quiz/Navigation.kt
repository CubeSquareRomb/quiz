package com.rombsquare.quiz

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rombsquare.quiz.game.cardgame.CardGameScreen
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.db.FileViewModel
import com.rombsquare.quiz.editor.EditorScreen
import com.rombsquare.quiz.quizlist.FileScreen
import com.rombsquare.quiz.game.optiongame.OptionGameScreen
import com.rombsquare.quiz.game.truefalsegame.TrueFalseGameScreen
import com.rombsquare.quiz.game.writinggame.WritingGameScreen

@Composable
fun NavApp(fileViewModel: FileViewModel, cardViewModel: CardViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            FileScreen(cardViewModel, fileViewModel, navController)
        }

        composable(
            "editor/{fileId}",
            arguments = listOf(navArgument("fileId") { type = NavType.StringType })
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getString("fileId")?.toInt()!!
            EditorScreen(navController, fileId, cardViewModel, fileViewModel)
        }

        composable(
            "option-game/{fileId}/{taskCount}",
            arguments = listOf(
                navArgument("fileId") { type = NavType.IntType },
                navArgument("taskCount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getInt("fileId") ?: 0
            val taskCount = backStackEntry.arguments?.getInt("taskCount") ?: -1

            OptionGameScreen(cardViewModel, fileId, taskCount, navController)
        }

        composable(
            "card-game/{fileId}/{taskCount}",
            arguments = listOf(
                navArgument("fileId") { type = NavType.IntType },
                navArgument("taskCount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getInt("fileId") ?: 0
            val taskCount = backStackEntry.arguments?.getInt("taskCount") ?: -1

            CardGameScreen(cardViewModel, fileId, taskCount, navController)
        }

        composable(
            "writing-game/{fileId}/{taskCount}",
            arguments = listOf(
                navArgument("fileId") { type = NavType.IntType },
                navArgument("taskCount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getInt("fileId") ?: 0
            val taskCount = backStackEntry.arguments?.getInt("taskCount") ?: -1

            WritingGameScreen(cardViewModel, fileId, taskCount, navController)
        }

        composable(
            "true-false-game/{fileId}/{taskCount}",
            arguments = listOf(
                navArgument("fileId") { type = NavType.IntType },
                navArgument("taskCount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getInt("fileId") ?: 0
            val taskCount = backStackEntry.arguments?.getInt("taskCount") ?: -1

            TrueFalseGameScreen(cardViewModel, fileId, taskCount, navController)
        }
    }
}
