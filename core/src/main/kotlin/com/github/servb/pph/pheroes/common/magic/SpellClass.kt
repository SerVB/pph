package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class SpellClass(override val v: Int) : UniqueValueEnum, CountValueEnum {
    DAMAGE(0),  // fparam = damage
    FURTSKILL(1),  // fparam = skill, sparam = modifier
    DISRAY(2),  // fparam = defence modifier
    RESURRECT(3),  // fparam = hit points, sparam = true/not
    SUMMON(4),  // fparam = creature type, sparam = quantity
    BLESS(5),  // fparam = modifier (+1, 0, -1), sparam = anti spell
    BLIND(6),  // fparam = retail power (%), sparam = not used
    EARTHQUAKE(7),  // fparam = walls ammount
    DISPEL(8),  // - none -
    CURE(9),  // fparam = hit points per spell power
    TOWNPORTAL(10),  // fparam = (_?_)
    ANTIMAGIC(11),  // fparam = maxLevel
    PRAYER(12),  // fparam = value
    VISION(13),  // fparam = radius
    COUNT(14),
}
