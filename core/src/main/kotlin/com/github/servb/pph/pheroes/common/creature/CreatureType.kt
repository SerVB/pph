package com.github.servb.pph.pheroes.common.creature

import com.github.servb.pph.pheroes.common.common.MineralSet
import com.github.servb.pph.pheroes.common.common.NationType
import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

// TODO: REMOVE COMMENTS ON THE RIGHT

enum class CreatureType(
        override val v: Int,
        val descriptor: CreatureDescriptor? = null
) : UniqueValueEnum, UndefinedCountValueEnum {
    UNKNOWN(-1),
    /* Knight */
    PEASANT(
            0,
            CreatureDescriptor(
                    1, NationType.HIGHMEN,
                    1, 2,
                    4, 1, TransportationType.WALK, 0,
                    2,
                    1, 1,
                    MineralSet(25, 0, 0, 0, 0, 0, 0),
                    22, 15, Perk.NONE.v
            )
    ),
    ARCHER(
            1,
            CreatureDescriptor(
                    2, NationType.HIGHMEN,
                    5, 4,
                    5, 1, TransportationType.WALK, 24,
                    10,
                    2, 3,
                    MineralSet(200, 0, 0, 0, 0, 0, 0),
                    10, 140, Perk.DOUBLESHOT.v
            )
    ),
    PIKEMAN(
            2,
            CreatureDescriptor(
                    3, NationType.HIGHMEN,
                    5, 9,
                    5, 1, TransportationType.WALK, 0,
                    25,
                    3, 4,
                    MineralSet(250, 0, 0, 0, 0, 0, 0),
                    7, 250, Perk.NONE.v
            )
    ),
    MONK(
            3,
            CreatureDescriptor(
                    4, NationType.HIGHMEN,
                    7, 9,
                    7, 1, TransportationType.WALK, 15,
                    30,
                    4, 6,
                    MineralSet(325, 0, 0, 0, 0, 0, 0),
                    6, 409, Perk.NOMELEEPENALTY.v
            )
    ),
    CAVALRY(
            4,
            CreatureDescriptor(
                    5, NationType.HIGHMEN,
                    10, 9,
                    10, 2, TransportationType.WALK, 0,
                    40,
                    5, 10,
                    MineralSet(450, 0, 0, 0, 0, 0, 0),
                    5, 689, Perk.JOUSTING.v
            )
    ),
    PALADIN(
            5,
            CreatureDescriptor(
                    6, NationType.HIGHMEN,
                    11, 13,
                    10, 1, TransportationType.WALK, 0,
                    80,
                    10, 20,
                    MineralSet(1200, 0, 0, 0, 0, 1, 0),
                    4, 1764, Perk.DOUBLEATTACK.v
            )
    ),

    /* Barbarian */
    GOBLIN(
            6,
            CreatureDescriptor(
                    1, NationType.BARBARIANS,
                    4, 1,
                    5, 1, TransportationType.WALK, 0,
                    3,
                    1, 2,
                    MineralSet(40, 0, 0, 0, 0, 0, 0),
                    20, 33, Perk.NONE.v
            )
    ),
    ORC(
            7,
            CreatureDescriptor(
                    2, NationType.BARBARIANS,
                    3, 4,
                    4, 1, TransportationType.WALK, 5,
                    15,
                    3, 4,
                    MineralSet(185, 0, 0, 0, 0, 0, 0),
                    10, 155, Perk.NONE.v
            )
    ),
    WARG_RIDER(
            8,
            CreatureDescriptor(
                    3, NationType.BARBARIANS,
                    6, 4,
                    9, 2, TransportationType.WALK, 0,
                    20,
                    3, 5,
                    MineralSet(250, 0, 0, 0, 0, 0, 0),
                    8, 262, Perk.DOUBLEATTACK.v
            )
    ),
    OGRE(
            9,
            CreatureDescriptor(
                    4, NationType.BARBARIANS,
                    9, 7,
                    5, 1, TransportationType.WALK, 0,
                    55,
                    5, 7,
                    MineralSet(500, 0, 0, 0, 0, 0, 0),
                    6, 627, Perk.NONE.v
            )
    ),
    TROLL(
            10,
            CreatureDescriptor(
                    5, NationType.BARBARIANS,
                    10, 6,
                    8, 1, TransportationType.WALK, 15,
                    40,
                    7, 12,
                    MineralSet(700, 0, 0, 0, 0, 0, 0),
                    5, 807, Perk.REGENERATES.v
            )
    ),
    CYCLOP(
            11,
            CreatureDescriptor(
                    6, NationType.BARBARIANS,
                    12, 9,
                    9, 1, TransportationType.WALK, 0,
                    100,
                    12, 24,
                    MineralSet(1500, 0, 0, 0, 0, 1, 0),
                    4, 2036, Perk.TWOHEXATTACK.v
            )
    ),

    /* Wizard */
    YOUNG_MAGE(
            12,
            CreatureDescriptor(
                    1, NationType.WIZARDS,
                    2, 1,
                    3, 1, TransportationType.WALK, 5,
                    4,
                    1, 3,
                    MineralSet(60, 0, 0, 0, 0, 0, 0),
                    18, 41, Perk.NONE.v
            )
    ),
    WHITE_WOLF(
            13,
            CreatureDescriptor(
                    2, NationType.WIZARDS,
                    5, 4,
                    8, 2, TransportationType.WALK, 0,
                    15,
                    2, 3,
                    MineralSet(150, 0, 0, 0, 0, 0, 0),
                    8, 149, Perk.NONE.v
            )
    ),
    LIVING_ARMOR(
            14,
            CreatureDescriptor(
                    3, NationType.WIZARDS,
                    6, 9,
                    4, 1, TransportationType.WALK, 0,
                    35,
                    4, 5,
                    MineralSet(300, 0, 0, 0, 0, 0, 0),
                    6, 401, Perk.LIFELESS.v or Perk.QUARTERDMG.v
            )
    ),
    PEGASUS(
            15,
            CreatureDescriptor(
                    4, NationType.WIZARDS,
                    7, 7,
                    9, 2, TransportationType.FLY, 0,
                    40,
                    5, 8,
                    MineralSet(400, 0, 0, 0, 0, 0, 0),
                    5, 527, Perk.NONE.v
            )
    ),
    MAGE(
            16,
            CreatureDescriptor(
                    5, NationType.WIZARDS,
                    12, 8,
                    9, 1, TransportationType.WALK, 15,
                    35,
                    7, 10,
                    MineralSet(700, 0, 0, 0, 0, 0, 0),
                    5, 748, Perk.NOMELEEPENALTY.v
            )
    ),
    THOR(
            17,
            CreatureDescriptor(
                    6, NationType.WIZARDS,
                    13, 13,
                    14, 1, TransportationType.WALK, 15,
                    300,
                    15, 30,
                    MineralSet(3500, 0, 0, 0, 1, 0, 0),
                    3, 6950, Perk.NOMELEEPENALTY.v
            )
    ),

    /* Warlock */
    CENTAUR(
            18,
            CreatureDescriptor(
                    1, NationType.BEASTMEN,
                    3, 1,
                    5, 2, TransportationType.WALK, 5,
                    5,
                    1, 2,
                    MineralSet(60, 0, 0, 0, 0, 0, 0),
                    18, 47, Perk.NONE.v
            )
    ),
    GARGOYLE(
            19,
            CreatureDescriptor(
                    2, NationType.BEASTMEN,
                    4, 7,
                    9, 1, TransportationType.FLY, 0,
                    15,
                    2, 3,
                    MineralSet(200, 0, 0, 0, 0, 0, 0),
                    8, 175, Perk.LIFELESS.v
            )
    ),
    GRIFFIN(
            20,
            CreatureDescriptor(
                    3, NationType.BEASTMEN,
                    6, 6,
                    8, 2, TransportationType.FLY, 0,
                    25,
                    3, 5,
                    MineralSet(300, 0, 0, 0, 0, 0, 0),
                    6, 334, Perk.RETALTOALL.v
            )
    ),
    MINOTAUR(
            21,
            CreatureDescriptor(
                    4, NationType.BEASTMEN,
                    9, 8,
                    8, 1, TransportationType.WALK, 0,
                    50,
                    5, 10,
                    MineralSet(500, 0, 0, 0, 0, 0, 0),
                    5, 682, Perk.NONE.v
            )
    ),
    HYDRA(
            22,
            CreatureDescriptor(
                    5, NationType.BEASTMEN,
                    8, 9,
                    4, 2, TransportationType.WALK, 0,
                    60,
                    6, 12,
                    MineralSet(750, 0, 0, 0, 0, 0, 0),
                    4, 872, Perk.NONRETALATTACK.v or Perk.ADJACENTATTACK.v
            )
    ),
    RED_DRAGON(
            23,
            CreatureDescriptor(
                    6, NationType.BEASTMEN,
                    13, 12,
                    13, 2, TransportationType.FLY, 0,
                    250,
                    20, 40,
                    MineralSet(4000, 0, 0, 0, 0, 0, 1),
                    3, 8528, Perk.ALLMAGICIMM.v or Perk.TWOHEXATTACK.v

            )
    ),

    /* Sorcer */
    SPRITE(
            24,
            CreatureDescriptor(
                    1, NationType.ELVES,
                    4, 2,
                    7, 1, TransportationType.FLY, 0,
                    2,
                    1, 2,
                    MineralSet(60, 0, 0, 0, 0, 0, 0),
                    20, 39, Perk.NONRETALATTACK.v
            )
    ),
    DWARF(
            25,
            CreatureDescriptor(
                    2, NationType.ELVES,
                    6, 6,
                    5, 1, TransportationType.WALK, 0,
                    20,
                    2, 4,
                    MineralSet(250, 0, 0, 0, 0, 0, 0),
                    8, 190, Perk.PROCRESIST40.v
            )
    ),
    ELF(
            26,
            CreatureDescriptor(
                    3, NationType.ELVES,
                    5, 5,
                    8, 1, TransportationType.WALK, 20,
                    15,
                    2, 4,
                    MineralSet(300, 0, 0, 0, 0, 0, 0),
                    7, 194, Perk.DOUBLESHOT.v
            )
    ),
    DRUID(
            27,
            CreatureDescriptor(
                    4, NationType.ELVES,
                    8, 7,
                    8, 1, TransportationType.WALK, 15,
                    25,
                    5, 8,
                    MineralSet(400, 0, 0, 0, 0, 0, 0),
                    5, 433, Perk.NOMELEEPENALTY.v
            )
    ),
    UNICORN(
            28,
            CreatureDescriptor(
                    5, NationType.ELVES,
                    10, 9,
                    9, 2, TransportationType.WALK, 0,
                    60,
                    7, 14,
                    MineralSet(650, 0, 0, 0, 0, 0, 0),
                    4, 819, Perk.NONE.v
            )
    ),
    FIREBIRD(
            29,
            CreatureDescriptor(
                    6, NationType.ELVES,
                    12, 11,
                    15, 2, TransportationType.FLY, 0,
                    150,
                    20, 40,
                    MineralSet(1700, 0, 0, 1, 0, 0, 0),
                    3, 3064, Perk.FIREMAGICIMM.v or Perk.TWOHEXATTACK.v
            )
    ),

    /* Necromant */
    SKELETON(
            30,
            CreatureDescriptor(
                    1, NationType.UNDEADS,
                    3, 3,
                    5, 1, TransportationType.WALK, 0,
                    4,
                    1, 3,
                    MineralSet(65, 0, 0, 0, 0, 0, 0),
                    18, 62, Perk.UNDEAD.v
            )
    ),
    ZOMBIE(
            31,
            CreatureDescriptor(
                    2, NationType.UNDEADS,
                    5, 2,
                    5, 1, TransportationType.WALK, 0,
                    20,
                    2, 3,
                    MineralSet(200, 0, 0, 0, 0, 0, 0),
                    8, 153, Perk.UNDEAD.v
            )
    ),
    LICH(
            32,
            CreatureDescriptor(
                    3, NationType.UNDEADS,
                    7, 6,
                    7, 1, TransportationType.WALK, 6,
                    25,
                    2, 3,
                    MineralSet(330, 0, 0, 0, 0, 0, 0),
                    6, 320, Perk.UNDEAD.v or Perk.NOMELEEPENALTY.v or Perk.LICHSHOOT.v
            )
    ),
    VAMPIRE(
            33,
            CreatureDescriptor(
                    4, NationType.UNDEADS,
                    8, 6,
                    9, 1, TransportationType.FLY, 0,
                    40,
                    5, 7,
                    MineralSet(650, 0, 0, 0, 0, 0, 0),
                    5, 746, Perk.UNDEAD.v or Perk.NONRETALATTACK.v or Perk.DRAINLIFES.v
            )
    ),
    BLACK_KNIGHT(
            34,
            CreatureDescriptor(
                    5, NationType.UNDEADS,
                    8, 12,
                    9, 1, TransportationType.WALK, 0,
                    50,
                    8, 14,
                    MineralSet(850, 0, 0, 0, 0, 0, 0),
                    4, 795, Perk.UNDEAD.v
            )
    ),
    PLAGUE(
            35,
            CreatureDescriptor(
                    6, NationType.UNDEADS,
                    11, 9,
                    8, 2, TransportationType.FLY, 15,
                    150,
                    20, 35,
                    MineralSet(2500, 0, 0, 1, 0, 0, 0),
                    3, 3559, Perk.UNDEAD.v
            )
    ),

    /* Neutral = 9 */
    ROGUE(
            36,
            CreatureDescriptor(
                    1, NationType.NEUTRAL,
                    6, 1,
                    6, 1, TransportationType.WALK, 0,
                    4,
                    1, 2,
                    MineralSet(50, 0, 0, 0, 0, 0, 0),
                    8, 54, Perk.NONRETALATTACK.v
            )
    ),
    NOMAD(
            37,
            CreatureDescriptor(
                    3, NationType.NEUTRAL,
                    7, 5,
                    9, 2, TransportationType.WALK, 0,
                    20,
                    2, 5,
                    MineralSet(200, 0, 0, 0, 0, 0, 0),
                    6, 244, Perk.NONE.v
            )
    ),
    GHOST(
            38,
            CreatureDescriptor(
                    5, NationType.NEUTRAL,
                    8, 6,
                    7, 1, TransportationType.FLY, 0,
                    20,
                    4, 6,
                    MineralSet(1000, 0, 0, 0, 0, 0, 0),
                    4, 468, Perk.UNDEAD.v or Perk.GHOST.v
            )
    ),
    GENIE(
            39,
            CreatureDescriptor(
                    6, NationType.NEUTRAL,
                    10, 9,
                    10, 1, TransportationType.FLY, 0,
                    50,
                    20, 30,
                    MineralSet(750, 0, 0, 0, 1, 0, 0),
                    3, 1725, Perk.DOHALF.v
            )
    ),
    MEDUSA(
            40,
            CreatureDescriptor(
                    4, NationType.NEUTRAL,
                    8, 9,
                    5, 2, TransportationType.WALK, 0,
                    35,
                    6, 10,
                    MineralSet(350, 0, 0, 0, 0, 0, 0),
                    4, 600, Perk.NONE.v
            )
    ),
    EARTH_ELEMENTAL(
            41,
            CreatureDescriptor(
                    4, NationType.NEUTRAL,
                    8, 8,
                    4, 1, TransportationType.WALK, 0,
                    50,
                    4, 5,
                    MineralSet(500, 0, 0, 0, 0, 0, 0),
                    4, 525, Perk.EARTHMAGICIMM.v or Perk.LIFELESS.v
            )
    ),
    AIR_ELEMENTAL(
            42,
            CreatureDescriptor(
                    4, NationType.NEUTRAL,
                    7, 7,
                    7, 1, TransportationType.WALK, 0,
                    35,
                    2, 8,
                    MineralSet(500, 0, 0, 0, 0, 0, 0),
                    4, 428, Perk.AIRMAGICIMM.v or Perk.LIFELESS.v
            )
    ),
    FIRE_ELEMENTAL(
            43,
            CreatureDescriptor(
                    4, NationType.NEUTRAL,
                    8, 6,
                    6, 1, TransportationType.WALK, 0,
                    40,
                    4, 5,
                    MineralSet(500, 0, 0, 0, 0, 0, 0),
                    4, 455, Perk.FIREMAGICIMM.v or Perk.LIFELESS.v
            )
    ),
    WATER_ELEMENTAL(
            44,
            CreatureDescriptor(
                    4, NationType.NEUTRAL,
                    6, 8,
                    5, 1, TransportationType.WALK, 0,
                    45,
                    3, 7,
                    MineralSet(500, 0, 0, 0, 0, 0, 0),
                    4, 512, Perk.WATERMAGICIMM.v or Perk.LIFELESS.v
            )
    ),

    COUNT(45),

    /* Random values */
    RANDOM(0x0F00),
    RANDOM_L1(RANDOM.v + 1),
    RANDOM_L2(RANDOM.v + 2),
    RANDOM_L3(RANDOM.v + 3),
    RANDOM_L4(RANDOM.v + 4),
    RANDOM_L5(RANDOM.v + 5),
    RANDOM_L6(RANDOM.v + 6);
}