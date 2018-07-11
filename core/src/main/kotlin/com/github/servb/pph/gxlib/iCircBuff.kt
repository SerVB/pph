package com.github.servb.pph.gxlib

class iCircBuff<T>(private val bufferSize: Int = 64) {
    private	var m_rCur = 0
    private var m_wCur = 0
    private var m_aT = MutableList<T?>(bufferSize) { null }

    fun Count(): Int {
        return if (m_wCur >= m_rCur) {
            m_wCur - m_rCur
        } else {
            bufferSize - m_rCur + m_wCur
        }
    }

    fun Put(t: T) {
        if (m_wCur == m_rCur) {
            check(false) { "Buffer is full" }
        }
        m_aT[m_wCur] = t
        ++m_wCur
        if (m_wCur == bufferSize) {
            m_wCur = 0
        }
    }

    fun Get(): T {
        check(m_rCur != m_wCur) { "Buffer is empty" }

        val oPos = m_rCur
        ++m_rCur
        if (m_rCur == bufferSize) {
            m_rCur = 0
        }
        return m_aT[oPos]!!
    }

    fun Reset() {
        m_rCur = 0
        m_wCur = 0
    }
}
