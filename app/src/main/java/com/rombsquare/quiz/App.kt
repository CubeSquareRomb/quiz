package com.rombsquare.quiz

import android.app.Application
import androidx.core.content.edit
import androidx.room.Room
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.FileEntity
import com.rombsquare.quiz.db.QuizDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    lateinit var database: QuizDatabase
        private set

    private val appScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java,
            "quiz_db"
        )
        .build()

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            appScope.launch {
                val fileDao = database.fileDao()
                val file = FileEntity(
                    name = "Example Quiz"
                )
                val fileId = fileDao.insert(file).toInt()

                val cardDao = database.cardDao()

                val cards = listOf<List<String>>(
                    listOf("What's the color of the cherry?", "Red"),
                    listOf("What's the color of the lava?", "Orange"),
                    listOf("What's the color of the sand?", "Yellow"),
                    listOf("What's the color of the grass?", "Green"),
                    listOf("What's the color of the sky?", "Cyan"),
                    listOf("What's the color of the sea?", "Blue"),
                    listOf("What's the rarest color for the flags?", "Purple"),
                    listOf("What's the color of rose?", "Pink"),
                    listOf("What's the color of black pants?", "Black"),
                    listOf("What's the color of sadness", "Gray"),
                    listOf("What's the color of the paper?", "White"),
                    listOf("What's the color of the ground?", "Brown")
                )

                cards.forEach {cardDao.insert(CardEntity(
                    side1 = it[0],
                    side2 = it[1],
                    fileId = fileId,
                    fixedOptions = it.size > 2,
                    incorrectOption1 = it.getOrNull(2) ?: "",
                    incorrectOption2 = it.getOrNull(3) ?: "",
                    incorrectOption3 = it.getOrNull(4) ?: "",
                ))}
                prefs.edit { putBoolean("isFirstLaunch", false) }
            }.start()
        }
    }
}