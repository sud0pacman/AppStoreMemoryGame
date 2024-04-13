package com.sudo_pacman.memorygame

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.memorygame.databinding.ActivityMainBinding
import com.sudo_pacman.memorygame.utils.myLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        "activty log".myLog()
    }
}