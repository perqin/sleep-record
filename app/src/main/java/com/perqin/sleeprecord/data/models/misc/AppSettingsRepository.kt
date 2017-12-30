package com.perqin.sleeprecord.data.models.misc

/**
 * Created on 12/31/17.
 *
 * @author perqin
 */
object AppSettingsRepository {
    private val appSettingsDao = AppSettingsDao()

    fun getLiveEarliestStartTime() = appSettingsDao.earliestStartTime

    fun getLiveLatestEndTime() = appSettingsDao.latestEndTime

    fun getLiveLatestSleepingTime() = appSettingsDao.latestSleepingTime

    fun getLiveShortestSleepingDuration() = appSettingsDao.shortestSleepingDuration
}
