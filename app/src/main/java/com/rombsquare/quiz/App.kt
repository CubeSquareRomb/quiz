package com.rombsquare.quiz

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rombsquare.quiz.db.QuizDatabase

class App : Application() {

    lateinit var database: QuizDatabase
        private set

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
    }
}