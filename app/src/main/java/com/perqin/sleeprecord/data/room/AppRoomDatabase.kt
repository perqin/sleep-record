package com.perqin.sleeprecord.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.perqin.sleeprecord.data.models.record.Record
import com.perqin.sleeprecord.data.models.record.RecordsDao

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
@Database(entities = [(Record::class)], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun recordsDao(): RecordsDao

    companion object {
        val DB_NAME = "app_db"
    }
}
