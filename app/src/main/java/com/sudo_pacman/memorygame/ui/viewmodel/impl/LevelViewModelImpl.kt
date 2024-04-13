package com.sudo_pacman.memorygame.ui.viewmodel.impl

import androidx.lifecycle.ViewModel
import com.sudo_pacman.memorygame.data.model.LevelEnum
import com.sudo_pacman.memorygame.repository.AppRepositoryImpl
import com.sudo_pacman.memorygame.ui.viewmodel.LevelViewModel

class LevelViewModelImpl (private val repository: AppRepositoryImpl) : ViewModel(), LevelViewModel {
    override fun selectLevel(level: LevelEnum) {
//        repository.getImagesByLevel(level)
    }
}


