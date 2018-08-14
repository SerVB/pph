package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class MapItemType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    MINERAL(0),
    MANACRYSTAL(1),
    CAMPFIRE(2),
    CHEST(3),
    ARTIFACT(4),
    SPELLSCROLL(5),
    LAMP(6),
    KEYGUARD(7),
    COUNT(8);
}
