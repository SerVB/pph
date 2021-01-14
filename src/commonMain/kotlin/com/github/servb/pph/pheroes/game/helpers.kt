package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.TextResId
import com.soywiz.klock.DateTime
import com.soywiz.klock.Month
import com.soywiz.klock.nanoseconds
import com.soywiz.korio.lang.format
import kotlin.math.roundToLong

/** https://docs.microsoft.com/en-us/windows/win32/api/minwinbase/ns-minwinbase-filetime. */
private val fileTimeOrigin = DateTime(1601, Month.January, 1)
private val fileTimeMultiplier = 600_000_000uL

fun UInt.timestampToDateTime(): DateTime {
    val fileTime = (fileTimeMultiplier * this).toLong()
    val nanos = (fileTime * 100.0).nanoseconds  // can't use integer 100 because overflow will happen
    return fileTimeOrigin + nanos
}

fun DateTime.toTimestamp(): UInt {
    val delta = this - fileTimeOrigin
    val nanos = delta.nanoseconds
    return ((nanos / 100).roundToLong().toULong() / fileTimeMultiplier).toUInt()
}

fun FormatDate(timestamp: UInt, bShowTime: Boolean): String {
    val dt = timestamp.timestampToDateTime()

    val day = dt.dayOfMonth
    val month = gTextMgr[TextResId.TRID_MONTH_JAN.v + dt.month0]
    val year = dt.yearInt

    return when (bShowTime) {
        true -> "%d %s %d (%d:%02d)".format(day, month, year, dt.hours, dt.minutes)
        false -> "%d %s %d".format(day, month, year)
    }
}
