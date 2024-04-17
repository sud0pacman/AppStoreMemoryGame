package com.sudo_pacman.memorygame.ui.screens

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.memorygame.data.model.CardData
import com.sudo_pacman.memorygame.data.model.LevelEnum
import com.sudo_pacman.memorygame.ui.viewmodel.factory.GameViewModelFactory
import com.sudo_pacman.memorygame.ui.viewmodel.impl.GameViewModelImpl
import com.sudo_pacman.memorygame.R
import com.sudo_pacman.memorygame.databinding.ScreenGameBinding
import com.sudo_pacman.memorygame.utils.MyMusicPlayer
import com.sudo_pacman.memorygame.utils.MyPref
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
    private var timeTotal: Int = 0
    private var levelInt = 1
    private var attempts = 0
    private lateinit var player: MyMusicPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        "create".myLog()

//        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setupOnBackPressed()

        level = navArgs.level

        timeTotal =
            when (level) {
                LevelEnum.EASY -> 100
                LevelEnum.MEDIUM -> 200
                else -> 300
            }

        playerSet()

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
            progressInit()
            loadViews(it)
            delay(4000)
            clickEvent()
            timerInitAndStart()
        }
            .launchIn(lifecycleScope)

        viewModel.open.observe(viewLifecycleOwner, openObserver)
        viewModel.openAction.observe(viewLifecycleOwner, openWithActionObserver)

        viewModel.close.observe(viewLifecycleOwner, closeObserver)
        viewModel.closeWithAction.observe(viewLifecycleOwner, closeWithActionObserver)

        viewModel.hide.observe(viewLifecycleOwner, hideObserver)
        viewModel.hideWithAnim.observe(viewLifecycleOwner, hideWithAnimObserver)

        binding.reload.setOnClickListener {
//            binding.container.removeAllViews()
//            viewModel.restartGame()
//            viewModel.loadImages(level)
//            timerInitAndStart()
        }

        binding.menu.setOnClickListener {
            backDialog()
        }

        binding.level.text = "${MyPref.getInstance().getLevel(level)}"
        levelInt = MyPref.getInstance().getLevel(level)
    }


    private fun playerSet() {
        player = MyMusicPlayer.getInstance()
        val pref = MyPref.getInstance()

        if (pref.getMusicState()) {
            binding.volume.setImageResource(R.drawable.sound_on)
        } else {
            binding.volume.setImageResource(R.drawable.sound_off)
        }
        binding.containerVolume.setOnClickListener {
            if (pref.getMusicState()) {
                player.manageMusic()
                binding.volume.setImageResource(R.drawable.sound_off)
            } else {
                player.manageMusic()
                binding.volume.setImageResource(R.drawable.sound_on)
            }
        }
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

                var paddingVertical = 30
                var paddingHorizontal = 20

                when (level) {
                    LevelEnum.EASY -> {
                        paddingHorizontal = 50
                        paddingVertical = 40
                    }

                    LevelEnum.MEDIUM -> {
                        paddingHorizontal = 40
                        paddingVertical = 30
                    }

                    else -> {}
                }

                image.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

                image.layoutParams = lp

                image.setBackgroundResource(R.drawable.bg_card)

                image.x = centerX - cardWidth / 2
                image.y = centerY - cardHeight / 2

                image.isEnabled = false

                image.animate()
                    .setDuration(1000)
                    .x((i * cardWidth) + (margin / 2) + binding.space.x)
                    .y((j * cardHeight) + (margin / 2) + binding.space.y)
                    .withEndAction {
                        image.openImage {
                            lifecycleScope.launch {
                                delay(1200L)
                                image.closeImage()
                                image.isEnabled = true
                            }
                        }
                    }

                image.setImageResource(R.drawable.quiz)

                binding.root.addView(image)

                image.tag = images[i * level.columnCount + j]

                views.add(image)
            }
        }
    }


    private fun clickEvent() {
        views.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (!canClick) return@setOnClickListener
                viewModel.checkMatchingCards(imageView.tag as CardData, index)
            }
        }
    }

    private fun progressInit() {
        binding.time.text = timeTotal.toString()

        val progressBar = binding.horizontalProgressBar
        progressBar.max = timeTotal
        progressBar.progress = timeTotal
    }

    private fun timerInitAndStart() {
        job = lifecycleScope.launch {
            while (timeTotal > 0) {
                timeTotal--
                binding.horizontalProgressBar.progress = timeTotal
                binding.time.text = timeTotal.toString()
                if (timeTotal == 0) {
                    Toast.makeText(requireContext(), "Game Over", Toast.LENGTH_SHORT).show()
                    showGameOverDialog()
                    job.cancel()
                }
                delay(1000L)
            }
        }
    }

    private fun showGameOverDialog() {
        val dialog = Dialog(requireContext())

        binding.container.removeAllViews()

        dialog.setContentView(R.layout.dialog_win)

        dialog.setCancelable(false)

        dialog.findViewById<TextView>(R.id.level).text = "$levelInt"
        dialog.findViewById<TextView>(R.id.attempts).text = "$attempts"

        dialog.findViewById<LinearLayout>(R.id.home).setOnClickListener {
            dialog.dismiss()
            findNavController().navigateUp()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.findViewById<LinearLayout>(R.id.next).setOnClickListener {
            dialog.dismiss()

//            binding.container.removeAllViews()

            timeTotal =
                when (level) {
                    LevelEnum.EASY -> 100
                    LevelEnum.MEDIUM -> 200
                    else -> 300
                }

            viewModel.restartGame()
            viewModel.loadImages(level)
            timerInitAndStart()
        }

        dialog.show()
    }


    private val openObserver = Observer<Int> {
        player.startOpenCardMusic()
        views[it].openImage()
    }

    private val openWithActionObserver = Observer<Int> {
        player.startOpenCardMusic()
        views[it].openImage {
            canClick = false
        }
    }


    private val closeObserver = Observer<Int> {
        views[it].closeImage()
    }

    private val closeWithActionObserver = Observer<Int> {
        player.startCloseCardMusic()

        views[it].closeImage {
            ++attempts
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
        player.startRemoveCardMusic()

        views[index].hideAnim {
            canClick = true
            ++attempts
            ++findCards

            "findcard -> $findCards,  x*y=${x * y}".myLog()

            isFinish()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                views[index].apply {
                    isClickable = false
                    isFocusableInTouchMode = false
                    isFocusable = false
                }
            }, 50) // Adjust the delay time as needed
        }
    }


    private fun isFinish() {
        if (findCards == (x * y) / 2) {
            job.cancel()

            ++levelInt
            binding.level.text = "$levelInt"
            showGameOverDialog()
            Toast.makeText(requireContext(), "Finish", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        MyPref.getInstance().saveLevel(level, levelInt)
        super.onStop()
    }

    private fun backDialog() {
        if (::job.isInitialized) job.cancel()

        val dialog = Dialog(requireContext())

        dialog.setContentView(R.layout.dialog_exit)

        dialog.setCancelable(false)



        dialog.findViewById<TextView>(R.id.yes_exit).setOnClickListener {
            dialog.dismiss()
            findNavController().navigateUp()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.attributes?.gravity = Gravity.CENTER

        dialog.findViewById<TextView>(R.id.no_exit).setOnClickListener {
            timerInitAndStart()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupOnBackPressed() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                job.cancel()

                val dialog = Dialog(requireContext())

                dialog.setContentView(R.layout.dialog_exit)

                dialog.setCancelable(false)



                dialog.findViewById<TextView>(R.id.yes_exit).setOnClickListener {
                    dialog.dismiss()
                    findNavController().navigateUp()
                }

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                dialog.window?.attributes?.gravity = Gravity.CENTER

                dialog.findViewById<TextView>(R.id.no_exit).setOnClickListener {
                    timerInitAndStart()
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


}