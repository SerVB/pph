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
    TWOHEXATTACK(0x00000020, "Two-hex attack (dragons)"),
    ADJACENTATTACK(0x00000040, "Attacks all adjacent enemies (hydras)"),
    LICHSHOOT(0x00000080, "Range attack affects adjacent hexes except undeads (Liches)"),
    UNDEAD(0x00000100, "All necropolis creatures + ghosts"),
    LIFELESS(0x00000200, "Golems, gargoyles, elementals"),
    REGENERATES(0x00000400, "Trolls"),
    JOUSTING(0x00000800, "Cavalry"),
    AIRMAGICIMM(0x00001000, "Air elementals"),
    EARTHMAGICIMM(0x00002000, "Earth elementals"),
    FIREMAGICIMM(0x00004000, "Fire elementals, Phoenix"),
    WATERMAGICIMM(0x00008000, "Water elementals"),
    PROCRESIST40(0x00010000, "40% magic resistance (dwarves)"),
    QUARTERDMG(0x00020000, "Receives only quarter damage from damage spells (golems)"),
    GHOST(0x00040000, "Ghost perk"),
    DOHALF(0x00080000, "Genie perk"),
    DRAINLIFES(0x00100000, "Drains life (vampires)"),
    ALLMAGICIMM(AIRMAGICIMM.v or EARTHMAGICIMM.v or FIREMAGICIMM.v or WATERMAGICIMM.v, "Black draggons");
}
