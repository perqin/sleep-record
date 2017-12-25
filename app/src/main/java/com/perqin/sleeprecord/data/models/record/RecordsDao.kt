package com.perqin.sleeprecord.data.models.record

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
@Dao
interface RecordsDao {
    @Query("SELECT * FROM ${Record.TABLE_NAME} ORDER BY ${Record.START} DESC")
    fun queryAllLiveRecords(): LiveData<List<Record>>
}
