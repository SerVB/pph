package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Target mode (used for combat spells). */
enum class SpellTargetMode(override val v: Int) : UniqueValueEnum {
    NONE(0),
    SUMMON(1),
    EARTHQUAKE(2),
    CREAT_GROUP(3),
    BALL_SET(4),
    BLAST_SET(5),
    RING_SET(6),
    ALL(7),
}
