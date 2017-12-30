package com.perqin.sleeprecord.pages.main

/**
 * Created on 12/31/17.
 *
 * DTO for {@link com.perqin.sleeprecord.data.models.record.Record}.
 *
 * @author perqin
 */
data class Record(
        val day: Int,
        val startH: Int,
        val startM: Int,
        val endH: Int,
        val endM: Int,
        val health: Health
) {
    val start = startH * 60 + startM
    val end = endH * 60 + endM

    enum class Health {
        GOOD,
        MEDIUM,
        BAD
    }
}
