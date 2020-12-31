package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class SpecialHeroFlag(
        override val v: Int,
        private val description: String? = null
) : UniqueValueEnum {
    INVALID(-1),
    NO_RANGE_PENALTY(0, "No range attack penalty"),
    SUMMON_RESURRECTION_BOUNS(1, "50% bonus for resurrection and summon spells"),
    MANA_RESTORE(2, "Mana restores each day"),
    DAMAGE_SPELL_BONUS(3, "Effect from all damage spells increased by 50%"),
    NECROMANCY_BONUS(4, "Necromancy skill restores mummies instead of skeletons");
}
