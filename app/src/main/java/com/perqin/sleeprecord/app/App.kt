package com.perqin.sleeprecord.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}
