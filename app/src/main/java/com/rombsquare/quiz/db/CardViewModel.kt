package com.rombsquare.quiz.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {
    private val _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards: StateFlow<List<CardEntity>> = _cards

    fun getAll() {
        viewModelScope.launch {
            _cards.value = repository.getAll()
        }
    }

    fun clear() {
        _cards.value = emptyList()
    }

    fun insert(card: CardEntity) {
        viewModelScope.launch {
            repository.insert(card)
            getAll()
        }
    }

    fun set(card: CardEntity) {
        viewModelScope.launch {
            repository.update(card)
            getAll()
        }
    }

    fun delete(card: CardEntity) {
        viewModelScope.launch {
            repository.delete(card)
            getAll()
        }
    }

    fun getByFileId(fileId: Int) {
        viewModelScope.launch {
            _cards.value = repository.getByFileId(fileId)
        }
    }
}