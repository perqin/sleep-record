package com.perqin.sleeprecord.utils.dateandtime

import java.text.DateFormat
import java.util.*

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */

fun currentTimestamp() = Calendar.getInstance().timeInMillis

fun timestampToLocalTime(timestamp: Long) = DateFormat.getDateTimeInstance().format(timestamp)
