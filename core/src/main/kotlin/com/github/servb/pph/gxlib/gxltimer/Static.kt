package com.github.servb.pph.gxlib.gxltimer

fun GetTickCount() = System.currentTimeMillis().toInt()  // TODO: Check the code

class iTimer {
    private var m_LastTime = GetTickCount()

    fun Init(): Boolean {
        return true
    }

    fun GetCurTime(): Int {
        m_LastTime = GetTickCount()
        return m_LastTime
    }

    fun GetStep(): Int {
        val ntime = GetTickCount()
        val sval = when {
            ntime == m_LastTime -> return 0
            ntime > m_LastTime -> ntime - m_LastTime
            else -> (0xFFFFFFFF - m_LastTime + ntime).toInt()
        }
        m_LastTime = ntime
        return sval
    }
}
