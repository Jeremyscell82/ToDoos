package com.lloydsbyte.todoos.utilz

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import kotlin.math.hypot
import kotlin.math.max

class RevealViewAnimator {

    companion object {
        fun revealView(layoutView: View, startPointX: Int, startPointY: Int, animDuration: Long) {

            val startRadius = 0f
            val endRadius = hypot(layoutView.width.toDouble(), layoutView.height.toDouble()).toFloat()

            val anim: Animator = ViewAnimationUtils.createCircularReveal(
                layoutView,
                startPointX,
                startPointY,
                startRadius,
                endRadius
            )

            layoutView.visibility = View.VISIBLE
            anim.duration = animDuration
            anim.start()

        }

        fun hideView(layoutView: View, endPointX: Int, endPointY: Int, animDuration: Long) {

            val startRadius: Int = max(layoutView.width, layoutView.height)
            val endRadius = 0

            val anim = ViewAnimationUtils.createCircularReveal(
                layoutView,
                endPointX,
                endPointY,
                startRadius.toFloat(),
                endRadius.toFloat()
            )
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                }
                override fun onAnimationEnd(animator: Animator) {
                    layoutView.visibility = View.INVISIBLE
                }
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            anim.duration = animDuration
            anim.start()
        }
    }

}