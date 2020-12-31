package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.pheroes.common.common.MineralSet
import com.github.servb.pph.pheroes.common.common.MineralType
import com.github.servb.pph.pheroes.common.common.skill.FurtherSkill
import com.github.servb.pph.pheroes.common.creature.CreatureType

enum class CastleConstruction(val v: Int) {
    INVALID(-1),

    // Dwellings
    DWELLINGS(0),

    DWELL_PEASNHUT(0),
    DWELL_ARCHTOWER(1),
    DWELL_GHOUSE(2),
    DWELL_ABBEY(3),
    DWELL_JARENA(4),
    DWELL_CATHEDRAL(5),
    DWELL_GBARRACKS(6),
    DWELL_ORCTOWER(7),
    DWELL_WARGCAVE(8),
    DWELL_OGREFORT(9),
    DWELL_TRBRIDGE(10),
    DWELL_CYCAVE(11),
    DWELL_MAGICSCHOOL(12),
    DWELL_WHITEWOLFDEN(13),
    DWELL_FACTORY(14),
    DWELL_MAGICCLOUD(15),
    DWELL_MAGETOWER(16),
    DWELL_CLDPALACE(17),
    DWELL_CENTCAVE(18),
    DWELL_CRYPT(19),
    DWELL_GRIFTOWER(20),
    DWELL_LABYRINTH(21),
    DWELL_HYDRAPOND(22),
    DWELL_DRAGCAVE(23),
    DWELL_TREEHOUSE(24),
    DWELL_DWCOTTAGE(25),
    DWELL_HOMESTEAD(26),
    DWELL_STONEHENGE(27),
    DWELL_UNGLADE(28),
    DWELL_REDTOWER(29),
    DWELL_EXCAVATION(30),
    DWELL_GRAVEYARD(31),
    DWELL_MAUSOLEUM(32),
    DWELL_SARCOPHAGUS(33),
    DWELL_DARKTOMB(34),
    DWELL_CURSEDTOWER(35),

    // Common constructions
    MAGEGUILDS(26),

    MAGEGUILD_L1(26),
    MAGEGUILD_L2(27),
    MAGEGUILD_L3(28),
    MAGEGUILD_L4(29),
    MAGEGUILD_L5(30),
    MAGICNODE(31),
    TAVERN(32),
    MARKETPLACE(33),

    // Common mines
    MINES(34),

    TOWNHALL(34),
    CITYHALL(35),
    OREMINE(36),
    SAWMILL(37),
    ALCHLAB(38),
    GEMSMINE(39),
    CRYSTALMINE(40),
    SULFURMINE(41),

    MOAT(42),
    MTURRET(43),
    LTURRET(44),
    RTURRET(45),

    // Dwelling enchancers
    SHOOTINGRANGE(46),
    MESSHALL(47),
    OAKWOOD(48),
    WATERFALL(49),
    MINERSGUILD(50),
    UNEARTHEDGRAVES(51),

    // Other special constructions
    OBSERVPOST(52),
    FORTIFICATION(53),
    HALLOFVALHALLA(54),
    ADOBE(55),
    WALLOFKNOWLEDGE(56),
    LIBRARY(57),
    ALTAR(58),
    MANAVORTEX(59),
    TREASURY(60),
    MYSTICPOUND(61),
    NECRAMPLIFIER(62),
    COVEROFDARKNESS(63),

    COUNT(64);
}

