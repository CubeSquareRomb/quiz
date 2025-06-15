package com.rombsquare.quiz.db

class CardRepository(private val dao: CardDao) {
    suspend fun insert(card: CardEntity): Long = dao.insert(card)
    suspend fun getAll(): List<CardEntity> = dao.getAll()
    suspend fun update(card: CardEntity) = dao.update(card)
    suspend fun delete(card: CardEntity) = dao.delete(card)
    suspend fun getByFileId(fileId: Int): List<CardEntity> = dao.getByFileId(fileId)
}