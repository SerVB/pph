package com.github.servb.pph.pheroes.common.event

import com.github.servb.pph.pheroes.common.common.MineralSet
import com.github.servb.pph.pheroes.common.common.MineralSetC
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.common.PlayerIdMask
import com.github.servb.pph.util.helpertype.UniqueValueEnum

data class iTimeEvent(
        val name: String,
        val text: String,
        val playerMask: PlayerIdMask,
        val time: Int,
        val repTime: EventFreq,
        val minerals: MineralSetC
) {
    enum class EventFreq(override val v: Int, val days: Int) : UniqueValueEnum {
        NEVER(0, 0),
        DAY1(1, 1), DAYS2(2, 2), DAYS3(3, 3), DAYS4(4, 4), DAYS5(5, 5), DAYS6(6, 6),
        WEEK1(7, 7), WEEKS2(8, 14), WEEKS3(9, 21), MONTH1(10, 28),
    }

    constructor() : this("", "", PlayerIdMask.ALL, 1, EventFreq.NEVER, MineralSet())

    fun IsConform(pid: PlayerId, curDay: Int) = when {
        playerMask.v and (1 shl pid.v) == 0 -> false
        curDay < time -> false
        curDay == time -> true
        repTime != EventFreq.NEVER -> 0 == (curDay - time) % repTime.days
        else -> false
    }
}
