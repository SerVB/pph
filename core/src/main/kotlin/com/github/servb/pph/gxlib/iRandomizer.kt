package com.github.servb.pph.gxlib

class iRandomizer(seed: Int = 1) {
    private val MaxVal = 0x7FFF
    private var m_holdrand = seed
    val seed get() = m_holdrand

    fun SetNewSeed(seed: Int): Int {
        m_holdrand = seed
        println("---- RND initialization with $m_holdrand")
        return m_holdrand
    }

    fun Rand(maxVal: Int = MaxVal): Int {
        check(maxVal > 0)

        m_holdrand = m_holdrand * 214013 + 2531011
        var res = (m_holdrand ushr 16) and 0x7fff
        println("- RND value $res")
        if (maxVal != MaxVal) {
            res %= maxVal
        }
        println("---- RND returns $res")
        return res
    }

    fun Rand(minVal: Int, maxVal: Int): Int {
        check(maxVal > minVal)
        return minVal + Rand(maxVal - minVal)
    }
}
