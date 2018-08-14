package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class PlayerTypeMask(override val v: Int) : UniqueValueEnum, CountValueEnum {
    HUMAN_ONLY(0),
    COMPUTER_ONLY(1),
    HUMAN_OR_COMPUTER(2),
    COUNT(3);
}
