package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class MapSize(override val v: Int, val size: Int = -42) : UniqueValueEnum, CountValueEnum {
    SMALL(0, 32),
    MEDIUM(1, 64),
    LARGE(2, 128),
    EXTRA_LARGE(3, 256),
    COUNT(4);
}
