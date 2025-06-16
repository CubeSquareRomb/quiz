package com.rombsquare.quiz.optiongame

// Generates options by task count, option count and current task index
// Returns a list of task indices as options, and correct option index inside option list
fun generateOptions(taskCount: Int, optionCount: Int, currentTaskIndex: Int, bannedIndices: List<Int> = emptyList<Int>()): Pair<MutableList<Int>, Int> {
    val indices: MutableList<Int> = mutableListOf()

    var i = 0
    var randomIndex = 0

    while (i < optionCount-1) {
        randomIndex = (0 until taskCount).random()

        if (bannedIndices.contains(randomIndex)
            && (taskCount - bannedIndices.size >= optionCount) // Add banned indices to the result if insufficient options
        ) {
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