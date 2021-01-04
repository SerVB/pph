package com.github.servb.pph.gxlib

import com.soywiz.klock.DateTime

// todo: using UTC, is it OK? (can cause problems when outputting dates to screen, check it)
fun GetTickCount(): UInt = DateTime.now().unixMillisLong.toUInt()

class iTimer {

    private var m_LastTime: UInt

    constructor() {
        m_LastTime = GetTickCount()
    }

    fun Init(): Boolean {
        return true
    }

    fun GetCurTime(): UInt {
        return GetTickCount().also { m_LastTime = it }
    }

    fun GetStep(): UInt {
        val ntime = GetTickCount()
        val sval: UInt
        if (ntime == m_LastTime) {
            return 0u
        } else if (ntime > m_LastTime) {
            sval = ntime - m_LastTime
        } else {
            sval = (0xFFFF_FFFFu - m_LastTime) + ntime
        }
        m_LastTime = ntime
        return sval
    }
}