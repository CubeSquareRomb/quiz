package com.rombsquare.quiz.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CardEntity::class, FileEntity::class],
    version = 3
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun fileDao(): FileDao
}