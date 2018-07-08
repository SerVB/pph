package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** TODO: Provide documentation, provide tests. */
enum class ART_TYPE(override val v: Int) : UniqueValueEnum, CountValueEnum {
    ARTT_BASIC(0),
    ARTT_FURTSKILL(1),
    ARTT_NEGSPHERE(2),
    ARTT_SHOFWAR(3),
    ARTT_CURSWORD(4),
    ARTT_COUNT(5),
    ARTT_ULTIMATE(0x0F00);
}
