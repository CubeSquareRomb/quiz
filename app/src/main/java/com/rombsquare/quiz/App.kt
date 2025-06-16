package com.rombsquare.quiz

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rombsquare.quiz.db.QuizDatabase
import androidx.core.content.edit
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.FileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    lateinit var database: QuizDatabase
        private set

    private val appScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE cards ADD COLUMN fixedOptions INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE cards ADD COLUMN incorrectOption1 TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE cards ADD COLUMN incorrectOption2 TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE cards ADD COLUMN incorrectOption3 TEXT NOT NULL DEFAULT ''")
            }
        }


        database = Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java,
            "quiz_db"
        )
        .addMigrations(MIGRATION_1_2)
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
                    listOf("What is 2+2", "4"),
                    listOf("What is 1000 - 3 - 4", "993"),
                    listOf("What's the best subject?", "Math", "Chemistry", "Physics", "Biology"),
                    listOf("Find the x: 10x = 5", "0.5"),
                    listOf("0.9 + 0.1", "1"),
                    listOf("Find root sum: x^2 - 5x + 6 = 0", "5"),
                    listOf("Cubic root of 1000", "10"),
                    listOf("Find the derivative of f(x) = x^10", "10x^9"),
                    listOf("Find the antiderivative of f(x) = x^10", "x^11 / 11"),
                    listOf("Product of ALL real numbers", "0"),
                    listOf("Sum of ALL real number + 17", "17"),
                    listOf("Find the 6!", "720"),
                    listOf("(-1) - (-1) - 1", "-1"),
                    listOf("Which equation has the solution 2 and -7?", "(x-2)(x+7) = 0", "(x+2)(x-7) = 0", "(x-2)(y-7) = 0", "(x+2)(y+7) = 0")
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