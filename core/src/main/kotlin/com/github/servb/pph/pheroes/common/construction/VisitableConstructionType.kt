package com.github.servb.pph.pheroes.common.construction

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class VisitableConstructionType(override val v: Int) : UniqueValueEnum, CountValueEnum {
    BASIC(0),    // Generic Visitable construction
    STABLE(1),        // Adds 'n' action points (end of week to each visitor)
    GAZEBO(2),        // Adds 'n' exp points (once to each visitor)
    MANASOURCE(3),    // Adds 'n' mana points (end of week to each visitor)
    MLMODIFIER(4),    // Modifies morale and luck
    PSMODIFIER(5),    // Modifies set of primary skills by mask
    OBELISK(6),        // Opens one element of puzzle map
    SIGN(7),            // Shows message
    DWELLING(8),        // Dwelling
    WITCHHUT(9),        // Witch Hut (learn random or specified secondary skill)
    SHRINE(10),        // Magic shrine (learn random spell of specified (1-3) level)
    TREASURY(11),        // Resource treasury
    TELEPORT(12),        // Teleports hero
    KEYMASTER(13),    // Gives specified key
    KNWLTREE(14),        // Tree of Knowledge
    WINDMILL(15),        // Windmill (gives random ammount of random mineral one time per week)
    WEEKLYMIN(16),    // Gives fixed ammount of specified mineral(s) one time per week
    COUNT(17),
    // Windmill (2-5 of random resource (except gold) one time per week)
    // Water wheel (+1000gp one time per week)
    // University (lern one or more from 4 secondray skills for 2.000)
    // Dragon utopia
    // Altar of sacrifice (creatures and artifacts to experience)
    // Market
    // Black market
    // Treasures ('n' units of 'x' creature type guards 'm' mineral set)
    // Den of thieves (shows detailed world information)
    // Shrine of magic (learns random 1-3 level spell)
    // Keymaster's Tent
    // Seer's hut
    // Crypt/Graveyard
    // Corps/Skeleton
}
