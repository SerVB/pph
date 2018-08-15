package com.github.servb.pph.pheroes.common.creature

import com.github.servb.pph.util.helpertype.UniqueValueEnum

const val CREATURE_PERK_COUNT = 21

enum class Perk(override val v: Int, private val description: String? = null) : UniqueValueEnum {
    NONE(0),
    DOUBLESHOT(0x00000001, "Shots twice (rangers, grand elves, etc)"),
    DOUBLEATTACK(0x00000002, "Attack the target twice (paladins, wolves, etc)"),
    NOMELEEPENALTY(0x00000004, "Scores full damage even at melee"),
    NONRETALATTACK(0x00000008, "Non retaliated attack (vampires, rogues, sprites, hydras, etc)"),
    RETALTOALL(0x00000010, "Retaliates against every attack (griffins)"),
    TWOHEXATTACK(0x0000002, "Two-hex attack (dragons)"),
    ADJACENTATTACK(0x0000004, "Attacks all adjacent enemies (hydras)"),
    LICHSHOOT(0x0000008, "Range attack affects adjacent hexes except undeads (Liches)"),
    UNDEAD(0x0000010, "All necropolis creatures + ghosts"),
    LIFELESS(0x0000020, "Golems, gargoyles, elementals"),
    REGENERATES(0x0000040, "Trolls"),
    JOUSTING(0x0000080, "Cavalry"),
    AIRMAGICIMM(0x0000100, "Air elementals"),
    EARTHMAGICIMM(0x0000200, "Earth elementals"),
    FIREMAGICIMM(0x0000400, "Fire elementals, Phoenix"),
    WATERMAGICIMM(0x0000800, "Water elementals"),
    PROCRESIST40(0x0001000, "40% magic resistance (dwarves)"),
    QUARTERDMG(0x0002000, "Receives only quarter damage from damage spells (golems)"),
    GHOST(0x0004000, "Ghost perk"),
    DOHALF(0x0008000, "Genie perk"),
    DRAINLIFES(0x0010000, "Drains life (vampires)"),
    ALLMAGICIMM(AIRMAGICIMM.v or EARTHMAGICIMM.v or FIREMAGICIMM.v or WATERMAGICIMM.v, "Black draggons");
}
