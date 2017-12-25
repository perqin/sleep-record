package com.perqin.sleeprecord.data.models.record

import com.perqin.sleeprecord.data.room.AppDb
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.run

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
object RecordsRepository {
    private val recordsDao = AppDb.instance.recordsDao()
    private val newRecordDao = NewRecordDao()

    fun getAllLiveRecords() = recordsDao.queryAllLiveRecords()

    fun getLiveNewRecordStart() = newRecordDao.newRecordStart

    fun startNewRecord() {
        newRecordDao.startNewRecord()
    }

    fun endNewRecord() {
        launch(UI) {
            val record = newRecordDao.endNewRecord()
            run(CommonPool) { recordsDao.insertRecord(record) }
        }
    }
}
