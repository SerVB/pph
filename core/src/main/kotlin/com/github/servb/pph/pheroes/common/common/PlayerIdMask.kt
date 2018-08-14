package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class PlayerIdMask(override val v: Int) : UniqueValueEnum {
    NONE(0x00),
    RED(0x01),
    GREEN(0x02),
    BLUE(0x04),
    CYAN(0x08),
    MAGENTA(0x10),
    YELLOW(0x20),
    ALL(
            RED.v or GREEN.v or BLUE.v or CYAN.v or MAGENTA.v or YELLOW.v
    )
}
