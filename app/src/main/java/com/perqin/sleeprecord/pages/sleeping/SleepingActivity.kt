package com.perqin.sleeprecord.pages.sleeping

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.perqin.sleeprecord.R
import kotlinx.android.synthetic.main.activity_sleeping.*

class SleepingActivity : AppCompatActivity() {
    private lateinit var sleepingVm: SleepingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleeping)

        sleepingVm = ViewModelProviders.of(this).get(SleepingViewModel::class.java)
        sleepingVm.newRecordStart.observe(this, Observer {
            if (it!! == -1L) {
                wakeUp()
            }
        })

        button_wake.setOnClickListener {
            sleepingVm.endNewRecord()
        }
    }

    private fun wakeUp() {
        finish()
    }
}
