package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class MineralType(override val v: Int) : UniqueValueEnum, UndefinedCountValueEnum {
    UNKNOWN(-1),
    GOLD(0),
    ORE(1),
    WOOD(2),
    MERCURY(3),
    GEMS(4),
    CRYSTAL(5),
    SULFUR(6),
    COUNT(7);
}
