package com.perqin.sleeprecord

import android.animation.Animator
import android.content.Context
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup

/**
 * Created on 12/26/17.
 *
 * @author perqin
 */
class Reveal(context: Context, attrs: AttributeSet) : Visibility(context, attrs) {
    override fun onAppear(sceneRoot: ViewGroup?, view: View, startValues: android.transition.TransitionValues?, endValues: android.transition.TransitionValues?): Animator {
        return ViewAnimationUtils.createCircularReveal(view, view.width / 2, view.height / 2, 0F, 1000F)
    }
}
