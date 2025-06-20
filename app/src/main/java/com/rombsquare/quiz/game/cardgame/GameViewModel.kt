package com.rombsquare.quiz.game.cardgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Game logic for all 3 modes
class GameViewModel(
    val cardViewModel: CardViewModel,
    val fileId: Int
) : ViewModel() {
    private var _lvl = MutableStateFlow(0)
    val lvl: StateFlow<Int> = _lvl

    private var _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private var _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards: StateFlow<List<CardEntity>> = _cards

    init {
        reset()
    }

    fun next(userAnswer: Int) {
        _score.value += userAnswer

        _lvl.value++

        if (_lvl.value >= cards.value.size) {
            return
        }
    }

    fun reset() {
        viewModelScope.launch {
            cardViewModel.clear()
            _cards.value = emptyList()
            cardViewModel.getByFileId(fileId)
            _lvl.value = 0
            _score.value = 0
            cardViewModel.cards.collect { newCards ->
                if (newCards.isEmpty()) {
                    return@collect
                }

                _cards.value = newCards.shuffled()
            }

            this.cancel()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class GameViewModelFactory(
    private val fileId: Int,
    private val cardViewModel: CardViewModel,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(cardViewModel, fileId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}