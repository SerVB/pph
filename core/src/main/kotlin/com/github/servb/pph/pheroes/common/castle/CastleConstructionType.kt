package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

// Types of castle constructions
enum class CastleConstructionType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    GENERIC(0),
    DWELLING(1),  // fparam = Creature type
    MAGEGUILD(2),  // fparam = guild level
    TAVERN(3),
    MAGICNODE(4),
    MINE(5),  // fparam = mineral type, sparam = quantity
    PERM_FSK_MOD(6),  // fparam = skill, sparam = modifier
    DWELL_ENC(7),  // fparam = dwelling id, sparam = modifier
    OBSERVPOST(8),
    LIBRARY(9),
    MANAVORTEX(10),
    TREASURY(11),
    MYSTICPOUND(12),
    NECRAMPLIFIER(13),
    COVEROFDARKNESS(14),
    COUNT(15);
}
