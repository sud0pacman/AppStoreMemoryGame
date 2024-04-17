package com.sudo_pacman.memorygame.utils

import android.animation.ObjectAnimator.*
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.sudo_pacman.memorygame.R
import com.sudo_pacman.memorygame.data.model.CardData
import com.sudo_pacman.memorygame.data.model.LevelEnum
import com.sudo_pacman.memorygame.ui.screens.LevelScreenDirections
import timber.log.Timber

fun String.myLog(tag: String = "TTT") = Timber.tag(tag).d(this)

fun ImageView.animateBounce() {
    val bounceAnimator = ofFloat(this, "translationY", 0f, 0f, 0f)
    bounceAnimator.interpolator = BounceInterpolator()
    bounceAnimator.duration = 1000 // Set the duration of the animation
    bounceAnimator.start()
}


fun ImageView.openImage() {
    animate()
        .setDuration(350)
        .rotationY(89f)
        .withEndAction {
            setImageResource((this.tag as CardData).resID)
            rotationY = -89f

            animate()
                .setDuration(350)
                .rotationY(0f)
                .withEndAction {
                }
                .start()
        }
        .start()
}

fun ImageView.openImage(endAnim: () -> Unit) {
    animate()
        .setDuration(350)
        .rotationY(89f)
        .withEndAction {
            setImageResource((this.tag as CardData).resID)
            rotationY = -89f

            animate()
                .setDuration(350)
                .rotationY(0f)
                .withEndAction {
                    endAnim.invoke()
                }
                .start()
        }
        .start()
}

fun ImageView.closeImage() {
    this.apply {
        animate()
            .setDuration(350)
            .rotationY(89f)
            .withEndAction {
                setImageResource(R.drawable.quiz)
                rotationY = -89f

                animate()
                    .setDuration(350)
                    .rotationY(0f)
                    .withEndAction {
                    }
                    .start()
            }
            .start()
    }

}

fun ImageView.closeImage(endAnim: () -> Unit) {
    animate()
        .setDuration(350)
        .rotationY(89f)
        .withEndAction {
            setImageResource(R.drawable.quiz)
            rotationY = -89f

            animate()
                .setDuration(350)
                .rotationY(0f)
                .withEndAction {
                    endAnim.invoke()
                }
                .start()
        }
        .start()
}

fun ImageView.hideAnim() {
    this.animate()
        .setDuration(1000)
        .alpha(0.5f)
//        .scaleX(0f)
//        .scaleY(0f)
        .start()
}

fun ImageView.hideAnim(endAnim: () -> Unit) {
    this.animate()
        .setDuration(1000)
        .alpha(0.5f)
//        .scaleX(0f)
//        .scaleY(0f)
        .withEndAction(endAnim)
        .start()
}


fun ConstraintLayout.clickLevelButton(click: () -> Unit) {
    this.animate()
        .scaleX(0.7f)
        .setDuration(350)
        .scaleY(0.7f)
        .withEndAction {
            this.animate()
                .setDuration(90)
                .scaleY(1f)
                .scaleX(1f)
                .withEndAction {
                    click.invoke()
                }
                .start()
        }
        .start()
}
