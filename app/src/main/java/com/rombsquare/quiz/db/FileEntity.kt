package com.rombsquare.quiz.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "<empty>",
    val tags: String = "",
    val isFav: Boolean = false,
    val isTrashed: Boolean = false
)