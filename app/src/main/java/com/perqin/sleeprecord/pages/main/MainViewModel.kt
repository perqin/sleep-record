package com.perqin.sleeprecord.pages.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import com.perqin.sleeprecord.data.models.misc.AppSettingsRepository
import com.perqin.sleeprecord.data.models.record.RecordsRepository
import java.util.*

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    val records: LiveData<List<Record>> by lazy { RecordsLiveData(
            RecordsRepository.getAllLiveRecords(),
            AppSettingsRepository.getLiveLatestSleepingTime(),
            AppSettingsRepository.getLiveShortestSleepingDuration()
    ) }
    val newRecordStart: LiveData<Long> by lazy { RecordsRepository.getLiveNewRecordStart() }
    val durationMin: LiveData<Int> by lazy { Transformations.map(AppSettingsRepository.getLiveEarliestStartTime(), {(it / 1000 / 60).toInt()}) }
    val durationMax: LiveData<Int> by lazy { Transformations.map(AppSettingsRepository.getLiveLatestEndTime(), {(it / 1000 / 60).toInt()}) }
    val durationStart: LiveData<Int> by lazy { Transformations.map(AppSettingsRepository.getLiveLatestSleepingTime(), {(it / 1000 / 60).toInt()}) }
    val durationLength: LiveData<Int> by lazy { Transformations.map(AppSettingsRepository.getLiveShortestSleepingDuration(), {(it / 1000 / 60).toInt()}) }

    fun startNewRecord() {
        RecordsRepository.startNewRecord()
    }
}

private class RecordsLiveData(
        val records: LiveData<List<com.perqin.sleeprecord.data.models.record.Record>>,
        val start: LiveData<Long>,
        val duration: LiveData<Long>) : MediatorLiveData<List<Record>>() {
    init {
        addSource(records, { refresh() })
        addSource(start, { refresh() })
        addSource(duration, { refresh() })
    }

    private fun refresh() {
        if (records.value != null && start.value != null && duration.value != null) {
            val rawRecords = records.value!!
            val start = (this.start.value!! / 1000 / 60).toInt()
            val duration = (this.duration.value!! / 1000 / 60).toInt()
            value = rawRecords.map {
                val cs = Calendar.getInstance().apply { timeInMillis = it.start }
                var ms = cs.get(Calendar.HOUR_OF_DAY) * 60 + cs.get(Calendar.MINUTE)
                val ce = Calendar.getInstance().apply { timeInMillis = it.end }
                var me = ce.get(Calendar.HOUR_OF_DAY) * 60 + ce.get(Calendar.MINUTE)
                val date = Calendar.getInstance().apply { timeInMillis = it.start }
                val dateBefore = Calendar.getInstance().apply { timeInMillis = it.start - 24 * 3600 * 1000 }
                var day = date.get(Calendar.DAY_OF_MONTH)
                if (Math.abs((ms + 24 * 60) - start) < Math.abs(ms - start)) {
                    ms += 24 * 60
                    me += 24 * 60
                    day = dateBefore.get(Calendar.DAY_OF_MONTH)
                }
                val health = if (ms <= start && me - ms >= duration) {
                    Record.Health.GOOD
                } else if (ms > start && me - ms < duration) {
                    Record.Health.BAD
                } else {
                    Record.Health.MEDIUM
                }
                Record(day, ms / 60, ms % 60, me / 60, me % 60, health)
            }
        }
    }
}
