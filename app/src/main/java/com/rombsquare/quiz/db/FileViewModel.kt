package com.rombsquare.quiz.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FileViewModel(private val repository: FileRepository) : ViewModel() {
    private val _files = MutableStateFlow<List<FileEntity>>(emptyList())
    val files: StateFlow<List<FileEntity>> = _files

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> = _tags

    init {
        viewModelScope.launch {
            loadAll()
        }
    }

    suspend fun getAll(): List<FileEntity> = repository.getAll()

    fun loadAll() {
        viewModelScope.launch {
            _files.value = getAll()
        }
    }

    suspend fun insert(file: FileEntity) {
        repository.insert(file)
    }

    suspend fun set(file: FileEntity) {
        repository.update(file)
    }

    fun delete(file: FileEntity) {
        viewModelScope.launch {
            repository.delete(file)
        }
    }

    fun containsTag(file: FileEntity, tag: String): Boolean {
        val tags = file.tags.split("|")
        return tag in tags
    }

    fun getAllNonTrashed() {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files
                .filter { !it.isTrashed }
                .sortedByDescending { it.isFav }
        }
    }

    fun getByTag(tag: String) {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files
                .filter { containsTag(it, tag) && !it.isTrashed }
                .sortedByDescending { it.isFav }
        }
    }

    fun getAllFav() {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files.filter { it.isFav && !it.isTrashed }
        }
    }

    fun getAllTrashed() {
        viewModelScope.launch {
            val files = getAll()
            _files.value = files
                .filter { it.isTrashed }
                .sortedByDescending { it.isFav }
        }
    }

    suspend fun getAllTags(): MutableList<String> {
        val files = getAll().filter {!it.isTrashed}
        var result = mutableListOf<String>()

        files.forEach { file ->
            if (!file.isTrashed) {
                for (tag in file.tags.split("|")) {
                    if (tag !in result) {
                        result.add(tag)
                    }
                }
            }
        }

        return result
    }

    suspend fun clearTrash() {
            val files = repository.getAll()
            val trashed = files.filter { it.isTrashed }
            trashed.forEach { file ->
                repository.delete(file)
        }
    }

    suspend fun refreshTags() {
        _tags.value = getAllTags()
    }

    fun getFiles(tag: String) {
            when (tag) {
                "all" -> getAllNonTrashed()
                "favorite" -> getAllFav()
                "trash" -> getAllTrashed()
                else -> getByTag(tag)
            }
    }
}