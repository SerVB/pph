package com.github.servb.pph.pheroes.common.common.artifact

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class ArtifactType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    BASIC(0),
    FURTSKILL(1),
    NEGSPHERE(2),
    SHOFWAR(3),
    CURSWORD(4),
    COUNT(5),
    ULTIMATE(0x0F00);
}
