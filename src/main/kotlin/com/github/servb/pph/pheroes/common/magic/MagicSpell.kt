package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** Spell list. */
enum class MagicSpell(override val v: Int) : UniqueValueEnum, UndefinedCountValueEnum {
    INVALID(-1),
    // Air magic 15-3 = 12
    MAGICARROW(0),
    PROTEARTH(1),
    HASTE(2),
    SHIELD(3),
    DISRAY(4),
    LIGHTNINGBOLT(5),
    PRECISION(6),
    AIRSHIELD(7),
    HOLYWORD(8),
    COUNTERSTRIKE(9),
    RESURRECT(10),
    AIRELEMENTAL(11),

    // Earth magic 13-3 = 10
    PROTAIR(12),
    SLOW(13),
    STONESKIN(14),
    VISIONS(15),
    EARTHQUAKE(16),
    SORROW(17),
    METEORSHOWER(18),
    TOWNPORTAL(19),
    IMPLOSION(20),
    EARTHELEMENTAL(21),

    // Fire magic 15-3 = 12
    BLOODLUST(22),
    PROTWATER(23),
    CURSE(24),
    BLIND(25),
    WEAKNESS(26),
    DEATHRIPPLE(27),
    FIREBALL(28),
    MISFORTUNE(29),
    ANIMATEDEAD(30),
    FIREBLAST(31),
    ARMAGEDDON(32),
    FIREELEMENTAL(33),

    // Water magic 15-4 = 11
    BLESS(34),
    PROTFIRE(35),
    DISPEL(36),
    CURE(37),
    COLDRAY(38),
    FORTUNE(39),
    MIRTH(40),
    COLDRING(41),
    ANTIMAGIC(42),
    PRAYER(43),
    WATERELEMENTAL(44),

    // New spells
    SUMMONSPRITES(45),

    COUNT(46),
}
