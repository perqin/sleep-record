package com.perqin.sleeprecord.data.models.record

import android.preference.PreferenceManager
import com.perqin.sleeprecord.app.App
import com.perqin.sleeprecord.utils.dateandtime.currentTimestamp
import com.perqin.sleeprecord.utils.livedata.longLiveData
import java.util.*

private const val PK_NEW_RECORD_START = "newRecordStart"

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class NewRecordDao {
    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(App.context) }
    val newRecordStart by lazy { sp.longLiveData(PK_NEW_RECORD_START, -1) }

    fun startNewRecord() {
        val ts = Calendar.getInstance().timeInMillis
        sp.edit()
                .putLong(PK_NEW_RECORD_START, ts)
                .apply()
    }

    fun endNewRecord(): Record {
        val start = newRecordStart.value!!
        val end = currentTimestamp()
        sp.edit()
                .remove(PK_NEW_RECORD_START)
                .apply()
        return Record(null, start, end)
    }
}
