package com.github.servb.pph.pheroes.common.common;

import com.badlogic.gdx.graphics.Color;
import com.github.servb.pph.gxlib.gxlmetrics.ConstPoint;
import com.github.servb.pph.gxlib.gxlmetrics.IConstPoint;
import com.github.servb.pph.util.helpertype.ConstArray;
import com.github.servb.pph.util.helpertype.SimpleConstArray;

import static com.github.servb.pph.gxlib.gxlcommondef.Static.iRandTable;

/**
 * Common defines and constants.
 *
 * @author SerVB
 */
public final class Static {

    /** Prevents from creating an instance of the class. */
    private Static() {}

    public static final int EMAP_FILE_HDR_KEY = 0x76235278;
    public static final int EMAP_FILE_VERSION = 0x19;

    public static final int GMAP_FILE_HDR_KEY = 'G' | ('M' << 8) | ('A' << 16) | ('P' << 24);
    public static final short GMAP_FILE_VERSION = 0x39;

    public static final int GOBJ_FILE_HDR_KEY = 'G' | ('O' << 8) | ('B' << 16) | ('J' << 24);
    public static final short GOBJ_FILE_VERSION = 0x07;

    public static final int GFNT_FILE_HDR_KEY = 'G' | ('F' << 8) | ('N' << 16) | ('T' << 24);
    public static final short GFNT_FILE_VERSION = 0x01;

    public static final int RANDOM_QUANTITY = 0;
    public static final int RAND_VAL = -1;

    public static final ConstArray<SURF_TYPE, Integer> SURF_TYPE_MASK =
            new ConstArray<SURF_TYPE, Integer>(new Integer[] {
                1 << SURF_TYPE.STYPE_WATER.getValue(),
                1 << SURF_TYPE.STYPE_SAND.getValue(),
                1 << SURF_TYPE.STYPE_DIRT.getValue(),
                1 << SURF_TYPE.STYPE_GRASS.getValue(),
                1 << SURF_TYPE.STYPE_SWAMP.getValue(),
                1 << SURF_TYPE.STYPE_LAVA.getValue(),
                1 << SURF_TYPE.STYPE_WASTELAND.getValue(),
                1 << SURF_TYPE.STYPE_DESERT.getValue(),
                1 << SURF_TYPE.STYPE_SNOW.getValue(),
                1 << SURF_TYPE.STYPE_NDESERT.getValue(),
                1 << SURF_TYPE.STYPE_PAVEMENT.getValue(),
                1 << SURF_TYPE.STYPE_NWASTELAND.getValue(),
            });

    public static final ConstArray<SURF_TYPE, Byte> SURF_MOVE_COST =
            new ConstArray<SURF_TYPE, Byte>(new Byte[] {
                (byte) 255, // Water
                12,         // Sand
                6,          // Dirt
                6,          // Grass
                14,         // Swamp
                10,         // Lava
                8,          // Wasteland
                12,         // Desert
                10,         // Snow
                12,         // New desert
                4,          // Pavement
                9           // New wasteland
            });

    public static final ConstArray<SURF_TYPE, Integer> SURF_COLOR =
            new ConstArray<SURF_TYPE, Integer>(new Integer[] {
                Color.toIntBits(8, 28, 128, 255),    // Water
                Color.toIntBits(214, 182, 148, 255), // Sand
                Color.toIntBits(99, 48, 8, 255),     // Dirt
                Color.toIntBits(24, 97, 16, 255),    // Grass
                Color.toIntBits(0, 44, 0, 255),      // Swamp
                Color.toIntBits(48, 48, 48, 255),    // Lava
                Color.toIntBits(165, 85, 16, 255),   // Wasteland
                Color.toIntBits(181, 138, 24, 255),  // Desert
                Color.toIntBits(220, 220, 220, 255), // Snow
                Color.toIntBits(192, 160, 0, 255),   // New Desert
                Color.toIntBits(160, 160, 160, 255), // Pavement
                Color.toIntBits(192, 192, 160, 255), // New wasteland
            });

