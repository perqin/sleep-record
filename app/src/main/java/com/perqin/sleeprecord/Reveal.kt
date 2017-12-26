package com.perqin.sleeprecord

import android.animation.Animator
import android.transition.Explode
import android.transition.TransitionValues
import android.view.View
import android.view.ViewGroup

/**
 * Created on 12/26/17.
 *
 * @author perqin
 */
class Reveal : Explode() {
    override fun onAppear(sceneRoot: ViewGroup?, view: View?, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
        println("onAppear: root = ${sceneRoot?.javaClass?.canonicalName}, view = ${view?.javaClass?.canonicalName}")
        return super.onAppear(sceneRoot, view, startValues, endValues)
    }

    override fun onDisappear(sceneRoot: ViewGroup?, view: View?, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
        println("onDisappear: root = ${sceneRoot?.javaClass?.canonicalName}, view = ${view?.javaClass?.canonicalName}")
        return super.onDisappear(sceneRoot, view, startValues, endValues)
    }
}
