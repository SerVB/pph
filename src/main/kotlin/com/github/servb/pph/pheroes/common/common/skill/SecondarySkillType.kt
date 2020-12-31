package com.github.servb.pph.pheroes.common.common.skill

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

// TODO: Are private comments actual?
enum class SecondarySkillType(
        override val v: Int,
        val furtherSkill: FurtherSkill? = null,
        val basic: Int? = null,
        val advanced: Int? = null,
        val expert: Int? = null,
        private val valueType: String? = null,
        private val type: Type? = null
) : UniqueValueEnum, UndefinedCountValueEnum {
    NONE(-1),
    ESTATES(0, FurtherSkill.MIN_GOLD, 250, 500, 1000, "gold points", Type.DAY_START),
    LEADERSHIP(1, FurtherSkill.MORALE, 1, 2, 3, "morale", Type.ONLY_BATTLE),
    LUCK(2, FurtherSkill.LUCK, 1, 2, 3, "luck", Type.ONLY_BATTLE),
    DIPLOMACY(3, FurtherSkill.DIPLOMACY, 20, 40, 60, "%", Type.ON_DEMAND),
    AIRMAGIC(4, FurtherSkill.MAG_AIR, 1, 2, 3, type = Type.ON_DEMAND),
    EARTHMAGIC(5, FurtherSkill.MAG_EARTH, 1, 2, 3, type = Type.ON_DEMAND),
    FIREMAGIC(6, FurtherSkill.MAG_FIRE, 1, 2, 3, type = Type.ON_DEMAND),
    WATERMAGIC(7, FurtherSkill.MAG_WATER, 1, 2, 3, type = Type.ON_DEMAND),
    WISDOM(8, FurtherSkill.WISDOM, 1, 2, 3, type = Type.ON_DEMAND),
    NECROMANCY(9, FurtherSkill.NECROMANCY, 10, 20, 30, "%", Type.ON_DEMAND),
    MYSTICISM(10, FurtherSkill.MANAPTS, 2, 4, 6, "mana points", Type.DAY_START),
    INTELLIGENCE(11, FurtherSkill.INTELLIGENCE, 25, 50, 100),
    RESISTANCE(12, FurtherSkill.RESIST, 5, 10, 20),
    SORCERY(13, FurtherSkill.SORCERY, 5, 10, 15, "%", Type.ON_DEMAND),
    LEARNING(14, FurtherSkill.LEARNING, 5, 10, 15, "%", Type.ON_DEMAND),
    SCOUTING(15, FurtherSkill.SCOUTING, 2, 4, 6, "cells", Type.ON_DEMAND),
    LOGISTICS(16, FurtherSkill.LOGISTICS, 10, 20, 30, "action points", Type.DAY_START),
    PATHFINDING(17, FurtherSkill.PATHFINDING, 25, 50, 75, type = Type.ON_DEMAND),
    ARCHERY(18, FurtherSkill.ARCHERY, 10, 25, 50, type = Type.ONLY_BATTLE),
    BALLISTICS(19, FurtherSkill.BALLISTICS, 1, 2, 3, type = Type.ONLY_BATTLE),
    OFFENCE(20, FurtherSkill.OFFENCE, 10, 20, 30, type = Type.ONLY_BATTLE),
    ARMORER(21, FurtherSkill.ARMORER, 5, 10, 15, type = Type.ONLY_BATTLE),
    COUNT(22);

    private enum class Type {
        DAY_START, ON_DEMAND, ONLY_BATTLE;
    }
}
