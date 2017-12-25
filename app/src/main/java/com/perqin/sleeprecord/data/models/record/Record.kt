package com.perqin.sleeprecord.data.models.record

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
@Entity(tableName = Record.TABLE_NAME)
data class Record(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = UID)
        var uid: Long? = null,
        @ColumnInfo(name = START)
        var start: Long = 0,
        @ColumnInfo(name = END)
        var end: Long = 0
) {
    companion object {
        const val TABLE_NAME = "records"
        const val UID = "uid"
        const val START = "start"
        const val END = "end"
    }
}