@ExperimentalUnsignedTypes
val CTLCNSTS_DESC = arrayListOf(
        CTLCNST_DESC_STRUCT(
                "Peasants' hut",
                CastleConstructionType.DWELLING,
                MineralSet(gold = 200),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.CITADEL),
                CreatureType.PEASANT.v,
                0
        ),
        CTLCNST_DESC_STRUCT(
                "Archers' tower",
                CastleConstructionType.DWELLING,
                MineralSet(gold = 1000, wood = 5),
                setOf(CastleConstruction.DWELL_PEASNHUT),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.CITADEL),
                CreatureType.ARCHER.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Guardhouse",
                CastleConstructionType.DWELLING,
                MineralSet(1500, 5),
                setOf(CastleConstruction.DWELL_ARCHTOWER),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.CITADEL),
                CreatureType.PIKEMAN.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Abbey",
                CastleConstructionType.DWELLING,
                MineralSet(2500, 4, 5, 0, 2, 0, 2),
                setOf(CastleConstruction.DWELL_GHOUSE),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.CITADEL),
                CreatureType.MONK.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Jousting arena",
                CastleConstructionType.DWELLING,
                MineralSet(3000, 0, 20),
                setOf(CastleConstruction.DWELL_ABBEY),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.CITADEL),
                CreatureType.CAVALRY.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Cathedral",
                CastleConstructionType.DWELLING,
                MineralSet(8000, 0, 20, 0, 0, 20),
                setOf(CastleConstruction.DWELL_ABBEY),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.CITADEL),
                CreatureType.PALADIN.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Goblin barracks",
                CastleConstructionType.DWELLING,
                MineralSet(300),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.STRONGHOLD),
                CreatureType.GOBLIN.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Orc tower",
                CastleConstructionType.DWELLING,
                MineralSet(800, 0, 5),
                setOf(CastleConstruction.DWELL_GBARRACKS),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.STRONGHOLD),
                CreatureType.ORC.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Warg cave",
                CastleConstructionType.DWELLING,
                MineralSet(1500),
                setOf(CastleConstruction.DWELL_ORCTOWER),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.STRONGHOLD),
                CreatureType.WARG_RIDER.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Ogre fort",
                CastleConstructionType.DWELLING,
                MineralSet(2000, 10, 10),
                setOf(CastleConstruction.DWELL_ORCTOWER),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.STRONGHOLD),
                CreatureType.OGRE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Troll bridge",
                CastleConstructionType.DWELLING,
                MineralSet(4000, 20),
                setOf(CastleConstruction.DWELL_OGREFORT),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.STRONGHOLD),
                CreatureType.TROLL.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Cyclops cave",
                CastleConstructionType.DWELLING,
                MineralSet(10000, 20, 0, 0, 0, 20),
                setOf(CastleConstruction.DWELL_WARGCAVE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.STRONGHOLD),
                CreatureType.CYCLOP.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Magic school",
                CastleConstructionType.DWELLING,
                MineralSet(400),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.TOWER),
                CreatureType.YOUNG_MAGE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "White wolf den",
                CastleConstructionType.DWELLING,
                MineralSet(800),
                setOf(CastleConstruction.DWELL_MAGICSCHOOL),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.TOWER),
                CreatureType.WHITE_WOLF.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Factory",
                CastleConstructionType.DWELLING,
                MineralSet(1500, 5, 5, 3),
                setOf(CastleConstruction.DWELL_MAGICSCHOOL),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.TOWER),
                CreatureType.LIVING_ARMOR.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Magic cloud/Cliff in the clouds/Magic Garden",
                CastleConstructionType.DWELLING,
                MineralSet(3000, 0, 5),
                setOf(CastleConstruction.DWELL_WHITEWOLFDEN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.TOWER),
                CreatureType.PEGASUS.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mage tower",
                CastleConstructionType.DWELLING,
                MineralSet(3500, 5, 5, 5, 5, 5, 5),
                setOf(CastleConstruction.DWELL_FACTORY, CastleConstruction.MAGEGUILD_L1),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.TOWER),
                CreatureType.MAGE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Cloud palace",
                CastleConstructionType.DWELLING,
                MineralSet(17500, 5, 5, 0, 20),
                setOf(CastleConstruction.DWELL_MAGICCLOUD, CastleConstruction.DWELL_MAGETOWER),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.TOWER),
                CreatureType.THOR.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Centaurs' cave",
                CastleConstructionType.DWELLING,
                MineralSet(500),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.DUNGEON),
                CreatureType.CENTAUR.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Crypt",
                CastleConstructionType.DWELLING,
                MineralSet(1000, 10),
                setOf(CastleConstruction.DWELL_CENTCAVE),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.DUNGEON),
                CreatureType.GARGOYLE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Griffin tower",
                CastleConstructionType.DWELLING,
                MineralSet(2000),
                setOf(CastleConstruction.DWELL_CRYPT),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.DUNGEON),
                CreatureType.GRIFFIN.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Labyrinth",
                CastleConstructionType.DWELLING,
                MineralSet(3000, 0, 0, 0, 10),
                setOf(CastleConstruction.DWELL_GRIFTOWER),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.DUNGEON),
                CreatureType.MINOTAUR.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Hydra pond",
                CastleConstructionType.DWELLING,
                MineralSet(4000, 0, 0, 0, 0, 0, 10),
                setOf(CastleConstruction.DWELL_LABYRINTH),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.DUNGEON),
                CreatureType.HYDRA.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Dragon cave",
                CastleConstructionType.DWELLING,
                MineralSet(22500, 20, 0, 0, 0, 0, 20),
                setOf(CastleConstruction.DWELL_HYDRAPOND, CastleConstruction.MAGEGUILD_L2),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.DUNGEON),
                CreatureType.RED_DRAGON.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Treehouse",
                CastleConstructionType.DWELLING,
                MineralSet(500, 0, 5),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.FORTRESS),
                CreatureType.SPRITE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Dwarf cottage",
                CastleConstructionType.DWELLING,
                MineralSet(1500, 0, 5),
                setOf(CastleConstruction.DWELL_TREEHOUSE),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.FORTRESS),
                CreatureType.DWARF.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Homestead",
                CastleConstructionType.DWELLING,
                MineralSet(2000, 0, 10),
                setOf(CastleConstruction.DWELL_DWCOTTAGE),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.FORTRESS),
                CreatureType.ELF.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Stonehenge",
                CastleConstructionType.DWELLING,
                MineralSet(2500, 10, 0, 5),
                setOf(CastleConstruction.DWELL_HOMESTEAD, CastleConstruction.MAGEGUILD_L1),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.FORTRESS),
                CreatureType.DRUID.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Unicorn glade",
                CastleConstructionType.DWELLING,
                MineralSet(3000, 0, 10, 0, 10),
                setOf(CastleConstruction.DWELL_STONEHENGE),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.FORTRESS),
                CreatureType.UNICORN.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Red tower",
                CastleConstructionType.DWELLING,
                MineralSet(10000, 20, 0, 15),
                setOf(CastleConstruction.DWELL_UNGLADE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.FORTRESS),
                CreatureType.FIREBIRD.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Excavation",
                CastleConstructionType.DWELLING,
                MineralSet(400),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.NECROPOLIS),
                CreatureType.SKELETON.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Graveyard",
                CastleConstructionType.DWELLING,
                MineralSet(1500),
                setOf(CastleConstruction.DWELL_EXCAVATION),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.NECROPOLIS),
                CreatureType.ZOMBIE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mausoleum",
                CastleConstructionType.DWELLING,
                MineralSet(2000, 10, 0, 0, 0, 5),
                setOf(CastleConstruction.DWELL_GRAVEYARD),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.NECROPOLIS),
                CreatureType.LICH.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Sarcophagus",
                CastleConstructionType.DWELLING,
                MineralSet(3000, 0, 10, 0, 5),
                setOf(CastleConstruction.DWELL_MAUSOLEUM),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.NECROPOLIS),
                CreatureType.VAMPIRE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Dark Tomb",
                CastleConstructionType.DWELLING,
                MineralSet(5000, 0, 10, 0, 0, 0, 10),
                setOf(CastleConstruction.DWELL_SARCOPHAGUS),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.NECROPOLIS),
                CreatureType.BLACK_KNIGHT.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Cursed Tower",
                CastleConstructionType.DWELLING,
                MineralSet(12500, 10, 10, 20),
                setOf(CastleConstruction.DWELL_MAUSOLEUM),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.NECROPOLIS),
                CreatureType.PLAGUE.v, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mage guild 1",
                CastleConstructionType.MAGEGUILD,
                MineralSet(2000, 5, 5),
                setOf(),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.ALL),
                1, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mage guild 2",
                CastleConstructionType.MAGEGUILD,
                MineralSet(1000, 5, 5, 4, 4, 4, 4),
                setOf(CastleConstruction.MAGEGUILD_L1),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.ALL),
                2, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mage guild 3",
                CastleConstructionType.MAGEGUILD,
                MineralSet(1000, 5, 5, 6, 6, 6, 6),
                setOf(CastleConstruction.MAGEGUILD_L2),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.ALL),
                3, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mage guild 4",
                CastleConstructionType.MAGEGUILD,
                MineralSet(1000, 5, 5, 8, 8, 8, 8),
                setOf(CastleConstruction.MAGEGUILD_L3),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.MAGIC.v or CastleTypeMask.MISC.v),
                4, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mage guild 5",
                CastleConstructionType.MAGEGUILD,
                MineralSet(1000, 5, 5, 10, 10, 10, 10),
                setOf(CastleConstruction.MAGEGUILD_L4),
                CTCNSTCAP(CastleSizeMask.L, CastleTypeMask.MAGIC.v or CastleTypeMask.MISC.v),
                5, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Magic node",
                CastleConstructionType.MAGICNODE,
                MineralSet(3000, 10, 0, 2, 2, 2, 2),
                setOf(CastleConstruction.MAGEGUILD_L2),
                CTCNSTCAP(CastleSizeMask.L, CastleTypeMask.MAGIC),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Tavern",
                CastleConstructionType.TAVERN,
                MineralSet(1000, 0, 5),
                setOf(CastleConstruction.INVALID),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.ALL),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Marketplace",
                CastleConstructionType.GENERIC,
                MineralSet(500, 0, 5),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.ALL),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Town hall",
                CastleConstructionType.MINE,
                MineralSet(2500),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.M, CastleTypeMask.ALL),
                MineralType.GOLD.v, 500
        ),
        CTLCNST_DESC_STRUCT(
                "City hall",
                CastleConstructionType.MINE,
                MineralSet(5000),
                setOf(CastleConstruction.MAGEGUILD_L1, CastleConstruction.MARKETPLACE),
                CTCNSTCAP(CastleSizeMask.L, CastleTypeMask.ALL),
                MineralType.GOLD.v, 1500
        ),
        CTLCNST_DESC_STRUCT(
                "Ore mine",
                CastleConstructionType.MINE,
                MineralSet(3000, 5),
                setOf(CastleConstruction.MARKETPLACE),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.MIGHT.v or CastleTypeMask.MISC.v),
                MineralType.ORE.v, 1
        ),
        CTLCNST_DESC_STRUCT(
                "Sawmill",
                CastleConstructionType.MINE,
                MineralSet(3000, 0, 5),
                setOf(CastleConstruction.MARKETPLACE),
                CTCNSTCAP(CastleSizeMask.SML, CastleTypeMask.MIGHT.v or CastleTypeMask.MISC.v),
                MineralType.WOOD.v, 1
        ),
        CTLCNST_DESC_STRUCT(
                "Alchemists' laboratory",
                CastleConstructionType.MINE,
                MineralSet(5000, 5, 5),
                setOf(CastleConstruction.MARKETPLACE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.MISC),
                MineralType.MERCURY.v, 1
        ),
        CTLCNST_DESC_STRUCT(
                "Gems mine",
                CastleConstructionType.MINE,
                MineralSet(5000, 5, 5),
                setOf(CastleConstruction.MARKETPLACE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.TOWER),
                MineralType.GEMS.v, 1
        ),
        CTLCNST_DESC_STRUCT(
                "Crystal mine",
                CastleConstructionType.MINE,
                MineralSet(5000, 5, 5),
                setOf(CastleConstruction.MARKETPLACE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.MIGHT),
                MineralType.CRYSTAL.v, 1
        ),
        CTLCNST_DESC_STRUCT(
                "Sulfur mine",
                CastleConstructionType.MINE,
                MineralSet(5000, 5, 5),
                setOf(CastleConstruction.MARKETPLACE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.DUNGEON),
                MineralType.SULFUR.v, 1
        ),
        CTLCNST_DESC_STRUCT(
                "Moat",
                CastleConstructionType.GENERIC,
                MineralSet(1000, 5, 5),
                setOf(),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.ALL),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Main turret",
                CastleConstructionType.GENERIC,
                MineralSet(2500, 5),
                setOf(),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.ALL),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Left turret",
                CastleConstructionType.GENERIC,
                MineralSet(1500, 5),
                setOf(CastleConstruction.MTURRET),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.MIGHT),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Right turret",
                CastleConstructionType.GENERIC,
                MineralSet(1500, 5),
                setOf(CastleConstruction.MTURRET),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.MIGHT),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Shooting range",
                CastleConstructionType.DWELL_ENC,
                MineralSet(1000, 0, 5),
                setOf(CastleConstruction.DWELL_ARCHTOWER),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.CITADEL),
                CastleConstruction.DWELL_ARCHTOWER.v, CreatureType.ARCHER.descriptor!!.growth
        ),
        CTLCNST_DESC_STRUCT(
                "Mess hall",
                CastleConstructionType.DWELL_ENC,
                MineralSet(1000, 5),
                setOf(CastleConstruction.DWELL_ORCTOWER),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.STRONGHOLD),
                CastleConstruction.DWELL_ORCTOWER.v, CreatureType.ORC.descriptor!!.growth
        ),
        CTLCNST_DESC_STRUCT(
                "Oak wood",
                CastleConstructionType.DWELL_ENC,
                MineralSet(1500),
                setOf(CastleConstruction.DWELL_WHITEWOLFDEN),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.TOWER),
                CastleConstruction.DWELL_WHITEWOLFDEN.v, CreatureType.WHITE_WOLF.descriptor!!.growth
        ),
        CTLCNST_DESC_STRUCT(
                "Waterfall",
                CastleConstructionType.DWELL_ENC,
                MineralSet(1000, 5),
                setOf(CastleConstruction.DWELL_CENTCAVE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.DUNGEON),
                CastleConstruction.DWELL_CENTCAVE.v, CreatureType.CENTAUR.descriptor!!.growth
        ),
        CTLCNST_DESC_STRUCT(
                "Miners guild",
                CastleConstructionType.DWELL_ENC,
                MineralSet(500, 5, 5),
                setOf(CastleConstruction.DWELL_DWCOTTAGE),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.FORTRESS),
                CastleConstruction.DWELL_DWCOTTAGE.v, CreatureType.DWARF.descriptor!!.growth
        ),
        CTLCNST_DESC_STRUCT(
                "Unearthed graves",
                CastleConstructionType.DWELL_ENC,
                MineralSet(1500),
                setOf(CastleConstruction.DWELL_EXCAVATION),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.NECROPOLIS),
                CastleConstruction.DWELL_EXCAVATION.v, CreatureType.SKELETON.descriptor!!.growth
        ),
        CTLCNST_DESC_STRUCT(
                "Observation post",
                CastleConstructionType.OBSERVPOST,
                MineralSet(2500, 5, 5),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.CITADEL),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Fortification",
                CastleConstructionType.GENERIC,
                MineralSet(1000, 10),
                setOf(CastleConstruction.INVALID),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.CITADEL),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Hall of Valhalla",
                CastleConstructionType.PERM_FSK_MOD,
                MineralSet(1000, 5, 5),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.STRONGHOLD),
                FurtherSkill.ATTACK.v, +1
        ),
        CTLCNST_DESC_STRUCT(
                "Adobe",
                CastleConstructionType.DWELL_ENC,
                MineralSet(2500, 10),
                setOf(CastleConstruction.DWELL_OGREFORT),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.STRONGHOLD),
                CastleConstruction.DWELL_OGREFORT.v, CreatureType.OGRE.descriptor!!.growth
        ),
        CTLCNST_DESC_STRUCT(
                "Wall of knowledge",
                CastleConstructionType.PERM_FSK_MOD,
                MineralSet(2000, 5),
                setOf(CastleConstruction.MAGEGUILD_L1),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.TOWER),
                FurtherSkill.KNOWLEDGE.v, +1
        ),
        CTLCNST_DESC_STRUCT(
                "Library",
                CastleConstructionType.LIBRARY,
                MineralSet(5000, 5, 5, 5, 5, 5, 5),
                setOf(CastleConstruction.MAGEGUILD_L2),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.TOWER),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Altar",
                CastleConstructionType.MINE,
                MineralSet(5000, 5),
                setOf(CastleConstruction.TAVERN),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.DUNGEON),
                MineralType.GOLD.v, 1000
        ),
        CTLCNST_DESC_STRUCT(
                "Mana vortex",
                CastleConstructionType.MANAVORTEX,
                MineralSet(2500, 0, 0, 1, 1, 1, 1),
                setOf(CastleConstruction.MAGEGUILD_L1),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.DUNGEON),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Treasury",
                CastleConstructionType.TREASURY,
                MineralSet(5000, 5, 5),
                setOf(CastleConstruction.MINERSGUILD),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.FORTRESS),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Mystic pound",
                CastleConstructionType.MYSTICPOUND,
                MineralSet(1500, 1, 1, 1, 1, 1, 1),
                setOf(CastleConstruction.INVALID),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.FORTRESS),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Necromancy amplifier",
                CastleConstructionType.NECRAMPLIFIER,
                MineralSet(gold = 2500),
                setOf(CastleConstruction.INVALID),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.NECROPOLIS),
                0, 0
        ),
        CTLCNST_DESC_STRUCT(
                "Cover of darkness",
                CastleConstructionType.COVEROFDARKNESS,
                MineralSet(gold = 1500, ore = 5),
                setOf(CastleConstruction.INVALID),
                CTCNSTCAP(CastleSizeMask.ML, CastleTypeMask.NECROPOLIS),
                0, 0
        )
)
