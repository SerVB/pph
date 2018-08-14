package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class NationType(
        override val v: Int,
        val mask: Int = -42,
        val ideology: IdeologyType? = null
) : UniqueValueEnum, CountValueEnum {
    NEUTRAL(0, ideology = IdeologyType.NEUTRAL),
    HIGHMEN(1, 0x1, IdeologyType.GOOD),
    BARBARIANS(2, 0x2, IdeologyType.EVIL),
    WIZARDS(3, 0x4, IdeologyType.GOOD),
    BEASTMEN(4, 0x8, IdeologyType.EVIL),
    ELVES(5, 0x10, IdeologyType.GOOD),
    UNDEADS(6, 0x20, IdeologyType.EVIL),
    COUNT(7);
}
