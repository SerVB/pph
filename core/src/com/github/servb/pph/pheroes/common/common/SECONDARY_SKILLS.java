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
 * SECONDARY_SKILLS. TODO: Provide documentation.
 *
 * @author SerVB
 */
public enum SECONDARY_SKILLS implements EnumC {

    SECSK_NONE(-1),     //    Basic        Advanced    Expert        Master        DayStart/OnDemand/OnlyBattle
    SECSK_ESTATES(0),   //    +100gp       +250gp      +500gp        +1000gp       X
    SECSK_LEADERSHIP,   //    +1 morale    +2 morale   +3 morale     +4 morale                        X
    SECSK_LUCK,         //    +1 luck      +2 luck     +3 luck       +4 luck                          X
    SECSK_DIPLOMACY,    //    20%          35%         50%           65%                    X
    SECSK_AIRMAGIC,     //                                                                  X
    SECSK_EARTHMAGIC,   //                                                                  X
    SECSK_FIREMAGIC,    //                                                                  X
    SECSK_WATERMAGIC,   //                                                                  X
    SECSK_WISDOM,       //    2nd          3rd         4th           5th                    X
    SECSK_NECROMANCY,   //    10%          20%         30%           40%                    X
    SECSK_MYSTICISM,    //    +2mp         +4mp        +6mp          +8mp          X
    SECSK_INTELLIGENCE, //
    SECSK_RESISTANCE,   //
    SECSK_SORCERY,      //    +5%          +10%        +15%          +20%                   X
    SECSK_LEARNING,     //    +5%exp       +10%exp     +15%exp       +20%exp                X
    SECSK_SCOUTING,     //    +1cell       +2cell      +3cell        +4cell                 X
    SECSK_LOGISTICS,    //    +15ap        +30ap       +45ap         +60ap         X
    SECSK_PATHFINDING,  //                                                                  X
    SECSK_ARCHERY,      //                                                                            X
    SESCK_BALLISTICS,   //                                                                            X
    SECSK_OFFENCE,      //                                                                            X
    SECSK_ARMORER,      //                                                                            X
    SECSK_COUNT;

    //<editor-fold defaultstate="collapsed" desc="C-like enum (for indexing and count)">
    /** The value of the element. */
    private int value;

    /** Constructs a new element with the next value. */
    private SECONDARY_SKILLS() {
        this(NextValueHolder.nextValue); // Call the constructor with the next value
    }

    /**
     * Constructs a new element with the specified value.
     *
     * @param value The specified value.
     */
    private SECONDARY_SKILLS(final int value) {
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
