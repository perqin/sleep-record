package com.perqin.sleeprecord.pages.sleeping

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.perqin.sleeprecord.data.models.record.RecordsRepository

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class SleepingViewModel(application: Application) : AndroidViewModel(application) {
    val newRecordStart: LiveData<Long> by lazy { RecordsRepository.getLiveNewRecordStart() }

    fun endNewRecord() {
        RecordsRepository.endNewRecord()
    }
}
