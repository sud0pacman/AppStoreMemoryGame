package com.sudo_pacman.memorygame.ui.viewmodel

import androidx.lifecycle.LiveData
import com.sudo_pacman.memorygame.data.model.CardData
import com.sudo_pacman.memorygame.data.model.LevelEnum
import kotlinx.coroutines.flow.MutableSharedFlow

interface GameViewModel {
    val images: MutableSharedFlow<List<CardData>>
    var message: LiveData<Boolean>
    val close: LiveData<Int>
    val canClick: LiveData<Boolean>
    val hide: LiveData<Int>

    fun checkMatchingCards(clickView: CardData, clickedIndex: Int)
    fun loadImages(level: LevelEnum)

    fun restartGame()
    val open: LiveData<Int>
    val closeWithAction: LiveData<Int>
    val openAction: LiveData<Int>
    val hideWithAnim: LiveData<Int>
}

