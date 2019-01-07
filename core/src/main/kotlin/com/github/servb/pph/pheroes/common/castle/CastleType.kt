package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.pheroes.common.common.HeroType
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

@ExperimentalUnsignedTypes
enum class CastleType(
        override val v: Int,
        val heroType: HeroType? = null,
        val CTL_MAGE_GUILD_FILTER_0: SpellFilterC? = null,
        val CTL_MAGE_GUILD_FILTER_1: SpellFilterC? = null,
        val CTL_MAGE_GUILD_FILTER_2: SpellFilterC? = null,
        val CTL_MAGE_GUILD_FILTER_3: SpellFilterC? = null,
        val CTL_MAGE_GUILD_FILTER_4: SpellFilterC? = null,
        val CTL_MAGE_GUILD_SPELLS_0: UByte? = null,
        val CTL_MAGE_GUILD_SPELLS_1: UByte? = null,
        val CTL_MAGE_GUILD_SPELLS_2: UByte? = null,
        val CTL_MAGE_GUILD_SPELLS_3: UByte? = null,
        val CTL_MAGE_GUILD_SPELLS_4: UByte? = null
) : UniqueValueEnum, CountValueEnum {
    CITADEL(
            0,
            HeroType.KNIGHT,
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIRST, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_SECOND, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_THIRD, MST_MASK_GOOD),
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
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIRST, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_SECOND, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_THIRD, MST_MASK_EVIL),
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
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIRST, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_SECOND, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_THIRD, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FOURTH, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIFTH, MST_MASK_GOOD),
            5u,
            4u,
            3u,
            2u,
            1u
    ),
    DUNGEON(
            3,
            HeroType.WARLOCK,
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIRST, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_SECOND, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_THIRD, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FOURTH, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIFTH, MST_MASK_EVIL),
            5u,
            4u,
            3u,
            2u,
            1u
    ),
    FORTRESS(
            4,
            HeroType.SORCERESS,
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIRST, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_SECOND, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_THIRD, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FOURTH, MST_MASK_GOOD),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIFTH, MST_MASK_GOOD),
            5u,
            4u,
            3u,
            2u,
            1u
    ),
    NECROPOLIS(
            5,
            HeroType.NECROMANCER,
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIRST, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_SECOND, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_THIRD, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FOURTH, MST_MASK_EVIL),
            SpellFilter(SPT_MASK_ALL, SPL_MASK_FIFTH, MST_MASK_EVIL),
            5u,
            4u,
            3u,
            2u,
            1u
    ),
    RANDOM(6),
    COUNT(7),
    INVALID(0xFF);
}
