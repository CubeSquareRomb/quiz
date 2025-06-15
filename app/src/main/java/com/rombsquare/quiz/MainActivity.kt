package com.rombsquare.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rombsquare.quiz.db.CardRepository
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.db.CardViewModelFactory
import com.rombsquare.quiz.db.FileRepository
import com.rombsquare.quiz.db.FileViewModel
import com.rombsquare.quiz.db.FileViewModelFactory
import com.rombsquare.quiz.ui.theme.QuizTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val fileDao = (this.applicationContext as App).database.fileDao()
            val fileRepo = FileRepository(fileDao)
            val fileViewModel: FileViewModel = viewModel(
                factory = FileViewModelFactory(fileRepo)
            )

            val cardDao = (this.applicationContext as App).database.cardDao()
            val cardRepo = CardRepository(cardDao)
            val cardViewModel: CardViewModel = viewModel(
                factory = CardViewModelFactory(cardRepo)
            )


            QuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { it ->
                    NavApp(fileViewModel, cardViewModel)
                }
            }
        }
    }
}









