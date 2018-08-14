package com.github.servb.pph.pheroes.common.common.artifact

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class ArtifactLevel(override val v: Int) : UniqueValueEnum, CountValueEnum {
    NONE(0),
    TREASURE(1),
    MINOR(2),
    MAJOR(3),
    RELICT(4),
    ULTIMATE(5),
    COUNT(6);
}
