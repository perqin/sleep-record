package com.perqin.sleeprecord.data.models.record

import com.perqin.sleeprecord.data.room.AppDb

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
object RecordsRepository {
    private val recordsDao = AppDb.instance.recordsDao()

    fun getAllLiveRecords() = recordsDao.queryAllLiveRecords()
}