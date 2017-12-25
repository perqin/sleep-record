package com.perqin.sleeprecord.pages.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.perqin.sleeprecord.data.models.record.Record
import com.perqin.sleeprecord.data.models.record.RecordsRepository

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    val records: LiveData<List<Record>> by lazy { RecordsRepository.getAllLiveRecords() }
}
