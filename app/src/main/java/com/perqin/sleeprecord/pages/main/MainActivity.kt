package com.perqin.sleeprecord.pages.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.perqin.sleeprecord.R
import com.perqin.sleeprecord.pages.sleeping.SleepingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainVm: MainViewModel
    private lateinit var recordsRecyclerAdapter: RecordsRecyclerAdapter

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
                goToSleep()
            }
        })

        recordsRecyclerAdapter = RecordsRecyclerAdapter()

        recyclerView.adapter = recordsRecyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener {
            mainVm.startNewRecord()
        }
    }

    private fun goToSleep() {
        startActivity(Intent(this, SleepingActivity::class.java))
    }
}
