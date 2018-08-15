package com.github.servb.pph.pheroes.common.creature

import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class CreatureType(override val v: Int) : UniqueValueEnum, UndefinedCountValueEnum {
    UNKNOWN(-1),
    /* Knight */
    PEASANT(0),
    ARCHER(1),
    PIKEMAN(2),
    MONK(3),
    CAVALRY(4),
    PALADIN(5),

    /* Barbarian */
    GOBLIN(6),
    ORC(7),
    WARG_RIDER(8),
    OGRE(9),
    TROLL(10),
    CYCLOP(11),

    /* Wizard */
    YOUNG_MAGE(12),
    WHITE_WOLF(13),
    LIVING_ARMOR(14),
    PEGASUS(15),
    MAGE(16),
    THOR(17),

    /* Warlock */
    CENTAUR(18),
    GARGOYLE(19),
    GRIFFIN(20),
    MINOTAUR(21),
    HYDRA(22),
    RED_DRAGON(23),

    /* Sorcer */
    SPRITE(24),
    DWARF(25),
    ELF(26),
    DRUID(27),
    UNICORN(28),
    FIREBIRD(29),

    /* Necromant */
    SKELETON(30),
    ZOMBIE(31),
    LICH(32),
    VAMPIRE(33),
    BLACK_KNIGHT(34),
    PLAGUE(35),

    /* Neutral = 9 */
    ROGUE(36),
    NOMAD(37),
    GHOST(38),
    GENIE(39),
    MEDUSA(40),
    EARTH_ELEMENTAL(41),
    AIR_ELEMENTAL(42),
    FIRE_ELEMENTAL(43),
    WATER_ELEMENTAL(44),

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