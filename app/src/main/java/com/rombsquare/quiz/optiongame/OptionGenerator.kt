package com.rombsquare.quiz.optiongame

import com.rombsquare.quiz.db.CardEntity

fun generateOptions_(tasks: List<CardEntity>, options: Int, currentTaskIndex: Int): Pair<MutableList<Int>, Int> {
    val indices: MutableList<Int> = mutableListOf()

    var i = 0
    var randomIndex = 0
    while (i < options-1) {
        randomIndex = (0 until tasks.size).random()

        if (tasks[randomIndex].fixedOptions && (tasks.filter {!it.fixedOptions}).size >= options) {
            continue
        }

        if (!indices.contains(randomIndex) &&
            tasks[randomIndex].side1 != tasks[currentTaskIndex].side1)
        {
            indices += randomIndex
            i++
        }
    }

    val correctOptionIndex: Int = (0..<options).random()
    indices.add(correctOptionIndex, currentTaskIndex)

    return Pair(indices, correctOptionIndex)
}

// Generates options by task count, option count and current task index
// Returns a list of task indices as options, and correct option index inside option list
fun generateOptions(taskCount: Int, optionCount: Int, currentTaskIndex: Int, bannedIndices: List<Int> = emptyList<Int>()): Pair<MutableList<Int>, Int> {
    val indices: MutableList<Int> = mutableListOf()

    var i = 0
    var randomIndex = 0

    while (i < optionCount-1) {
        randomIndex = (0 until taskCount).random()

        if (bannedIndices.contains(randomIndex)) {
            continue
        }

        if (!indices.contains(randomIndex) && randomIndex != currentTaskIndex) {
            indices += randomIndex
            i++
        }
    }

    val correctOptionIndex: Int = (0..<optionCount).random()
    indices.add(correctOptionIndex, currentTaskIndex)

    return Pair(indices, correctOptionIndex)
}

fun generateOptionsByCards(cardList: List<CardEntity>, optionCount: Int, currentCard: CardEntity): Pair<MutableList<CardEntity>, Int> {
    val (options, correct) = generateOptions(cardList.size, optionCount, cardList.indexOf(currentCard))
    var optionList = mutableListOf<CardEntity>()

    options.forEach {
        optionList += cardList[it]
    }

    return Pair(optionList, correct)
}

fun main() {
    val (options, correct) = generateOptions(7, 4, 3, listOf(1, 5, 4))
    println(options)
    println(correct)
}