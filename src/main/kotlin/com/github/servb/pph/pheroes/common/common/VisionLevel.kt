package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Vision level (information level).  */
enum class VisionLevel(override val v: Int) : UniqueValueEnum {
    NONE(0),
    BASIC(1),
    EXPERT(2);
}
