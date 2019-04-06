package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class SpellDisposition(override val v: Int) : UniqueValueEnum, UndefinedCountValueEnum {
    NONE(-1),
    NEUTRAL(0),
    POSITIVE(1),
    NEGATIVE(2),
    COUNT(3),
}
