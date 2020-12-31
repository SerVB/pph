package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class PathElementType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    WALL(0),
    ROAD(1),
    RIVER(2),
    COUNT(3);
}
