package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class MagicSchoolLevel(override val v: Int) : UniqueValueEnum, CountValueEnum {
    NONE(0),
    BASIC(1),
    ADVANCED(2),
    EXPERT(3),
    COUNT(4),
}
