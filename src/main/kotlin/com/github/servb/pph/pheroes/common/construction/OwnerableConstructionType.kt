package com.github.servb.pph.pheroes.common.construction

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class OwnerableConstructionType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    BASIC(0),    // Generic Ownerable construction
    FURTSKILL(1),    // Modifies one or several owner's further skills
    COUNT(2),
}
