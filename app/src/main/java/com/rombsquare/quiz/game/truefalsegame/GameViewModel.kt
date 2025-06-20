package com.rombsquare.quiz.game.truefalsegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rombsquare.quiz.db.CardEntity
import com.rombsquare.quiz.db.CardViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Game view model for the True/False mode
class GameViewModel(
    val cardViewModel: CardViewModel,
    val fileId: Int,
) : ViewModel() {
    private var _lvl = MutableStateFlow(0)
    val lvl: StateFlow<Int> = _lvl

    private var _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private var _answer = MutableStateFlow(false)
    val answer: StateFlow<Boolean> = _answer

    private var _hypotheticalAnswer = MutableStateFlow("")
    val hypotheticalAnswer: StateFlow<String> = _hypotheticalAnswer

    private var _card = MutableStateFlow(CardEntity(0, "", "", fileId))
    val card: StateFlow<CardEntity> = _card

    private var _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards: StateFlow<List<CardEntity>> = _cards

    init {
        reset()
    }

    fun update() {
        _answer.value = (0..1).random() == 1

        _card.value = _cards.value[_lvl.value]
        if (_answer.value) {
            _hypotheticalAnswer.value = _card.value.side2
            return
        }

        if (_card.value.fixedOptions) {
            if (_answer.value) {
                _hypotheticalAnswer.value = _card.value.side2
            } else {
                _hypotheticalAnswer.value = listOf(_card.value.incorrectOption1, _card.value.incorrectOption2, _card.value.incorrectOption3).random()
            }
        } else {
            val incorrectAnswers = _cards.value.map {it.side2}.filter {it != _card.value.side2}
            _hypotheticalAnswer.value = incorrectAnswers.random()
        }
    }

    fun next(userAnswer: Boolean) {
        if (userAnswer == _answer.value) {
            _score.value++
        }

        _lvl.value++

        if (_lvl.value < _cards.value.size) {
            update()
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