    public static final int CalcCellSeqGame(final IConstPoint pnt, final int maxv) {
        int result = pnt.getX();
        result += ~(pnt.getY() << 16);
        result ^= (pnt.getX() >> 5);
        result += (pnt.getY() << 3);
        result ^= (pnt.getX() >> 13);
        result += ~(pnt.getY() << 9);
        result ^= (result >> 17);

        final int idx = (result ^ (result >> 8) ^ (result >> 16)) & 255;
        result = iRandTable.get(idx);

        return result % maxv;
    }

    // TODO: Uncomment when editor will be coded.
    /*public static final int CalcCellSeqEditor(final constPoint pnt, final int maxv) {
        int result = pnt.x;
        result += ~(pnt.y << 16);
        result ^= (pnt.x >> 5);
        result += (pnt.y << 3);
        result ^= (pnt.x >> 13);
        result += ~(pnt.y << 9);
        result ^= (result >> 17);

        final int idx = (result ^ (result >> 8) ^ (result >> 16)) & 255;
        result = iTables.crc32[idx];

        return result % maxv;
    }*/

    public static final ConstArray<MAP_SIZE, Integer> MAP_SIZ_SIZE =
            new ConstArray<MAP_SIZE, Integer>(new Integer[] {
                32,
                64,
                128,
                256
            });

    public static final ConstArray<PLAYER_ID, Short> PLAYER_COLORS =
            new ConstArray<PLAYER_ID, Short>(new Short[] {
                (short) (0x1F << 11), // Red
                (short) (0x30 << 5), // Green
                (short) (0x1F), // Blue
                (short) (0x30 << 5 | 0x1F), // Cyan
                (short) (0x1D << 11 | 0x1F), // Magenta
                (short) (0x1D << 11 | 0x38 << 5) // Yellow
            });

    public static final ConstArray<PLAYER_ID, String> PLAYER_TEXT_COLORS =
            new ConstArray<PLAYER_ID, String>(new String[] {
                "#FF99",
                "#F8E8",
                "#F99F",
                "#F7EE",
                "#FE7E",
                "#FEE7"
            });

    public static final ConstArray<PLAYER_ID, String> PLAYER_WORD_COLORS =
            new ConstArray<PLAYER_ID, String>(new String[] {
                "Red",
                "Green",
                "Blue",
                "Cyan",
                "Magenta",
                "Yellow"
            });

    public static final ConstArray<NATION_TYPE, IDEOLOGY_TYPE> NATION_TYPE_IDEOLOGY =
            new ConstArray<NATION_TYPE, IDEOLOGY_TYPE>(new IDEOLOGY_TYPE[] {
                IDEOLOGY_TYPE.IDEOLOGY_NEUTRAL,
                IDEOLOGY_TYPE.IDEOLOGY_GOOD,
                IDEOLOGY_TYPE.IDEOLOGY_EVIL,
                IDEOLOGY_TYPE.IDEOLOGY_GOOD,
                IDEOLOGY_TYPE.IDEOLOGY_EVIL,
                IDEOLOGY_TYPE.IDEOLOGY_GOOD,
                IDEOLOGY_TYPE.IDEOLOGY_EVIL
            });

    public static final ConstArray<HERO_TYPE, IDEOLOGY_TYPE> HERO_TYPE_IDEOLOGY =
            new ConstArray<HERO_TYPE, IDEOLOGY_TYPE>(new IDEOLOGY_TYPE[] {
                IDEOLOGY_TYPE.IDEOLOGY_GOOD,
                IDEOLOGY_TYPE.IDEOLOGY_EVIL,
                IDEOLOGY_TYPE.IDEOLOGY_GOOD,
                IDEOLOGY_TYPE.IDEOLOGY_EVIL,
                IDEOLOGY_TYPE.IDEOLOGY_GOOD,
                IDEOLOGY_TYPE.IDEOLOGY_EVIL
            });

    public static final int DEF_HERO_SCOUTING = 4;
    public static final int DEF_HERO_MYSTICISM = 1;
    public static final int DEF_HERO_MOVES = 60;

    public static final FractionCoeff MineralExchRate(final MINERAL_TYPE from, final MINERAL_TYPE to, final int mlvl) {
        final int fromIdx = from.getValue();
        final int toIdx = to.getValue();

        final int[] MINERAL_EXCH_RATE = {1, 250, 250, 500, 500, 500, 500};
        return new FractionCoeff(MINERAL_EXCH_RATE[fromIdx] * (mlvl + 1), 10 * 2 * MINERAL_EXCH_RATE[toIdx]);
    }

