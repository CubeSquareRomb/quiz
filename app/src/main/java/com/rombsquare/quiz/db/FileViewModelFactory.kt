package com.rombsquare.quiz.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FileViewModelFactory(
    private val repository: FileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FileViewModel::class.java)) {
            return FileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
