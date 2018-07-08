package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/**
 * Further skills (don't forget to edit defines.cpp at map editor). TODO: Provide documentation, provide tests.
 */
enum class FURTHER_SKILLS(override val v: Int) : UniqueValueEnum, CountValueEnum {
    FSK_INVALID(-1),

    /** [All] Increases Attack skill by 'n'.  */
    FSK_ATTACK(0),

    /** [All] Increases Defence skill by 'n'.  */
    FSK_DEFENCE(1),

    /** [All] Increases Spell power skill by 'n'.  */
    FSK_POWER(2),

    /** [All] Increases Knowledge skill by 'n'.  */
    FSK_KNOWLEDGE(3),

    /** [Overland] Hero’s land movement range is increased on 'n' pts.  */
    FSK_ACTPTS(4),

    /** [Overland] Hero’s land movement range is increased by 'n'%.  */
    FSK_LOGISTICS(5),

    /** [Overland] Reduce the effects of difficult terrain by 'n'%.  */
    FSK_PATHFINDING(6),

    /** [Overland] Increases Scouting level by 'n' cells.  */
    FSK_SCOUTING(7),

    /** [Overland] Increases Vision level by 'n'.  */
    FSK_VISION(8),

    /** [Combat] ???.  */
    FSK_BALLISTICS(9),

    /** [Overland] Earned experience is increased by 'n'%.  */
    FSK_LEARNING(10),

    /**
     * [All] 'n'% of creatures normally fleeing from your army offer to join.
     * Cost of surrendering is reduced by 'n/2'%.
     */
    FSK_DIPLOMACY(11),

    /** [Combat] 'n'% of enemy creatures killed are resurrected as skeletons and added to the hero’s army.  */
    FSK_NECROMANCY(12),

    /** [Combat] Increases Magic spells power by 'n'%.  */
    FSK_SORCERY(13),

    /** [Overland] Restores 'n' mana points each day.  */
    FSK_MANAPTS(14),

    /** [Overland] Maximum spell points is increased by 'n'%.  */
    FSK_INTELLIGENCE(15),

    /** [Overland] Allows the hero to learn 'n'th level spells and below.  */
    FSK_WISDOM(16),

    /** [All] Allows the hero to cast 'n'th level spells of Air magic and below.  */
    FSK_MAG_AIR(17),

    /** [All] Allows the hero to cast 'n'th level spells of Earth magic and below.  */
    FSK_MAG_EARTH(18),

    /** [All] Allows the hero to cast 'n'th level spells of Fire magic and below.  */
    FSK_MAG_FIRE(19),

    /** [All] Allows the hero to cast 'n'th level spells of Water magic and below.  */
    FSK_MAG_WATER(20),

    /** [Combat] Damage from Air magic spells is reduced by 'n'% for target.  */
    FSK_RES_AIR(21),

    /** [Combat] Damage from Earth magic spells is reduced by 'n'% for target.  */
    FSK_RES_EARTH(22),

    /** [Combat] Damage from Fire magic spells is reduced by 'n'% for target.  */
    FSK_RES_FIRE(23),

    /** [Combat] Damage from Water magic spells is reduced by 'n'% for target.  */
    FSK_RES_WATER(24),

    /** [Combat] Increases Archery skill by 'n'% (increase range attack damage).  */
    FSK_ARCHERY(25),

    /** [Combat] Increases Offence skill by 'n'% (increase melee damage).  */
    FSK_OFFENCE(26),

    /** [Combat] Decrease range attack damage by 'n'%.  */
    FSK_AIRSHIELD(27),

    /** [Combat] Decrease melee damage by 'n'%.  */
    FSK_SHIELD(28),

    /** [Combat] Increases Armorer skill by 'n'% (decrease damage).  */
    FSK_ARMORER(29),

    /** [Combat]        Increase attack skill for range attack by 'n'.  */
    FSK_RANGEATTACK(30),

    /** [Combat] Increase attack skill for melee attack by 'n'.  */
    FSK_MELEEATTACK(31),

    /** [Combat] Increases Magic resistance skill by 'n'%.  */
    FSK_RESIST(32),

    /** [Combat] Creatures hit-points is increased by 'n'.  */
    FSK_HITS(33),

    /** [Combat] Creatures speed is increased by 'n'.  */
    FSK_SPEED(34),

    /** [Combat] Morale is increased by 'n'.  */
    FSK_MORALE(35),

    /** [Combat] Luck is increased by 'n'.  */
    FSK_LUCK(36),

    /** [Combat] Counter strike.  */
    FSK_COUNTERSTRIKE(37),

    /** [Overland] Adds 'n' units of gold each day.  */
    FSK_MIN_GOLD(38),

    /** [Overland] Adds 'n' units of ore each day.  */
    FSK_MIN_ORE(39),

    /** [Overland] Adds 'n' units of wood each day.  */
    FSK_MIN_WOOD(40),

    /** [Overland] Adds 'n' units of mercury each day.  */
    FSK_MIN_MERCURY(41),

    /** [Overland] Adds 'n' units of gems each day.  */
    FSK_MIN_GEMS(42),

    /** [Overland] Adds 'n' units of crystal each day.  */
    FSK_MIN_CRYSTAL(43),

    /** [Overland] Adds 'n' units of sulfur each day.  */
    FSK_MIN_SULFUR(44),

    FSK_COUNT(45);
}
