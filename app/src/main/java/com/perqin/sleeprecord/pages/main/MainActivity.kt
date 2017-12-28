package com.perqin.sleeprecord.pages.main

import android.animation.Animator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import com.perqin.sleeprecord.R
import com.perqin.sleeprecord.pages.sleeping.SleepingActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    private lateinit var mainVm: MainViewModel
    private lateinit var recordsRecyclerAdapter: RecordsRecyclerAdapter

    /**
     * Decide whether the fab will do stuff.
     * To avoid fab tapped twice, we have to disable it during handling the tap (that is, showing animation).
     * However, the FAB's elevation will be set to 0 when disabled, which causes the fAB shown below the Toolbar.
     * Thus, we use this field instead of FAB's own enabled or not.
     */
    private var fabEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if (fabEnabled) mainVm.startNewRecord()
        }
    }

    private fun goToSleep() {
        val changeViews = {
            fabEnabled = false
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
            Handler().postDelayed({
                view_revealed.visibility = View.GONE
            }, resources.getInteger(R.integer.duration_activityTransition).toLong())
            fabEnabled = true
            fab.layoutParams = (fab.layoutParams as CoordinatorLayout.LayoutParams).apply { gravity = Gravity.BOTTOM or GravityCompat.END }
        }

        val at = AutoTransition().apply { addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                reveal({
                    resetViews()
                    startActivity(Intent(this@MainActivity, SleepingActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
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
