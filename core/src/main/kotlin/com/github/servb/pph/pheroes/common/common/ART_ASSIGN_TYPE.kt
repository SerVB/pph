package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** TODO: Provide documentation, provide tests. */
enum class ART_ASSIGN_TYPE(override val v: Int) : UniqueValueEnum, CountValueEnum {
    ART_ASSIGN_HEAD(0),
    ART_ASSIGN_NECK(1),
    ART_ASSIGN_TORSO(2),
    ART_ASSIGN_SHOULDERS(3),
    ART_ASSIGN_HANDS(4),
    ART_ASSIGN_FINGERS(5),
    ART_ASSIGN_LEGS(6),
    ART_ASSIGN_FEET(7),
    ART_ASSIGN_MISC(8),
    ART_ASSIGN_COUNT(9);
}
