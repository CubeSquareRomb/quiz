package com.rombsquare.quiz

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PrefManager(context: Context) {
    private val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun getShowAnswer(): Boolean = prefs.getBoolean("show_answer", false)
    fun setShowAnswer(state: Boolean) = prefs.edit { putBoolean("show_answer", state) }
}

class PrefViewModel(private val prefManager: PrefManager) : ViewModel() {
    private val _showAnswer = MutableStateFlow(prefManager.getShowAnswer())
    val showAnswer: StateFlow<Boolean> = _showAnswer

    fun setShowAnswer(state: Boolean) {
        _showAnswer.value = state
        prefManager.setShowAnswer(state)
    }
}

@Suppress("UNCHECKED_CAST")
class PrefViewModelFactory(
    private val prefManager: PrefManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrefViewModel::class.java)) {
            return PrefViewModel(prefManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
