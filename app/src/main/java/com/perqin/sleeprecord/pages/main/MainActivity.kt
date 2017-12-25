package com.perqin.sleeprecord.pages.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.perqin.sleeprecord.R

class MainActivity : AppCompatActivity() {
    lateinit var mainVm: MainViewModel
    lateinit var recordsRecyclerAdapter: RecordsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainVm = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainVm.records.observe(this, Observer {
            recordsRecyclerAdapter.update(it!!)
        })

        recordsRecyclerAdapter = RecordsRecyclerAdapter()
    }
}
