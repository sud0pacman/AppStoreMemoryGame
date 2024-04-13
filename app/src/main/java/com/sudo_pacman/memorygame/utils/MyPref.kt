package com.sudo_pacman.memorygame.utils

import android.annotation.SuppressLint
import android.content.Context
import com.sudo_pacman.memorygame.data.model.LevelEnum

class MyPref private constructor() {
    private val pref by lazy { context.getSharedPreferences("MEMORY_GAME", Context.MODE_PRIVATE) }
    private val editor by lazy { pref.edit() }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: MyPref

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        fun getInstanceForMusic(context: Context): MyPref {
            if (!(::instance.isInitialized)) {
                instance = MyPref()
                this.context = context
            }
            return instance
        }

        fun init (context: Context){
            this.context = context
        }
        fun getInstance(): MyPref{
            if (!(::instance.isInitialized)){
                instance = MyPref()
            }

            return instance
        }
    }

    fun onOrOffMusic(state: Boolean) {
        editor.putBoolean("MUSIC", state).apply()
    }

    fun getMusicState(): Boolean {
        return pref.getBoolean("MUSIC", true)
    }

    fun getLevel(defLevel: LevelEnum): Int {
        return when (defLevel) {
            LevelEnum.EASY -> pref.getInt("EASY", 1)
            LevelEnum.MEDIUM -> pref.getInt("MEDIUM", 1)
            LevelEnum.HARD -> pref.getInt("HARD", 1)
        }
    }

    fun saveLevel(levelEnum: LevelEnum, level: Int) {
        when (levelEnum) {
            LevelEnum.EASY -> {
                editor.putInt("EASY", level).apply()
            }

            LevelEnum.MEDIUM -> {
                editor.putInt("MEDIUM", level).apply()
            }

            LevelEnum.HARD -> editor.putInt("HARD", level).apply()
        }
    }
}