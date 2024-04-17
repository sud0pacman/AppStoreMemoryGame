package com.sudo_pacman.memorygame

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sudo_pacman.memorygame.utils.myLog


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val decorView = window.decorView

        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions

        val a = object :CountDownTimer(10000L, 1000L){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {

            }

        }

        a.start()


        "activty log".myLog()
    }
}