package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.or

enum class SpellType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    OVERLAND(0),
    COMBAT(1),
    COUNT(2);
}

enum class SpellTypeMask(override val v: Int) : UniqueValueEnum {
    OVERLAND(0b01),
    COMBAT(0b10),
    ALL(OVERLAND or COMBAT),
}
