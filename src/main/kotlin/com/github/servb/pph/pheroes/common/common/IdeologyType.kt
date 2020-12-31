package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Nations and heroes.  */
enum class IdeologyType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    NEUTRAL(0),
    GOOD(1),
    EVIL(2),
    COUNT(3);
}
