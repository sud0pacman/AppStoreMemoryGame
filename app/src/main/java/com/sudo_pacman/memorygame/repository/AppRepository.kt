package com.sudo_pacman.memorygame.repository

import com.sudo_pacman.memorygame.data.model.CardData
import com.sudo_pacman.memorygame.data.model.LevelEnum
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun checkOpens(openedImages: ArrayList<Int>) : Boolean
    fun getImagesByLevel(level: LevelEnum): Flow<List<CardData>>
}