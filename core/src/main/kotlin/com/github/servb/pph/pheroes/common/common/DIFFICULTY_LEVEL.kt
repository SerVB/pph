package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** TODO: Provide documentation, provide tests.  */
enum class DIFFICULTY_LEVEL(override val v: Int) : UniqueValueEnum, CountValueEnum {
    DFC_UNDEFINED(-1),
    DFC_EASY(0),
    DFC_NORMAL(1),
    DFC_HARD(2),
    DFC_EXPERT(3),
    DFC_IMPOSSIBLE(4),
    DFC_COUNT(5);
}
