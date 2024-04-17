package com.sudo_pacman.memorygame.ui.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.memorygame.R
import com.sudo_pacman.memorygame.databinding.ScreenSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScreenSplash : Fragment(R.layout.screen_splash) {
    private val binding by viewBinding(ScreenSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.loadingProgressBar.max = 100

        lifecycleScope.launch {

            delay(450)

            for (i in 0..100) {
                binding.loadingProgressBar.progress = i
                delay(7)
            }

            findNavController().navigate(ScreenSplashDirections.actionScreenSplashToLevelScreen())
        }
    }
}