package com.rombsquare.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel
import com.rombsquare.quiz.optiongame.generateOptions
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Game logic for all 3 modes
class GameViewModel(
    val cardViewModel: CardViewModel,
    val fileId: Int,
    val mode: String
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

    fun next(userAnswer: Int) {
        if (mode == "options") {
            if (userAnswer == _correctOption.value) {
                _score.value++
            }
        } else if (mode == "cards") {
            _score.value += userAnswer
        }

        _lvl.value++

        if (_lvl.value >= cards.value.size) {
            return
        }

        if (mode != "options") {
            return
        }

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

    // NEXT method for writing mode
    fun next(userAnswer: String) {
        if (userAnswer == _cards.value[_lvl.value].side2) {
            _score.value++
        }

        _lvl.value++
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

                val currentCard = _cards.value[0]
                if (currentCard.fixedOptions) {
                    _optionIndices.value = mutableListOf<Int>(0, 1, 2, 3).shuffled()
                    _correctOption.value = _optionIndices.value.indexOf(0)
                    return@collect
                }
                val optionOutput = generateOptions(
                    _cards.value.size,
                    4,
                    0,
                    _cards.value.mapIndexedNotNull { index, value -> if (value.fixedOptions) index else null }
                )
                _optionIndices.value = optionOutput.first
                _correctOption.value = optionOutput.second

                this.cancel()
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class GameViewModelFactory(
    private val fileId: Int,
    private val mode: String,
    private val cardViewModel: CardViewModel,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(cardViewModel, fileId, mode) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}