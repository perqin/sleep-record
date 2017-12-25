package com.perqin.sleeprecord.data.room

import android.arch.persistence.room.Room
import com.perqin.sleeprecord.app.App

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
object AppDb {
    val instance by lazy {
        Room.databaseBuilder(App.context, AppRoomDatabase::class.java, AppRoomDatabase.DB_NAME).build()
    }
}
