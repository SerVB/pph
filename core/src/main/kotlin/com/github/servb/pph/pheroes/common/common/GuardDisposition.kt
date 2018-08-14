package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Guards. TODO: Provide documentation. */
enum class GuardDisposition(override val v: Int) : UniqueValueEnum, CountValueEnum {
    COMPLIANT(0),
    AGGRESSIVE(1),
    SAVAGE(2),
    COUNT(3);
}
