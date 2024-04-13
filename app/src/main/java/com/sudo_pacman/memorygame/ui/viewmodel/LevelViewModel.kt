package com.sudo_pacman.memorygame.ui.viewmodel

import com.sudo_pacman.memorygame.data.model.LevelEnum

interface LevelViewModel {
    fun selectLevel(level: LevelEnum)
}
