package com.rombsquare.quiz.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = FileEntity::class,
            parentColumns = ["id"],
            childColumns = ["fileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["fileId"])]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val side1: String,
    val side2: String,
    val fileId: Int,
    val fixedOptions: Boolean = false,
    val incorrectOption1: String = "",
    val incorrectOption2: String = "",
    val incorrectOption3: String = "",
)