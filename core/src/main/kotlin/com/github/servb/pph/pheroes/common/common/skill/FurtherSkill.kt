package com.github.servb.pph.pheroes.common.common.skill

import com.github.servb.pph.pheroes.common.common.skill.FurtherSkill.FurtherSkillType.*
import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/**
 * Further skills (don't forget to edit defines.cpp at map editor). TODO: Provide documentation, provide tests.
 */
enum class FurtherSkill(
        override val v: Int,
        private val type: FurtherSkillType? = null,
        private val description: String? = null
) : UniqueValueEnum, UndefinedCountValueEnum {
    INVALID(-1),

    ATTACK(0, ALL, "Increases Attack skill by 'n'"),
    DEFENCE(1, ALL, "Increases Defence skill by 'n'"),
    POWER(2, ALL, "Increases Spell power skill by 'n'"),
    KNOWLEDGE(3, ALL, "Increases Knowledge skill by 'n'"),

    ACTPTS(4, OVERLAND, "Hero's land movement range is increased by 'n' pts"),
    LOGISTICS(5, OVERLAND, "Hero's land movement range is increased by 'n'%"),
    PATHFINDING(6, OVERLAND, "Reduce the effects of difficult terrain by 'n'%"),
    SCOUTING(7, OVERLAND, "Increases Scouting level by 'n' cells"),
    VISION(8, OVERLAND, "Increases Vision level by 'n'"),
    BALLISTICS(9, COMBAT),
    LEARNING(10, OVERLAND, "Earned experience is increased by 'n'%"),
    DIPLOMACY(
            11,
            ALL,
            """'n'% of creatures normally fleeing from your army offer to join.
              |Cost of surrendering is reduced by 'n/2'%""".trimMargin()
    ),
    NECROMANCY(
            12,
            COMBAT,
            "'n'% of enemy creatures killed are resurrected as skeletons and added to the hero’s army"
    ),
    SORCERY(13, COMBAT, "Increases Magic spells power by 'n'%"),
    MANAPTS(14, OVERLAND, "Restores 'n' mana points each day"),
    INTELLIGENCE(15, OVERLAND, "Maximum spell points is increased by 'n'%"),
    WISDOM(16, OVERLAND, "Allows the hero to learn 'n'th level spells and below"),

    MAG_AIR(17, ALL, "Allows the hero to cast 'n'th level spells of Air magic and below"),
    MAG_EARTH(18, ALL, "Allows the hero to cast 'n'th level spells of Earth magic and below"),
    MAG_FIRE(19, ALL, "Allows the hero to cast 'n'th level spells of Fire magic and below"),
    MAG_WATER(20, ALL, "Allows the hero to cast 'n'th level spells of Water magic and below"),

    RES_AIR(21, COMBAT, "Damage from Air magic spells is reduced by 'n'% for target"),
    RES_EARTH(22, COMBAT, "Damage from Earth magic spells is reduced by 'n'% for target"),
    RES_FIRE(23, COMBAT, "Damage from Fire magic spells is reduced by 'n'% for target"),
    RES_WATER(24, COMBAT, "Damage from Water magic spells is reduced by 'n'% for target"),

    ARCHERY(25, COMBAT, "Increases Archery skill by 'n'% (increase range attack damage)"),
    OFFENCE(26, COMBAT, "Increases Offence skill by 'n'% (increase melee damage)"),

    AIRSHIELD(27, COMBAT, "Decrease range attack damage by 'n'%"),
    SHIELD(28, COMBAT, "Decrease melee damage by 'n'%"),

    ARMORER(29, COMBAT, "Increases Armorer skill by 'n'% (decrease damage)"),

    RANGEATTACK(30, COMBAT, "Increase attack skill for range attack by 'n'"),
    MELEEATTACK(31, COMBAT, "Increase attack skill for melee attack by 'n'"),

    RESIST(32, COMBAT, "Increases Magic resistance skill by 'n'%"),
    HITS(33, COMBAT, "Creatures hit-points is increased by 'n'"),
    SPEED(34, COMBAT, "Creatures speed is increased by 'n'"),
    MORALE(35, COMBAT, "Morale is increased by 'n'"),
    LUCK(36, COMBAT, "Luck is increased by 'n'"),

    COUNTERSTRIKE(37, COMBAT, "Counter strike"),

    MIN_GOLD(38, OVERLAND, "Adds 'n' units of gold each day"),
    MIN_ORE(39, OVERLAND, "Adds 'n' units of ore each day"),
    MIN_WOOD(40, OVERLAND, "Adds 'n' units of wood each day"),
    MIN_MERCURY(41, OVERLAND, "Adds 'n' units of mercury each day"),
    MIN_GEMS(42, OVERLAND, "Adds 'n' units of gems each day"),
    MIN_CRYSTAL(43, OVERLAND, "Adds 'n' units of crystal each day"),
    MIN_SULFUR(44, OVERLAND, "Adds 'n' units of sulfur each day"),

    COUNT(45);

    enum class FurtherSkillType {
        ALL,
        OVERLAND,
        COMBAT;
    }
}
