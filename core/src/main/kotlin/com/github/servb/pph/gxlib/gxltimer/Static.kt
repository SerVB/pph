package com.github.servb.pph.gxlib.gxltimer

import ui
import unsigned.Uint
import unsigned.ui

fun GetTickCount() = Uint(System.currentTimeMillis().toInt())  // TODO: Check the code

class iTimer {

    fun Init(): Boolean {
        return true;
    }

    fun GetCurTime(): Uint {
        m_LastTime = GetTickCount()
        return m_LastTime
    }

    fun GetStep(): Uint {
        val ntime = GetTickCount()
        val sval: Uint
        sval = when {
            ntime == m_LastTime -> return 0.ui
            ntime > m_LastTime -> ntime - m_LastTime
            else -> (0xFFFFFFFF.ui - m_LastTime) + ntime
        }
        m_LastTime = ntime
        return sval
    }

    private var m_LastTime: Uint

    init {
        m_LastTime = GetTickCount()
    }
}
