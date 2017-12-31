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
import android.widget.TextView
import com.perqin.sleeprecord.R
import com.perqin.sleeprecord.app.App
import com.perqin.sleeprecord.pages.sleeping.SleepingActivity
import com.perqin.sleeprecord.utils.ui.DurationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    private lateinit var mainVm: MainViewModel
    private lateinit var recordsRecyclerAdapter: RecordsRecyclerAdapter
    private lateinit var settingsDurationViewHolder: SettingsDurationViewHolder
    private lateinit var healthTipsTextViewHolder: HealthTipsTextViewHolder

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
        mainVm.records.observe(this, Observer { recordsRecyclerAdapter.records = it!! })
        mainVm.newRecordStart.observe(this, Observer {
            if (it!! != -1L) {
                // New record created
                fab.post { goToSleep() }
            }
        })
        mainVm.durationMin.observe(this, Observer {
            it!!
            val hours = it / 60
            val minutes = it % 60
            settingsDurationViewHolder.min = it
            textView_earliestStartTime.text = App.context.getString(if (hours >= 24) R.string.textView_main_timeNextDay else R.string.textView_main_timeToday, hours % 24, minutes)
            recordsRecyclerAdapter.durationMin = it
        })
        mainVm.durationMax.observe(this, Observer {
            it!!
            val hours = it / 60
            val minutes = it % 60
            settingsDurationViewHolder.max = it
            textView_latestEndTime.text = App.context.getString(if (hours >= 24) R.string.textView_main_timeNextDay else R.string.textView_main_timeToday, hours % 24, minutes)
            recordsRecyclerAdapter.durationMax = it
        })
        mainVm.durationStart.observe(this, Observer {
            it!!
            settingsDurationViewHolder.durationStart = it
            healthTipsTextViewHolder.latestSleepingTime = it
        })
        mainVm.durationLength.observe(this, Observer {
            it!!
            settingsDurationViewHolder.durationLength = it
            healthTipsTextViewHolder.shortestDuration = it
        })

        recordsRecyclerAdapter = RecordsRecyclerAdapter()
        settingsDurationViewHolder = SettingsDurationViewHolder(durationView_settings)
        healthTipsTextViewHolder = HealthTipsTextViewHolder(textView_healthTips)

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
            ViewAnimationUtils.createCircularReveal(view_revealed, centerX, centerY, 0F, max(view_revealed.width, view_revealed.height).toFloat()).apply {
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

    inner class SettingsDurationViewHolder(private val durationView: DurationView) {
        var min: Int = 0
            set(value) {
                field = value
                refresh()
            }
        var max: Int = 0
            set(value) {
                field = value
                refresh()
            }
        var durationStart: Int = 0
            set(value) {
                field = value
                refresh()
            }
        var durationLength: Int = 0
            set(value) {
                field = value
                refresh()
            }

        private fun refresh() {
            durationView.min = min
            durationView.max = max
            durationView.durationStart = durationStart
            durationView.durationEnd = durationStart + durationLength
        }
    }

    inner class HealthTipsTextViewHolder(private val textView: TextView) {
        var latestSleepingTime: Int = 0
            set(value) {
                field = value
                refresh()
            }
        var shortestDuration: Int = 0
            set(value) {
                field = value
                refresh()
            }

        private fun refresh() {
            val hours = latestSleepingTime / 60
            val minutes = latestSleepingTime % 60
            textView.text = App.context.getString(
                    R.string.textView_main_healthTips,
                    App.context.getString(if (hours >= 24) R.string.textView_main_timeNextDay else R.string.textView_main_timeToday, hours % 24, minutes),
                    shortestDuration / 60,
                    shortestDuration % 60
            )
        }
    }
}
