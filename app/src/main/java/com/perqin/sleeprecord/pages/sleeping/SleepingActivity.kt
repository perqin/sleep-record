package com.perqin.sleeprecord.pages.sleeping

import android.animation.Animator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import com.perqin.sleeprecord.R
import kotlinx.android.synthetic.main.activity_sleeping.*
import kotlin.math.max

class SleepingActivity : AppCompatActivity() {
    private lateinit var sleepingVm: SleepingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleeping)

        sleepingVm = ViewModelProviders.of(this).get(SleepingViewModel::class.java)
        sleepingVm.newRecordStart.observe(this, Observer {
            if (it!! == -1L) {
                button_wake.post { wakeUp() }
            }
        })

        button_wake.setOnClickListener {
            sleepingVm.endNewRecord()
        }
    }

    private fun wakeUp() {
        val buttonLoc = IntArray(2)
        val revealedLoc = IntArray(2)
        button_wake.getLocationOnScreen(buttonLoc)
        view_revealed.getLocationOnScreen(revealedLoc)
        val centerX = buttonLoc[0] - revealedLoc[0] + button_wake.width / 2
        val centerY = buttonLoc[1] - revealedLoc[1] + button_wake.height / 2
        val maxRadius = max(constraintLayout_root.width, constraintLayout_root.height)
        view_revealed.visibility = View.VISIBLE
        ViewAnimationUtils.createCircularReveal(view_revealed, centerX, centerY, 0F, maxRadius.toFloat()).apply { addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }

            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        }) }.start()
    }
}
