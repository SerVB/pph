/*
 * Copyright 2018 SerVB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.servb.pph.pheroes.common.common;

import com.github.servb.pph.util.helpertype.EnumC;

/**
 * Further skills (don't forget to edit defines.cpp at map editor). TODO: Provide documentation.
 *
 * @author SerVB
 */
public enum FURTHER_SKILLS implements EnumC {

    FSK_INVALID(-1),

    /** [All] Increases Attack skill by 'n'. */
    FSK_ATTACK,

    /** [All] Increases Defence skill by 'n'. */
    FSK_DEFENCE,

    /** [All] Increases Spell power skill by 'n'. */
    FSK_POWER,

    /** [All] Increases Knowledge skill by 'n'. */
    FSK_KNOWLEDGE,

    /** [Overland] Hero’s land movement range is increased on 'n' pts. */
    FSK_ACTPTS,

    /** [Overland] Hero’s land movement range is increased by 'n'%. */
    FSK_LOGISTICS,

    /** [Overland] Reduce the effects of difficult terrain by 'n'%. */
    FSK_PATHFINDING,

    /** [Overland] Increases Scouting level by 'n' cells. */
    FSK_SCOUTING,

    /** [Overland] Increases Vision level by 'n'. */
    FSK_VISION,

    /** [Combat] ???. */
    FSK_BALLISTICS,

    /** [Overland] Earned experience is increased by 'n'%. */
    FSK_LEARNING,

    /**
     * [All] 'n'% of creatures normally fleeing from your army offer to join.
     * Cost of surrendering is reduced by 'n/2'%.
     */
    FSK_DIPLOMACY,

    /** [Combat] 'n'% of enemy creatures killed are resurrected as skeletons and added to the hero’s army. */
    FSK_NECROMANCY,

    /** [Combat] Increases Magic spells power by 'n'%. */
    FSK_SORCERY,

    /** [Overland] Restores 'n' mana points each day. */
    FSK_MANAPTS,

    /** [Overland] Maximum spell points is increased by 'n'%. */
    FSK_INTELLIGENCE,

    /** [Overland] Allows the hero to learn 'n'th level spells and below. */
    FSK_WISDOM,

    /** [All] Allows the hero to cast 'n'th level spells of Air magic and below. */
    FSK_MAG_AIR,

    /** [All] Allows the hero to cast 'n'th level spells of Earth magic and below. */
    FSK_MAG_EARTH,

    /** [All] Allows the hero to cast 'n'th level spells of Fire magic and below. */
    FSK_MAG_FIRE,

    /** [All] Allows the hero to cast 'n'th level spells of Water magic and below. */
    FSK_MAG_WATER,

    /** [Combat] Damage from Air magic spells is reduced by 'n'% for target. */
    FSK_RES_AIR,

    /** [Combat] Damage from Earth magic spells is reduced by 'n'% for target. */
    FSK_RES_EARTH,

    /** [Combat] Damage from Fire magic spells is reduced by 'n'% for target. */
    FSK_RES_FIRE,

    /** [Combat] Damage from Water magic spells is reduced by 'n'% for target. */
    FSK_RES_WATER,

    /** [Combat] Increases Archery skill by 'n'% (increase range attack damage). */
    FSK_ARCHERY,

    /** [Combat] Increases Offence skill by 'n'% (increase melee damage). */
    FSK_OFFENCE,

    /** [Combat] Decrease range attack damage by 'n'%. */
    FSK_AIRSHIELD,

    /** [Combat] Decrease melee damage by 'n'%. */
    FSK_SHIELD,

    /** [Combat] Increases Armorer skill by 'n'% (decrease damage). */
    FSK_ARMORER,

    /** [Combat]        Increase attack skill for range attack by 'n'. */
    FSK_RANGEATTACK,

    /** [Combat] Increase attack skill for melee attack by 'n'. */
    FSK_MELEEATTACK,

    /** [Combat] Increases Magic resistance skill by 'n'%. */
    FSK_RESIST,

    /** [Combat] Creatures hit-points is increased by 'n'. */
    FSK_HITS,

    /** [Combat] Creatures speed is increased by 'n'. */
    FSK_SPEED,

    /** [Combat] Morale is increased by 'n'. */
    FSK_MORALE,

    /** [Combat] Luck is increased by 'n'. */
    FSK_LUCK,

    /** [Combat] Counter strike. */
    FSK_COUNTERSTRIKE,

    /** [Overland] Adds 'n' units of gold each day. */
    FSK_MIN_GOLD,

    /** [Overland] Adds 'n' units of ore each day. */
    FSK_MIN_ORE,

    /** [Overland] Adds 'n' units of wood each day. */
    FSK_MIN_WOOD,

    /** [Overland] Adds 'n' units of mercury each day. */
    FSK_MIN_MERCURY,

    /** [Overland] Adds 'n' units of gems each day. */
    FSK_MIN_GEMS,

    /** [Overland] Adds 'n' units of crystal each day. */
    FSK_MIN_CRYSTAL,

    /** [Overland] Adds 'n' units of sulfur each day. */
    FSK_MIN_SULFUR,

    FSK_COUNT;

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /** The value of the element. */
    private int value;

    /** Constructs a new element with the next value. */
    private FURTHER_SKILLS() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private FURTHER_SKILLS(final int value) {
        this.value = value;                     // Set the specified value to this value
        NextValueHolder.nextValue = value + 1;  // Increment the next value for a next element
    }

    /**
     * Returns the value of the element.
     *
     * @return The value of the element.
     */
    @Override
    public final int getValue() {
        return value;
    }

    /** Object that holds the next value. */
    private final static class NextValueHolder {

        /** The next value. */
        private static int nextValue = 0;

    }
    //</editor-fold>

}
