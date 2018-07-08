package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** TODO: Provide documentation, provide tests. */
enum class ART_LEVEL_TYPE(override val v: Int) : UniqueValueEnum, CountValueEnum {
    ART_LEVEL_NONE(0),
    ART_LEVEL_TREASURE(1),
    ART_LEVEL_MINOR(2),
    ART_LEVEL_MAJOR(3),
    ART_LEVEL_RELICT(4),
    ART_LEVEL_ULTIMATE(5),
    ART_LEVEL_COUNT(6);
}