    public static final ConstArray<MINERAL_TYPE, Integer> MINERALS_DIVIDER =
            new ConstArray<MINERAL_TYPE, Integer>(new Integer[] {1000, 2, 2, 1, 1, 1, 1});

    public static final ConstArray<HERO_TYPE, SimpleConstArray<SimpleConstArray<Integer>>> ULTART_STDMODIF =
            new ConstArray<HERO_TYPE, SimpleConstArray<SimpleConstArray<Integer>>>(new SimpleConstArray[] {
                new SimpleConstArray<SimpleConstArray<Integer>>(new SimpleConstArray[] {
                    // Knight
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 5000}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ATTACK.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DEFENCE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_POWER.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_KNOWLEDGE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LOGISTICS.getValue(), 20}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_SPEED.getValue(), 2})
                }),
                new SimpleConstArray<SimpleConstArray<Integer>>(new SimpleConstArray[] {
                    // Barbarian
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 5000}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ATTACK.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DEFENCE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_POWER.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_KNOWLEDGE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_RESIST.getValue(), 30}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_OFFENCE.getValue(), 0})
                }),
                new SimpleConstArray<SimpleConstArray<Integer>>(new SimpleConstArray[] {
                    // Wizard
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 5000}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ATTACK.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DEFENCE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_POWER.getValue(), 6}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_KNOWLEDGE.getValue(), 6}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_RESIST.getValue(), 0}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_OFFENCE.getValue(), 0})
                }),
                new SimpleConstArray<SimpleConstArray<Integer>>(new SimpleConstArray[] {
                    // Warlock
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 5000}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ATTACK.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DEFENCE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_POWER.getValue(), 6}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_KNOWLEDGE.getValue(), 6}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_RESIST.getValue(), 0}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_OFFENCE.getValue(), 0})
                }),
                new SimpleConstArray<SimpleConstArray<Integer>>(new SimpleConstArray[] {
                    // Sorceress
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 5000}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ATTACK.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DEFENCE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_POWER.getValue(), 6}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_KNOWLEDGE.getValue(), 6}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_RESIST.getValue(), 0}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_OFFENCE.getValue(), 0})
                }),
                new SimpleConstArray<SimpleConstArray<Integer>>(new SimpleConstArray[] {
                    // Necromancer
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 5000}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ATTACK.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DEFENCE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_POWER.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_KNOWLEDGE.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_NECROMANCY.getValue(), 15}),
                    new SimpleConstArray<Integer>(new Integer[] {/**/FURTHER_SKILLS.FSK_OFFENCE.getValue(), 0})
                })
            });

    public static final ConstArray<HERO_TYPE, SPEC_HERO_FLAGS> ULTART_SPECFLAGS =
            new ConstArray<HERO_TYPE, SPEC_HERO_FLAGS>(new SPEC_HERO_FLAGS[] {
                SPEC_HERO_FLAGS.SHF_INVALID, // Knight
                SPEC_HERO_FLAGS.SHF_NORANGEPENALTY, // Barbarian
                SPEC_HERO_FLAGS.SHF_SUMRESBOUNS, // Wizard
                SPEC_HERO_FLAGS.SHF_MANARESTORE, // Warlock
                SPEC_HERO_FLAGS.SHF_DMGSPBONUS, // Sorceress
                SPEC_HERO_FLAGS.SHF_NECRBONUS // Necromancer
            });

    public static final ConstArray<HERO_ART_CELL, ART_ASSIGN_TYPE> HERO_ART_CELL_ASSIGN =
            new ConstArray<HERO_ART_CELL, ART_ASSIGN_TYPE>(new ART_ASSIGN_TYPE[] {
                ART_ASSIGN_TYPE.ART_ASSIGN_HEAD,
                ART_ASSIGN_TYPE.ART_ASSIGN_NECK,
                ART_ASSIGN_TYPE.ART_ASSIGN_TORSO,
                ART_ASSIGN_TYPE.ART_ASSIGN_HANDS,
                ART_ASSIGN_TYPE.ART_ASSIGN_HANDS,
                ART_ASSIGN_TYPE.ART_ASSIGN_FINGERS,
                ART_ASSIGN_TYPE.ART_ASSIGN_FINGERS,
                ART_ASSIGN_TYPE.ART_ASSIGN_SHOULDERS,
                ART_ASSIGN_TYPE.ART_ASSIGN_LEGS,
                ART_ASSIGN_TYPE.ART_ASSIGN_FEET,
                ART_ASSIGN_TYPE.ART_ASSIGN_MISC,
                ART_ASSIGN_TYPE.ART_ASSIGN_MISC,
                ART_ASSIGN_TYPE.ART_ASSIGN_MISC,
                ART_ASSIGN_TYPE.ART_ASSIGN_MISC
            });

    public static final ConstArray<HERO_TYPE, ConstArray<PRSKILL_TYPE, Byte>> HERO_PRIM_SKILL =
            new ConstArray<HERO_TYPE, ConstArray<PRSKILL_TYPE, Byte>>(new ConstArray[] {
                new ConstArray<PRSKILL_TYPE, Byte>(new Byte[] {25, 50, 5, 20}), // Knight
                new ConstArray<PRSKILL_TYPE, Byte>(new Byte[] {60, 25, 5, 10}), // Barbarian
                new ConstArray<PRSKILL_TYPE, Byte>(new Byte[] {10, 10, 40, 40}), // Wizard
                new ConstArray<PRSKILL_TYPE, Byte>(new Byte[] {15, 10, 45, 30}), // Warlock
                new ConstArray<PRSKILL_TYPE, Byte>(new Byte[] {15, 15, 30, 40}), // Sorceress
                new ConstArray<PRSKILL_TYPE, Byte>(new Byte[] {15, 15, 40, 30}) // Necromancer
            });

    public static final ConstArray<HERO_TYPE, ConstArray<SECONDARY_SKILLS, Byte>> HERO_SEC_SKILL =
            new ConstArray<HERO_TYPE, ConstArray<SECONDARY_SKILLS, Byte>>(new ConstArray[] {
                new ConstArray<SECONDARY_SKILLS, Byte>(new Byte[] {
                    // Knight
                    2, 6, 6, 4, // Estates, Leadership, Luck, Diplomacy
                    1, 1, 0, 2, // Air, Earth, Fire, Water
                    1, 0, 0, 1, 2, 0, 4, // Wisdom, Necromancy, Mysticism, Intelligence, Resistance, Sorcery, Learning
                    3, 4, 3, // Scouting, Logistics, Pathfinding,
                    3, 3, 2, 4 // Archery, Ballistics, Offence, Armorer
                }),
                new ConstArray<SECONDARY_SKILLS, Byte>(new Byte[] {
                    // Barbarian
                    2, 4, 4, 3, // Estates, Leadership, Luck, Diplomacy
                    1, 1, 2, 0, // Air, Earth, Fire, Water
                    1, 0, 0, 1, 1, 0, 2, // Wisdom, Necromancy, Mysticism, Intelligence, Resistance, Sorcery, Learning
                    3, 3, 5, // Scouting, Logistics, Pathfinding,
                    4, 3, 5, 3 // Archery, Ballistics, Offence, Armorer
                }),
                new ConstArray<SECONDARY_SKILLS, Byte>(new Byte[] {
                    // Wizard
                    2, 2, 2, 3, // Estates, Leadership, Luck, Diplomacy
                    3, 3, 0, 5, // Air, Earth, Fire, Water
                    6, 0, 4, 2, 2, 3, 5, // Wisdom, Necromancy, Mysticism, Intelligence, Resistance, Sorcery, Learning
                    3, 2, 2, // Scouting, Logistics, Pathfinding,
                    0, 0, 0, 0 // Archery, Ballistics, Offence, Armorer
                }),
                new ConstArray<SECONDARY_SKILLS, Byte>(new Byte[] {
                    // Warlock
                    2, 0, 2, 3, // Estates, Leadership, Luck, Diplomacy
                    3, 3, 5, 0, // Air, Earth, Fire, Water
                    4, 1, 4, 3, 2, 5, 4, // Wisdom, Necromancy, Mysticism, Intelligence, Resistance, Sorcery, Learning
                    4, 2, 3, // Scouting, Logistics, Pathfinding,
                    0, 0, 0, 0 // Archery, Ballistics, Offence, Armorer
                }),
                new ConstArray<SECONDARY_SKILLS, Byte>(new Byte[] {
                    // Sorceress
                    2, 2, 2, 3, // Estates, Leadership, Luck, Diplomacy
                    3, 3, 0, 5, // Air, Earth, Fire, Water
                    6, 0, 4, 2, 2, 3, 5, // Wisdom, Necromancy, Mysticism, Intelligence, Resistance, Sorcery, Learning
                    3, 2, 2, // Scouting, Logistics, Pathfinding,
                    1, 1, 0, 0 // Archery, Ballistics, Offence, Armorer
                }),
                new ConstArray<SECONDARY_SKILLS, Byte>(new Byte[] {
                    // Necromancer
                    2, 0, 2, 3, // Estates, Leadership, Luck, Diplomacy
                    3, 3, 5, 0, // Air, Earth, Fire, Water
                    4, 5, 4, 3, 2, 5, 4, // Wisdom, Necromancy, Mysticism, Intelligence, Resistance, Sorcery, Learning
                    4, 2, 3, // Scouting, Logistics, Pathfinding,
                    1, 1, 0, 0 // Archery, Ballistics, Offence, Armorer
                })
            });

    public static final SimpleConstArray<IConstPoint> HERO_FLAG_ANCHOR =
            new SimpleConstArray<IConstPoint>(new IConstPoint[] {
                new ConstPoint(4, 7), new ConstPoint(4, 5), new ConstPoint(4, 4), new ConstPoint(4, 5),
                new ConstPoint(4, 7),
                new ConstPoint(4, 6), new ConstPoint(4, 4), new ConstPoint(4, 5), new ConstPoint(4, 6),

                new ConstPoint(8, 7), new ConstPoint(9, 7), new ConstPoint(8, 7), new ConstPoint(9, 8),
                new ConstPoint(10, 8),
                new ConstPoint(10, 7), new ConstPoint(9, 7), new ConstPoint(8, 7), new ConstPoint(7, 7),

                new ConstPoint(11, 8), new ConstPoint(12, 8), new ConstPoint(11, 8), new ConstPoint(10, 8),
                new ConstPoint(10, 9),
                new ConstPoint(9, 9), new ConstPoint(10, 8), new ConstPoint(11, 8), new ConstPoint(10, 8),

                new ConstPoint(13, 7), new ConstPoint(14, 7), new ConstPoint(13, 8), new ConstPoint(11, 9),
                new ConstPoint(13, 9),
                new ConstPoint(14, 10), new ConstPoint(14, 9), new ConstPoint(14, 8), new ConstPoint(12, 7),

                new ConstPoint(32, 8), new ConstPoint(32, 9), new ConstPoint(32, 10), new ConstPoint(32, 8),
                new ConstPoint(32, 9),
                new ConstPoint(32, 10), new ConstPoint(32, 11), new ConstPoint(32, 8), new ConstPoint(32, 9),

                new ConstPoint(20, 7), new ConstPoint(19, 7), new ConstPoint(20, 8), new ConstPoint(19, 9),
                new ConstPoint(20, 9),
                new ConstPoint(19, 10), new ConstPoint(19, 9), new ConstPoint(19, 8), new ConstPoint(22, 7),

                new ConstPoint(22, 8), new ConstPoint(21, 8), new ConstPoint(22, 8), new ConstPoint(23, 8),
                new ConstPoint(23, 9),
                new ConstPoint(24, 9), new ConstPoint(23, 8), new ConstPoint(22, 8), new ConstPoint(23, 8),

                new ConstPoint(25, 7), new ConstPoint(24, 7), new ConstPoint(25, 7), new ConstPoint(24, 8),
                new ConstPoint(23, 8),
                new ConstPoint(23, 7), new ConstPoint(24, 7), new ConstPoint(25, 7), new ConstPoint(26, 7)
            });

    /** Human and computer. */
    public static final SimpleConstArray<ConstArray<DIFFICULTY_LEVEL, constMineralSet>> INITIAL_RESOURCES =
            new SimpleConstArray<ConstArray<DIFFICULTY_LEVEL, constMineralSet>>(new ConstArray[] {
                new ConstArray<DIFFICULTY_LEVEL, constMineralSet>(new constMineralSet[] {
                    // For Human
                    // Gold, Ore, Wood, Merc, Gems, Cryst, Sulfur
                    new constMineralSet(new int[] {15000, 30, 30, 15, 15, 15, 15}), // Easy
                    new constMineralSet(new int[] {10000, 20, 20, 10, 10, 10, 10}), // Normal
                    new constMineralSet(new int[] {5000, 10, 10, 5, 5, 5, 5}), // Hard
                    new constMineralSet(new int[] {2500, 5, 5, 2, 2, 2, 2}), // Expert
                    new constMineralSet(new int[] {0, 0, 0, 0, 0, 0, 0}) // Impossible
                }),
                new ConstArray<DIFFICULTY_LEVEL, constMineralSet>(new constMineralSet[] {
                    // For Computer
                    // Gold, Ore, Wood, Merc, Gems, Cryst, Sulfur
                    new constMineralSet(new int[] {2500, 5, 5, 2, 2, 2, 2}), // Easy
                    new constMineralSet(new int[] {5000, 10, 10, 5, 5, 5, 5}), // Normal
                    new constMineralSet(new int[] {15000, 30, 30, 15, 15, 15, 15}), // Hard
                    new constMineralSet(new int[] {25000, 50, 50, 25, 25, 25, 25}), // Expert
                    new constMineralSet(new int[] {50000, 80, 80, 40, 40, 40, 40}) // Impossible
                })
            });

    public static final ConstArray<SECONDARY_SKILLS,
            ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>>> SEKSKILL_DESC =
            new ConstArray<SECONDARY_SKILLS, ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>>>(new ConstArray[] {
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Estates
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 250}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 500}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MIN_GOLD.getValue(), 1000})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Leadership
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MORALE.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MORALE.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MORALE.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Luck
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LUCK.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LUCK.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LUCK.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Diplomacy
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DIPLOMACY.getValue(), 20}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DIPLOMACY.getValue(), 40}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_DIPLOMACY.getValue(), 60})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Air
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_AIR.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_AIR.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_AIR.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Earth
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_EARTH.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_EARTH.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_EARTH.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Fire
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_FIRE.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_FIRE.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_FIRE.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Water
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_WATER.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_WATER.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MAG_WATER.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Wisdom
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_WISDOM.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_WISDOM.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_WISDOM.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Necromancy
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_NECROMANCY.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_NECROMANCY.getValue(), 20}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_NECROMANCY.getValue(), 30})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Mysticism
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MANAPTS.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MANAPTS.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_MANAPTS.getValue(), 6})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Intelligence
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_INTELLIGENCE.getValue(), 25}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_INTELLIGENCE.getValue(), 50}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_INTELLIGENCE.getValue(), 100})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Resistance
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_RESIST.getValue(), 5}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_RESIST.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_RESIST.getValue(), 20})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Sorcery
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_SORCERY.getValue(), 5}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_SORCERY.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_SORCERY.getValue(), 15})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Learning
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LEARNING.getValue(), 5}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LEARNING.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LEARNING.getValue(), 15})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Scouting
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_SCOUTING.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_SCOUTING.getValue(), 4}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_SCOUTING.getValue(), 6})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Logistics
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LOGISTICS.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LOGISTICS.getValue(), 20}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_LOGISTICS.getValue(), 30})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Pathfinding
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_PATHFINDING.getValue(), 25}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_PATHFINDING.getValue(), 50}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_PATHFINDING.getValue(), 75})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Archery
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ARCHERY.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ARCHERY.getValue(), 25}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ARCHERY.getValue(), 50})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Ballistics
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_BALLISTICS.getValue(), 1}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_BALLISTICS.getValue(), 2}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_BALLISTICS.getValue(), 3})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Offence
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_OFFENCE.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_OFFENCE.getValue(), 20}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_OFFENCE.getValue(), 30})
                }),
                new ConstArray<SECSKILL_LEVEL, SimpleConstArray<Integer>> (new SimpleConstArray[] {
                    // Armorer
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ARMORER.getValue(), 5}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ARMORER.getValue(), 10}),
                    new SimpleConstArray<Integer>(new Integer[] {FURTHER_SKILLS.FSK_ARMORER.getValue(), 15})
                })
            });

}
