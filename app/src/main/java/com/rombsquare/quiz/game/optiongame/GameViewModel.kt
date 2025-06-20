package com.rombsquare.quiz.game.optiongame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    val cardViewModel: CardViewModel,
    val fileId: Int,
) : ViewModel() {
    private var _lvl = MutableStateFlow(0)
    val lvl: StateFlow<Int> = _lvl

    private var _optionIndices = MutableStateFlow(List(4) {0})
    val optionIndices: StateFlow<List<Int>> = _optionIndices

    private var _correctOption = MutableStateFlow(0)

    private var _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private var _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards: StateFlow<List<CardEntity>> = _cards

    init {
        reset()
    }

    fun next(userAnswer: Int): Boolean {
        var isCorrect = false

        if (userAnswer == _correctOption.value) {
            _score.value++
            isCorrect = true
        } else {
            isCorrect = false
        }

        _lvl.value++

        if (_lvl.value >= cards.value.size) {
            return false
        }

        update()
        return isCorrect
    }

    fun update() {
        val currentCard = _cards.value[_lvl.value]
        if (currentCard.fixedOptions) {
            _optionIndices.value = mutableListOf<Int>(0, 1, 2, 3).shuffled()
            _correctOption.value = _optionIndices.value.indexOf(0)
        } else {
            val optionOutput = generateOptions(
                _cards.value.size,
                4,
                _lvl.value,
                _cards.value.mapIndexedNotNull { index, value -> if (value.fixedOptions) index else null }
            )

            _optionIndices.value = optionOutput.first
            _correctOption.value = optionOutput.second
        }
    }

    fun reset() {
        viewModelScope.launch {
            cardViewModel.clear()
            _cards.value = emptyList()
            cardViewModel.getByFileId(fileId)
            cardViewModel.cards.collect { newCards ->
                if (newCards.isEmpty()) {
                    return@collect
                }

                _cards.value = newCards.shuffled()
                _lvl.value = 0

                update()

                this.cancel()
            }
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