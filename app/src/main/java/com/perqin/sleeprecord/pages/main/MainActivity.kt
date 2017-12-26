package com.perqin.sleeprecord.pages.main

import android.animation.Animator
import android.app.ActivityOptions
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.AutoTransition
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import com.perqin.sleeprecord.R
import com.perqin.sleeprecord.pages.sleeping.SleepingActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    private lateinit var mainVm: MainViewModel
    private lateinit var recordsRecyclerAdapter: RecordsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.enterTransition = Fade().apply { removeTarget("revealed") }
        window.exitTransition = Fade().apply { removeTarget("revealed") }
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainVm = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainVm.records.observe(this, Observer {
            recordsRecyclerAdapter.updateRecords(it!!)
        })
        mainVm.newRecordStart.observe(this, Observer {
            if (it!! != -1L) {
                // New record created
                fab.post { goToSleep() }
            }
        })

        recordsRecyclerAdapter = RecordsRecyclerAdapter()

        recyclerView.adapter = recordsRecyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener {
            mainVm.startNewRecord()
        }
    }

    override fun onResume() {
        super.onResume()
        view_revealed.visibility = View.GONE
    }
//
//    override fun onStop() {
//        super.onStop()
//        view_revealed.visibility = View.GONE
//    }

    private fun goToSleep() {
        val changeViews = {
            fab.isEnabled = false
            fab.layoutParams = (fab.layoutParams as CoordinatorLayout.LayoutParams).apply { gravity = Gravity.TOP or GravityCompat.END }
        }
        val reveal = { done: () -> Unit ->
            val fabLoc = IntArray(2)
            val revealedLoc = IntArray(2)
            fab.getLocationOnScreen(fabLoc)
            view_revealed.getLocationOnScreen(revealedLoc)
            val centerX = fabLoc[0] - revealedLoc[0] + fab.width / 2
            val centerY = fabLoc[1] - revealedLoc[1] + fab.height / 2
            view_revealed.visibility = View.VISIBLE
            ViewAnimationUtils.createCircularReveal(view_revealed, centerX, centerY, 0F, max(frameLayout_root.width, frameLayout_root.height).toFloat()).apply {
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        done()
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })
            }.start()
        }
        val resetViews = {
            fab.isEnabled = true
            fab.layoutParams = (fab.layoutParams as CoordinatorLayout.LayoutParams).apply { gravity = Gravity.BOTTOM or GravityCompat.END }
        }

        val at = AutoTransition().apply { addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                reveal({
                    resetViews()
                    startActivity(Intent(this@MainActivity, SleepingActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
                })
            }

            override fun onTransitionResume(transition: Transition?) {}
            override fun onTransitionPause(transition: Transition?) {}
            override fun onTransitionCancel(transition: Transition?) {}
            override fun onTransitionStart(transition: Transition?) {}
        }) }
        TransitionManager.beginDelayedTransition(coordinatorLayout, at)
        changeViews()
    }
}
