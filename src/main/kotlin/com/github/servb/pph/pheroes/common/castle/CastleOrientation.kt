package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class CastleOrientation(
        override val v: Int,
        val heroOrientation: Int? = null
) : UniqueValueEnum, CountValueEnum {
    LEFT(0, 1),
    RIGHT(1, 7),
    COUNT(2);
}
