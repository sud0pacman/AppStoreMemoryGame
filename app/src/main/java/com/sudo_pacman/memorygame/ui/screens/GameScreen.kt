package com.sudo_pacman.memorygame.ui.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.memorygame.data.model.CardData
import com.sudo_pacman.memorygame.data.model.LevelEnum
import com.sudo_pacman.memorygame.ui.viewmodel.factory.GameViewModelFactory
import com.sudo_pacman.memorygame.ui.viewmodel.impl.GameViewModelImpl
import com.sudo_pacman.memorygame.R
import com.sudo_pacman.memorygame.databinding.ScreenGameBinding
import com.sudo_pacman.memorygame.utils.closeImage
import com.sudo_pacman.memorygame.utils.hideAnim
import com.sudo_pacman.memorygame.utils.myLog
import com.sudo_pacman.memorygame.utils.openImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class GameScreen : Fragment(R.layout.screen_game) {
    private val viewModel: GameViewModelImpl by viewModels { GameViewModelFactory() }
    private val binding: ScreenGameBinding by viewBinding(ScreenGameBinding::bind)
    private var x = 0
    private var y = 0
    private val views = ArrayList<ImageView>()
    private var cardWidth = 0f
    private var cardHeight = 0f
    private var centerX = 0f
    private var centerY = 0f
    private val navArgs by navArgs<GameScreenArgs>()
    private var findCards = 0
    private var canClick = true
    private lateinit var level: LevelEnum
    private lateinit var job: Job
    private lateinit var progressJob: Job
    private var i: Int = 0
    private val MAX_TIME by lazy {
        when (level) {
            LevelEnum.EASY -> 100
            LevelEnum.MEDIUM -> 200
            LevelEnum.HARD -> 300
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        "create".myLog()

        level = navArgs.level

        i =
            if (level == LevelEnum.EASY) 100
            else if (level == LevelEnum.MEDIUM) 200
            else 300

        binding.time.text = i.toString()

        val progressBar = binding.horizontalProgressBar
        progressBar.max = i
        job = lifecycleScope.launch {
            while (i > 0) {
                progressBar.progress = i
                i--
                binding.time.text = i.toString()
                if (i == 0) {
                    Toast.makeText(requireContext(), "Game Over", Toast.LENGTH_SHORT).show()
                    showGameOverDialog()
                }
                delay(1000L)
            }
        }

        // ekran chizildi
        binding.container.post {
            // x y pozitsiya topildi
            x = level.rowCount
            y = level.columnCount

            cardWidth = binding.container.width.toFloat() / x
            cardHeight = binding.container.height.toFloat() / y

            centerX = binding.container.width / 2f
            centerY = binding.container.height / 2f


            "x: $x, y: $y".myLog()

            viewModel.loadImages(level) // viewmodelga rasmlar keldi
        }

        viewModel.images.onEach {
            loadViews(it)
        }
            .launchIn(lifecycleScope)

        viewModel.open.observe(viewLifecycleOwner, openObserver)
        viewModel.openAction.observe(viewLifecycleOwner, openWithActionObserver)

        viewModel.close.observe(viewLifecycleOwner, closeObserver)
        viewModel.closeWithAction.observe(viewLifecycleOwner, closeWithActionObserver)

        viewModel.hide.observe(viewLifecycleOwner, hideObserver)
        viewModel.hideWithAnim.observe(viewLifecycleOwner, hideWithAnimObserver)

//        binding.reload.setOnClickListener {
//            binding.container.removeAllViews()
//            viewModel.restartGame()
//            viewModel.loadImages(level)
//        }
//
//        binding.menu.setOnClickListener {
//            findNavController().popBackStack()
//        }
    }

    private fun showGameOverDialog() {

    }

    private fun loadViews(images: List<CardData>) {
        val margin = 20 // Margin value

        val availableCardWidth = (cardWidth - margin)
        val availableCardHeight = (cardHeight - margin)

        for (i in 0 until x) {
            for (j in 0 until y) {

                val image = ImageView(requireContext())

                val lp = ConstraintLayout.LayoutParams(
                    availableCardWidth.toInt(),
                    availableCardWidth.toInt()
                )

                image.setPadding(20, 30, 20, 30,)

                image.layoutParams = lp

                image.setBackgroundResource(R.drawable.bg_card)

                image.x = centerX - cardWidth / 2
                image.y = centerY - cardHeight / 2

                image.animate()
                    .setDuration(1000)
                    .x((i * cardWidth) + (margin / 2))
                    .y((j * cardHeight) + (margin / 2))

                image.setImageResource(R.drawable.quiz)

                binding.container.addView(image)

                image.tag = images[i * level.columnCount + j]

                views.add(image)
            }
        }

        clickEvent()
    }

    private fun clickEvent() {
        views.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (!canClick) return@setOnClickListener
                viewModel.checkMatchingCards(imageView.tag as CardData, index)
            }
        }
    }


    private val openObserver = Observer<Int> {
        views[it].openImage()
    }

    private val openWithActionObserver = Observer<Int> {
        views[it].openImage {
            canClick = false
        }
    }


    private val closeObserver = Observer<Int> {
        views[it].closeImage()
    }

    private val closeWithActionObserver = Observer<Int> {
        views[it].closeImage {
            canClick = true
        }
    }

    private val hideObserver = Observer<Int> {
        views[it].hideAnim()

        views[it].apply {
            isClickable = false
            isFocusableInTouchMode = false
            isFocusable = false
        }

    }


    private val hideWithAnimObserver = Observer<Int> { index ->
        views[index].hideAnim {
            canClick = true
            ++findCards
            isFinish()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                views[index].apply {
                    isClickable = false
                    isFocusableInTouchMode = false
                    isFocusable = false
                }
            }, 100) // Adjust the delay time as needed
        }
    }


    private fun isFinish() {
        if (findCards == x * y)
            Toast.makeText(requireContext(), "Finish", Toast.LENGTH_SHORT).show()
    }
}