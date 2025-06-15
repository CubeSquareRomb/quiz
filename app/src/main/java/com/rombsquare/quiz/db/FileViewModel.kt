package com.rombsquare.quiz.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FileViewModel(private val repository: FileRepository) : ViewModel() {
    private val _files = MutableStateFlow<List<FileEntity>>(emptyList())
    val files: StateFlow<List<FileEntity>> = _files

    init {
        viewModelScope.launch {
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _files.value = repository.getAll()
        }
    }

    fun insert(name: String) {
        viewModelScope.launch {
            val newFile = FileEntity(name = name)
            repository.insert(newFile)
            refresh()
        }
    }

    fun set(file: FileEntity) {
        viewModelScope.launch {
            repository.update(file)
            refresh()
        }
    }

    fun delete(file: FileEntity) {
        viewModelScope.launch {
            repository.delete(file)
            refresh()
        }
    }
}