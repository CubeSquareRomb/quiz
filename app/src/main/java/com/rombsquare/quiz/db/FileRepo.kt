package com.rombsquare.quiz.db

class FileRepository(private val dao: FileDao) {
    suspend fun insert(file: FileEntity): Long = dao.insert(file)
    suspend fun getAll(): List<FileEntity> = dao.getAll()
    suspend fun update(file: FileEntity) = dao.update(file)
    suspend fun delete(file: FileEntity) = dao.delete(file)
}