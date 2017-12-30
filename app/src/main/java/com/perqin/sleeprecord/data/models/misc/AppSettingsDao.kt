package com.perqin.sleeprecord.data.models.misc

import android.preference.PreferenceManager
import com.perqin.sleeprecord.app.App
import com.perqin.sleeprecord.utils.livedata.longLiveData

private const val PK_EARLIEST_START_TIME = "earliestStartTime"
private const val PK_LATEST_END_TIME = "latestEndTime"
private const val PK_LATEST_SLEEPING_TIME = "latestSleepingTime"
private const val PK_SHORTEST_SLEEPING_DURATION = "shortestSleepingDuration"

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class AppSettingsDao {
    private val sp by lazy { PreferenceManager.getDefaultSharedPreferences(App.context) }
    val earliestStartTime by lazy { sp.longLiveData(PK_EARLIEST_START_TIME, 21 * 3600 * 1000) }                 // default to 9:00 P.M.
    val latestEndTime by lazy { sp.longLiveData(PK_LATEST_END_TIME, (24 + 15) * 3600 * 1000) }                  // default to 3:00 P.M. the next day
    val latestSleepingTime by lazy { sp.longLiveData(PK_LATEST_SLEEPING_TIME, 24 * 3600 * 1000) }               // default to 12:00 A.M. the next day
    val shortestSleepingDuration by lazy { sp.longLiveData(PK_SHORTEST_SLEEPING_DURATION, 7 * 3600 * 1000) }    // default to 7 hours
}
