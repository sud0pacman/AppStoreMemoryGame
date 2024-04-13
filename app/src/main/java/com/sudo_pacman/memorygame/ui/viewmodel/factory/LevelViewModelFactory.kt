package com.sudo_pacman.memorygame.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sudo_pacman.memorygame.repository.AppRepositoryImpl
import com.sudo_pacman.memorygame.ui.viewmodel.impl.LevelViewModelImpl

class LevelViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LevelViewModelImpl::class.java))
            return LevelViewModelImpl(AppRepositoryImpl.getInstance()) as T
        throw IllegalArgumentException("")
    }
}