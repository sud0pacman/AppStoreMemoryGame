package com.sudo_pacman.memorygame.app

import android.app.Application
import com.sudo_pacman.memorygame.utils.MyMusicPlayer
import com.sudo_pacman.memorygame.utils.MyPref
import timber.log.Timber

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        MyPref.init(this)
        MyMusicPlayer.init(this)
        Timber.plant(Timber.DebugTree())
    }
}