package com.github.servb.pph.pheroes.common.common.artifact

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class ArtifactAssign(override val v: Int) : UniqueValueEnum, CountValueEnum {
    HEAD(0),
    NECK(1),
    TORSO(2),
    SHOULDERS(3),
    HANDS(4),
    FINGERS(5),
    LEGS(6),
    FEET(7),
    MISC(8),
    COUNT(9);
}
