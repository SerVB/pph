package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.pheroes.common.common.HeroType
import com.github.servb.pph.pheroes.common.magic.MagicSchoolMask
import com.github.servb.pph.pheroes.common.magic.SpellFilter
import com.github.servb.pph.pheroes.common.magic.SpellLevelMask
import com.github.servb.pph.pheroes.common.magic.SpellTypeMask
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class CastleType(
    override val v: Int,
    val heroType: HeroType? = null,
    val CTL_MAGE_GUILD_FILTER_0: SpellFilter? = null,
    val CTL_MAGE_GUILD_FILTER_1: SpellFilter? = null,
    val CTL_MAGE_GUILD_FILTER_2: SpellFilter? = null,
    val CTL_MAGE_GUILD_FILTER_3: SpellFilter? = null,
    val CTL_MAGE_GUILD_FILTER_4: SpellFilter? = null,
    val CTL_MAGE_GUILD_SPELLS_0: UByte?,/* = null*/
    val CTL_MAGE_GUILD_SPELLS_1: UByte?,/* = null*/
    val CTL_MAGE_GUILD_SPELLS_2: UByte?,/* = null*/
    val CTL_MAGE_GUILD_SPELLS_3: UByte?,/* = null*/
    val CTL_MAGE_GUILD_SPELLS_4: UByte?,/* = null*/
) : UniqueValueEnum, CountValueEnum {
    CITADEL(
            0,
            HeroType.KNIGHT,
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIRST, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.SECOND, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.THIRD, MagicSchoolMask.GOOD),
            null,
            null,
            5u,
            4u,
            3u,
            null,
            null
    ),
    STRONGHOLD(
            1,
            HeroType.BARBARIAN,
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIRST, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.SECOND, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.THIRD, MagicSchoolMask.EVIL),
            null,
            null,
            5u,
            4u,
            3u,
            null,
            null
    ),
    TOWER(
            2,
            HeroType.WIZARD,
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIRST, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.SECOND, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.THIRD, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FOURTH, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIFTH, MagicSchoolMask.GOOD),
            5u,
            4u,
            3u,
            2u,
            1u
    ),
    DUNGEON(
            3,
            HeroType.WARLOCK,
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIRST, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.SECOND, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.THIRD, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FOURTH, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIFTH, MagicSchoolMask.EVIL),
            5u,
            4u,
            3u,
            2u,
            1u
    ),
    FORTRESS(
            4,
            HeroType.SORCERESS,
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIRST, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.SECOND, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.THIRD, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FOURTH, MagicSchoolMask.GOOD),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIFTH, MagicSchoolMask.GOOD),
            5u,
            4u,
            3u,
            2u,
            1u
    ),
    NECROPOLIS(
            5,
            HeroType.NECROMANCER,
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIRST, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.SECOND, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.THIRD, MagicSchoolMask.EVIL),
            SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FOURTH, MagicSchoolMask.EVIL),
        SpellFilter(SpellTypeMask.ALL, SpellLevelMask.FIFTH, MagicSchoolMask.EVIL),
        5u,
        4u,
        3u,
        2u,
        1u
    ),
    RANDOM(6),
    COUNT(7),
    INVALID(0xFF);

    constructor(v: Int) : this(
        v,
        CTL_MAGE_GUILD_SPELLS_0 = null,
        CTL_MAGE_GUILD_SPELLS_1 = null,
        CTL_MAGE_GUILD_SPELLS_2 = null,
        CTL_MAGE_GUILD_SPELLS_3 = null,
        CTL_MAGE_GUILD_SPELLS_4 = null
    )  // todo: remove after KT-44180
}
