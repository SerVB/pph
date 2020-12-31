package com.github.servb.pph.pheroes.common.magic

import com.github.servb.pph.pheroes.common.common.skill.FurtherSkill
import com.github.servb.pph.pheroes.common.creature.CreatureType
import com.github.servb.pph.util.helpertype.or

/** Spell effect descriptor (4 x 2 = 8 bytes). */
@ExperimentalUnsignedTypes
data class SpellEffectDescriptor(
        val tgtType: UShort,
        val tgtMode: UShort,
        val fparam: Short,
        val sparam: Short
) {
    constructor(
            tgtType: SpellTargetTypeMask,
            tgtMode: SpellTargetMode,
            fparam: Int = 0,
            sparam: Int = 0
    ) : this(
            tgtType = tgtType.v.toUShort(),
            tgtMode = tgtMode.v.toUShort(),
            fparam = fparam.toShort(),
            sparam = sparam.toShort()
    )

    constructor(
            tgtType: SpellTargetTypeMask,
            tgtMode: SpellTargetMode,
            fparam: Short,
            sparam: MagicSpell
    ) : this(
            tgtType = tgtType.v.toUShort(),
            tgtMode = tgtMode.v.toUShort(),
            fparam = fparam,
            sparam = sparam.v.toShort()
    )

    constructor(
            tgtType: SpellTargetTypeMask,
            tgtMode: SpellTargetMode,
            fparam: FurtherSkill,
            sparam: Short
    ) : this(
            tgtType = tgtType.v.toUShort(),
            tgtMode = tgtMode.v.toUShort(),
            fparam = fparam.v.toShort(),
            sparam = sparam
    )

    constructor(
            tgtType: SpellTargetTypeMask,
            tgtMode: SpellTargetMode,
            fparam: CreatureType,
            sparam: Short
    ) : this(
            tgtType = tgtType.v.toUShort(),
            tgtMode = tgtMode.v.toUShort(),
            fparam = fparam.v.toShort(),
            sparam = sparam
    )
}

/** Spell descriptor ( 8 + 8 x 4 = 40 bytes). */
@ExperimentalUnsignedTypes
data class SpellDescriptor(
        val type: UByte,
        val level: UByte,
        val school: UByte,
        val spClass: UByte,
        val bcost: UByte,
        val label: UByte,
        val reserved: UShort,

        /** Size = MSL_COUNT. */
        val eff: List<SpellEffectDescriptor>
) {
    constructor(
            type: SpellType,
            level: SpellLevel,
            school: MagicSchoolType,
            spClass: SpellClass,
            bcost: Byte,
            label: SpellLabel,
            reserved: Short,
            eff: List<SpellEffectDescriptor>
    ) : this(
            type = type.v.toUByte(),
            level = level.v.toUByte(),
            school = school.v.toUByte(),
            spClass = spClass.v.toUByte(),
            bcost = bcost.toUByte(),
            label = label.v.toUByte(),
            reserved = reserved.toUShort(),
            eff = eff
    )
}

/** Size = MagicSpell.COUNT. */
@ExperimentalUnsignedTypes
val spellDescriptors: List<SpellDescriptor> = listOf(

        SpellDescriptor(
                //////////////////////////////////////////////////////////////////////////
                // Air magic
                //////////////////////////////////////////////////////////////////////////

                // Magic arrow
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.AIR, SpellClass.DAMAGE, 5, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // None (pow*10)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 10
                        ),
                        SpellEffectDescriptor(
                                // Basic (pow*10)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 10
                        ),
                        SpellEffectDescriptor(
                                // Advanced (pow*12)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 12
                        ),
                        SpellEffectDescriptor(
                                // Expert (pow*15)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 15
                        )
                )
        ),
        SpellDescriptor(
                // Protection from earth
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.AIR, SpellClass.FURTSKILL, 5, SpellLabel.PROTEARTH, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_EARTH, 30
                        ),
                        SpellEffectDescriptor(
                                // basic (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_EARTH, 30
                        ),
                        SpellEffectDescriptor(
                                // advanced (50%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_EARTH, 50
                        ),
                        SpellEffectDescriptor(
                                // expert (50% + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.RES_EARTH, 50
                        )
                )
        ),
        SpellDescriptor(
                // Haste
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.AIR, SpellClass.FURTSKILL, 6, SpellLabel.HASTE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (3 hexes)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SPEED, 3
                        ),
                        SpellEffectDescriptor(
                                // basic (3 hexes)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SPEED, 3
                        ),
                        SpellEffectDescriptor(
                                // advanced (5 hexes)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SPEED, 5
                        ),
                        SpellEffectDescriptor(
                                // expert (5 hexes + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.SPEED, 5
                        )
                )
        ),
        SpellDescriptor(
                // Shield 
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.AIR, SpellClass.FURTSKILL, 5, SpellLabel.SHIELD, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (15%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SHIELD, 15
                        ),
                        SpellEffectDescriptor(
                                // basic (15%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SHIELD, 15
                        ),
                        SpellEffectDescriptor(
                                // advanced (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SHIELD, 30
                        ),
                        SpellEffectDescriptor(
                                // expert (30% + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.SHIELD, 30
                        )
                )
        ),
        SpellDescriptor(
                // Disrupting ray
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.AIR, SpellClass.DISRAY, 10, SpellLabel.DISRAY, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (-3)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 3
                        ),
                        SpellEffectDescriptor(
                                // none (-3)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 3
                        ),
                        SpellEffectDescriptor(
                                // none (-4)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 4
                        ),
                        SpellEffectDescriptor(
                                // none (-5)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 5
                        )
                )
        ),
        SpellDescriptor(
                // Lightning bolt
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.AIR, SpellClass.DAMAGE, 10, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*25)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 25
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*25)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 25
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*27)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 27
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*30)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 30
                        )
                )
        ),
        SpellDescriptor(
                // Precision
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.AIR, SpellClass.FURTSKILL, 8, SpellLabel.PRECISION, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (+3)
                                SpellTargetTypeMask.FRSHOOTERS, SpellTargetMode.CREAT_GROUP, FurtherSkill.RANGEATTACK, 3
                        ),
                        SpellEffectDescriptor(
                                // basic (+3)
                                SpellTargetTypeMask.FRSHOOTERS, SpellTargetMode.CREAT_GROUP, FurtherSkill.RANGEATTACK, 3
                        ),
                        SpellEffectDescriptor(
                                // advanced (+5)
                                SpellTargetTypeMask.FRSHOOTERS, SpellTargetMode.CREAT_GROUP, FurtherSkill.RANGEATTACK, 5
                        ),
                        SpellEffectDescriptor(
                                // expert (+5 + all)
                                SpellTargetTypeMask.FRSHOOTERS, SpellTargetMode.ALL, FurtherSkill.RANGEATTACK, 5
                        )
                )
        ),
        SpellDescriptor(
                // Air shield 
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.AIR, SpellClass.FURTSKILL, 12, SpellLabel.AIRSHIELD, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (25%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.AIRSHIELD, 25
                        ),
                        SpellEffectDescriptor(
                                // basic (25%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.AIRSHIELD, 25
                        ),
                        SpellEffectDescriptor(
                                // advanced (50%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.AIRSHIELD, 50
                        ),
                        SpellEffectDescriptor(
                                // expert (50% + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.AIRSHIELD, 50
                        )
                )
        ),
        SpellDescriptor(
                // Holy word
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.AIR, SpellClass.DAMAGE, 15, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*10)
                                SpellTargetTypeMask.ALLUNDEADS, SpellTargetMode.ALL, 10
                        ),
                        SpellEffectDescriptor(
                                // none (pow*10)
                                SpellTargetTypeMask.ALLUNDEADS, SpellTargetMode.ALL, 10
                        ),
                        SpellEffectDescriptor(
                                // none (pow*12)
                                SpellTargetTypeMask.ALLUNDEADS, SpellTargetMode.ALL, 12
                        ),
                        SpellEffectDescriptor(
                                // none (pow*15)
                                SpellTargetTypeMask.ALLUNDEADS, SpellTargetMode.ALL, 15
                        )
                )
        ),
        SpellDescriptor(
                // Counterstrike
                SpellType.COMBAT, SpellLevel.FOURTH, MagicSchoolType.AIR, SpellClass.FURTSKILL, 20, SpellLabel.COUNTERSTRIKE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (1 counterstrike)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.COUNTERSTRIKE, 1
                        ),
                        SpellEffectDescriptor(
                                // basic (1 counterstrike)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.COUNTERSTRIKE, 1
                        ),
                        SpellEffectDescriptor(
                                // advanced (2 counterstrike)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.COUNTERSTRIKE, 2
                        ),
                        SpellEffectDescriptor(
                                // expert (3 counterstrike)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.COUNTERSTRIKE, 3
                        )
                )
        ),
        SpellDescriptor(
                // Resurrect 
                SpellType.COMBAT, SpellLevel.FIFTH, MagicSchoolType.AIR, SpellClass.RESURRECT, 25, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*40)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.CREAT_GROUP, 40, 1
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*40)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.CREAT_GROUP, 40, 1
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*50)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.CREAT_GROUP, 45, 1
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*60)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.CREAT_GROUP, 50, 1
                        )
                )
        ),
        SpellDescriptor(
                // Summon air elemental
                SpellType.COMBAT, SpellLevel.FIFTH, MagicSchoolType.AIR, SpellClass.SUMMON, 25, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*3)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.AIR_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*3)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.AIR_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*4)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.AIR_ELEMENTAL, 4
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*5)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.AIR_ELEMENTAL, 5
                        )
                )
        ),
        SpellDescriptor(
                //////////////////////////////////////////////////////////////////////////
                // Earth magic
                //////////////////////////////////////////////////////////////////////////

                // Protection from air 
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.EARTH, SpellClass.FURTSKILL, 5, SpellLabel.PROTAIR, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_AIR, 30
                        ),
                        SpellEffectDescriptor(
                                // basic (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_AIR, 30
                        ),
                        SpellEffectDescriptor(
                                // advanced (50%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_AIR, 50
                        ),
                        SpellEffectDescriptor(
                                // expert (50% + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.RES_AIR, 50
                        )
                )
        ),
        SpellDescriptor(
                // Slow 
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.EARTH, SpellClass.FURTSKILL, 6, SpellLabel.SLOW, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (-3)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SPEED, -3
                        ),
                        SpellEffectDescriptor(
                                // basic (-3)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SPEED, -3
                        ),
                        SpellEffectDescriptor(
                                // advanced (-6)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.SPEED, -6
                        ),
                        SpellEffectDescriptor(
                                // expert (-6 + all)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.ALL, FurtherSkill.SPEED, -6
                        )
                )
        ),
        SpellDescriptor(
                // Stone skin 
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.EARTH, SpellClass.FURTSKILL, 5, SpellLabel.STONESKIN, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (+3)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.DEFENCE, 3
                        ),
                        SpellEffectDescriptor(
                                // basic (+3)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.DEFENCE, 3
                        ),
                        SpellEffectDescriptor(
                                // advanced (+6)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.DEFENCE, 6
                        ),
                        SpellEffectDescriptor(
                                // expert (+6 + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.DEFENCE, 6
                        )
                )
        ),
        SpellDescriptor(
                // Visions
                SpellType.OVERLAND, SpellLevel.SECOND, MagicSchoolType.EARTH, SpellClass.VISION, 10, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (5 cells)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 5
                        ),
                        SpellEffectDescriptor(
                                // none (5 cells)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 5
                        ),
                        SpellEffectDescriptor(
                                // none (10 cells)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 10
                        ),
                        SpellEffectDescriptor(
                                // none (15 cells)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 15
                        )
                )
        ),
        SpellDescriptor(
                // Earthquake
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.EARTH, SpellClass.EARTHQUAKE, 15, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (2 walls)
                                SpellTargetTypeMask.ALL, SpellTargetMode.EARTHQUAKE, 2
                        ),
                        SpellEffectDescriptor(
                                // basic (2 walls)
                                SpellTargetTypeMask.ALL, SpellTargetMode.EARTHQUAKE, 2
                        ),
                        SpellEffectDescriptor(
                                // advanced (3 walls)
                                SpellTargetTypeMask.ALL, SpellTargetMode.EARTHQUAKE, 3
                        ),
                        SpellEffectDescriptor(
                                // expert (4 walls)
                                SpellTargetTypeMask.ALL, SpellTargetMode.EARTHQUAKE, 4
                        )
                )
        ),
        SpellDescriptor(
                // Sorrow 
                SpellType.COMBAT, SpellLevel.FOURTH, MagicSchoolType.EARTH, SpellClass.FURTSKILL, 16, SpellLabel.SORROW, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (-1)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.CREAT_GROUP, FurtherSkill.MORALE, -1
                        ),
                        SpellEffectDescriptor(
                                // basic (-1)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.CREAT_GROUP, FurtherSkill.MORALE, -1
                        ),
                        SpellEffectDescriptor(
                                // advanced (-2)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.CREAT_GROUP, FurtherSkill.MORALE, -2
                        ),
                        SpellEffectDescriptor(
                                // expert (-2 + all)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.ALL, FurtherSkill.MORALE, -2
                        )
                )
        ),
        SpellDescriptor(
                // Meteor shower
                SpellType.COMBAT, SpellLevel.FOURTH, MagicSchoolType.EARTH, SpellClass.DAMAGE, 18, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*25)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 25
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*25)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 25
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*27)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 27
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*30)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 30
                        )
                )
        ),
        SpellDescriptor(
                // Town portal
                SpellType.OVERLAND, SpellLevel.FIFTH, MagicSchoolType.EARTH, SpellClass.TOWNPORTAL, 25, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (?)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 0
                        ),
                        SpellEffectDescriptor(
                                // basic (?)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 0
                        ),
                        SpellEffectDescriptor(
                                // advanced (?)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 1
                        ),
                        SpellEffectDescriptor(
                                // expert (?)
                                SpellTargetTypeMask.NONE, SpellTargetMode.NONE, 1
                        )
                )
        ),
        SpellDescriptor(
                // Implosion
                SpellType.COMBAT, SpellLevel.FIFTH, MagicSchoolType.EARTH, SpellClass.DAMAGE, 25, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*60)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 60
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*60)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 60
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*70)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 70
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*80)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 80
                        )
                )
        ),
        SpellDescriptor(
                // Summon earth elemental
                SpellType.COMBAT, SpellLevel.FIFTH, MagicSchoolType.EARTH, SpellClass.SUMMON, 25, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*3)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.EARTH_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*3)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.EARTH_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*4)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.EARTH_ELEMENTAL, 4
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*5)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.EARTH_ELEMENTAL, 5
                        )
                )
        ),
        SpellDescriptor(
                //////////////////////////////////////////////////////////////////////////
                // Fire magic
                //////////////////////////////////////////////////////////////////////////

                // Bloodlust
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.FIRE, SpellClass.FURTSKILL, 5, SpellLabel.BLOODLUST, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (+3)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.MELEEATTACK, 3
                        ),
                        SpellEffectDescriptor(
                                // basic (+3)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.MELEEATTACK, 3
                        ),
                        SpellEffectDescriptor(
                                // advanced (+6)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.MELEEATTACK, 6
                        ),
                        SpellEffectDescriptor(
                                // expert (+6 + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.MELEEATTACK, 6
                        )
                )
        ),
        SpellDescriptor(
                // Protection from water
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.FIRE, SpellClass.FURTSKILL, 5, SpellLabel.PROTWATER, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_WATER, 30
                        ),
                        SpellEffectDescriptor(
                                // basic (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_WATER, 30
                        ),
                        SpellEffectDescriptor(
                                // advanced (50%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_WATER, 50
                        ),
                        SpellEffectDescriptor(
                                // expert (50% + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.RES_WATER, 50
                        )
                )
        ),
        SpellDescriptor(
                // Curse
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.FIRE, SpellClass.BLESS, 6, SpellLabel.CURSE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (?)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.CREAT_GROUP, -1, MagicSpell.BLESS
                        ),
                        SpellEffectDescriptor(
                                // basic (?)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.CREAT_GROUP, -1, MagicSpell.BLESS
                        ),
                        SpellEffectDescriptor(
                                // advanced (?)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.CREAT_GROUP, -1, MagicSpell.BLESS
                        ),
                        SpellEffectDescriptor(
                                // expert (? + all)
                                SpellTargetTypeMask.ENNUNDEADS, SpellTargetMode.ALL, -1, MagicSpell.BLESS
                        )
                )
        ),
        SpellDescriptor(
                // Blind
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.FIRE, SpellClass.BLIND, 10, SpellLabel.BLIND, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (50% retail)
                                SpellTargetTypeMask.ENNORMALS, SpellTargetMode.CREAT_GROUP, 2
                        ),
                        SpellEffectDescriptor(
                                // basic (50% retail)
                                SpellTargetTypeMask.ENNORMALS, SpellTargetMode.CREAT_GROUP, 2
                        ),
                        SpellEffectDescriptor(
                                // advanced (25% retail)
                                SpellTargetTypeMask.ENNORMALS, SpellTargetMode.CREAT_GROUP, 4
                        ),
                        SpellEffectDescriptor(
                                // expert (no retail)
                                SpellTargetTypeMask.ENNORMALS, SpellTargetMode.CREAT_GROUP, 0
                        )
                )
        ),
        SpellDescriptor(
                // Weakness 
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.FIRE, SpellClass.FURTSKILL, 8, SpellLabel.WEAKNESS, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (-3)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.ATTACK, -3
                        ),
                        SpellEffectDescriptor(
                                // basic (-3)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.ATTACK, -3
                        ),
                        SpellEffectDescriptor(
                                // advanced (-6)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.ATTACK, -6
                        ),
                        SpellEffectDescriptor(
                                // expert (-6 + all)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.ALL, FurtherSkill.ATTACK, -6
                        )
                )
        ),
        SpellDescriptor(
                // Death ripple
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.FIRE, SpellClass.DAMAGE, 10, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*5)
                                SpellTargetTypeMask.ALLNORMALS, SpellTargetMode.ALL, 5
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*5)
                                SpellTargetTypeMask.ALLNORMALS, SpellTargetMode.ALL, 5
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*7)
                                SpellTargetTypeMask.ALLNORMALS, SpellTargetMode.ALL, 7
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*10)
                                SpellTargetTypeMask.ALLNORMALS, SpellTargetMode.ALL, 10
                        )
                )
        ),
        SpellDescriptor(
                // Fireball
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.FIRE, SpellClass.DAMAGE, 15, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*15)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 15
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*15)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 15
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*17)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 17
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*20)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BALL_SET, 20
                        )
                )
        ),
        SpellDescriptor(
                // Misfortune 
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.FIRE, SpellClass.FURTSKILL, 12, SpellLabel.MISFORTUNE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (-1)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.LUCK, -1
                        ),
                        SpellEffectDescriptor(
                                // basic (-1)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.LUCK, -1
                        ),
                        SpellEffectDescriptor(
                                // advanced (-2)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, FurtherSkill.LUCK, -2
                        ),
                        SpellEffectDescriptor(
                                // expert (-2 + all)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.ALL, FurtherSkill.LUCK, -2
                        )
                )
        ),
        SpellDescriptor(
                // Animate dead
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.FIRE, SpellClass.RESURRECT, 15, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*40)
                                SpellTargetTypeMask.FRUNDEADS, SpellTargetMode.CREAT_GROUP, 40, 1
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*40)
                                SpellTargetTypeMask.FRUNDEADS, SpellTargetMode.CREAT_GROUP, 40, 1
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*45)
                                SpellTargetTypeMask.FRUNDEADS, SpellTargetMode.CREAT_GROUP, 45, 1
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*50)
                                SpellTargetTypeMask.FRUNDEADS, SpellTargetMode.CREAT_GROUP, 50, 1
                        )
                )
        ),
        SpellDescriptor(
                // Fireblast 
                SpellType.COMBAT, SpellLevel.FOURTH, MagicSchoolType.FIRE, SpellClass.DAMAGE, 16, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*15)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BLAST_SET, 15
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*15)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BLAST_SET, 15
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*17)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BLAST_SET, 17
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*20)
                                SpellTargetTypeMask.ALL, SpellTargetMode.BLAST_SET, 20
                        )
                )
        ),
        SpellDescriptor(
                // Armageddon
                SpellType.COMBAT, SpellLevel.FIFTH, MagicSchoolType.FIRE, SpellClass.DAMAGE, 30, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*40)
                                SpellTargetTypeMask.ALL, SpellTargetMode.ALL, 40
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*40)
                                SpellTargetTypeMask.ALL, SpellTargetMode.ALL, 40
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*45)
                                SpellTargetTypeMask.ALL, SpellTargetMode.ALL, 45
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*50)
                                SpellTargetTypeMask.ALL, SpellTargetMode.ALL, 50
                        )
                )
        ),
        SpellDescriptor(
                // Summon fire elemental 
                SpellType.COMBAT, SpellLevel.FIFTH, MagicSchoolType.FIRE, SpellClass.SUMMON, 25, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*3)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.FIRE_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*3)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.FIRE_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*4)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.FIRE_ELEMENTAL, 4
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*5)
                                SpellTargetTypeMask.ALL, SpellTargetMode.SUMMON, CreatureType.FIRE_ELEMENTAL, 5
                        )
                )
        ),
        SpellDescriptor(
                //////////////////////////////////////////////////////////////////////////
                // Water magic
                //////////////////////////////////////////////////////////////////////////

                // Bless
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.WATER, SpellClass.BLESS, 5, SpellLabel.BLESS, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (?)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, 1, MagicSpell.CURSE
                        ),
                        SpellEffectDescriptor(
                                // basic (?)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, 1, MagicSpell.CURSE
                        ),
                        SpellEffectDescriptor(
                                // advanced (?)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, 1, MagicSpell.CURSE
                        ),
                        SpellEffectDescriptor(
                                // expert (? + all)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.ALL, 1, MagicSpell.CURSE
                        )
                )
        ),
        SpellDescriptor(
                // Protection from fire
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.WATER, SpellClass.FURTSKILL, 5, SpellLabel.PROTFIRE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_FIRE, 30
                        ),
                        SpellEffectDescriptor(
                                // basic (30%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_FIRE, 30
                        ),
                        SpellEffectDescriptor(
                                // advanced (50%)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.RES_FIRE, 50
                        ),
                        SpellEffectDescriptor(
                                // expert (50% + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.RES_FIRE, 50
                        )
                )
        ),
        SpellDescriptor(
                // Dispel
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.WATER, SpellClass.DISPEL, 5, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (friendly)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP
                        ),
                        SpellEffectDescriptor(
                                // basic (friendly)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP
                        ),
                        SpellEffectDescriptor(
                                // advanced (any)
                                SpellTargetTypeMask.ALL, SpellTargetMode.CREAT_GROUP
                        ),
                        SpellEffectDescriptor(
                                // expert (all)
                                SpellTargetTypeMask.ALL, SpellTargetMode.ALL
                        )
                )
        ),
        SpellDescriptor(
                // Cure
                SpellType.COMBAT, SpellLevel.FIRST, MagicSchoolType.WATER, SpellClass.CURE, 6, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*5)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.CREAT_GROUP, 5
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*5)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.CREAT_GROUP, 5
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*7)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.CREAT_GROUP, 7
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*7 + all)
                                SpellTargetTypeMask.FRNORMALS, SpellTargetMode.ALL, 7
                        )
                )
        ),
        SpellDescriptor(
                // Cold ray
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.WATER, SpellClass.DAMAGE, 8, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*20)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 20
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*20)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 20
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*22)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 22
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*25)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.CREAT_GROUP, 25
                        )
                )
        ),
        SpellDescriptor(
                // Fortune
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.WATER, SpellClass.FURTSKILL, 7, SpellLabel.FORTUNE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (+1)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.LUCK, 1
                        ),
                        SpellEffectDescriptor(
                                // basic (+1)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.LUCK, 1
                        ),
                        SpellEffectDescriptor(
                                // advanced (+2)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, FurtherSkill.LUCK, 2
                        ),
                        SpellEffectDescriptor(
                                // expert (+2 + all)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.ALL, FurtherSkill.LUCK, 2
                        )
                )
        ),
        SpellDescriptor(
                // Mirth
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.WATER, SpellClass.FURTSKILL, 12, SpellLabel.MIRTH, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (+1)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, FurtherSkill.MORALE, 1
                        ),
                        SpellEffectDescriptor(
                                // basic (+1)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, FurtherSkill.MORALE, 1
                        ),
                        SpellEffectDescriptor(
                                // advanced (+2)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, FurtherSkill.MORALE, 2
                        ),
                        SpellEffectDescriptor(
                                // expert (+2 + all)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.ALL, FurtherSkill.MORALE, 2
                        )
                )
        ),
        SpellDescriptor(
                // Cold ring
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.WATER, SpellClass.DAMAGE, 12, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (pow*10)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.RING_SET, 10
                        ),
                        SpellEffectDescriptor(
                                // basic (pow*10)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.RING_SET, 10
                        ),
                        SpellEffectDescriptor(
                                // advanced (pow*12)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.RING_SET, 12
                        ),
                        SpellEffectDescriptor(
                                // expert (pow*15)
                                SpellTargetTypeMask.ALLENEMY, SpellTargetMode.RING_SET, 15
                        )
                )
        ),
        SpellDescriptor(
                // Anti-magic
                SpellType.COMBAT, SpellLevel.THIRD, MagicSchoolType.WATER, SpellClass.ANTIMAGIC, 15, SpellLabel.ANTIMAGIC, 0,
                listOf(
                        SpellEffectDescriptor(
                                // none (1-3 level)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, SpellLevelMask.FIRST or SpellLevelMask.SECOND or SpellLevelMask.THIRD
                        ),
                        SpellEffectDescriptor(
                                // basic (1-3 level)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, SpellLevelMask.FIRST or SpellLevelMask.SECOND or SpellLevelMask.THIRD
                        ),
                        SpellEffectDescriptor(
                                // advanced (1-4 level)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, SpellLevelMask.FIRST or SpellLevelMask.SECOND or SpellLevelMask.THIRD or SpellLevelMask.FOURTH
                        ),
                        SpellEffectDescriptor(
// expert (all spells)
                                SpellTargetTypeMask.ALLFRIENDLY, SpellTargetMode.CREAT_GROUP, SpellLevelMask.ALL.v
                        )
                )
        ),
        SpellDescriptor(
// Prayer 
                SpellType.COMBAT, SpellLevel.FOURTH, MagicSchoolType.WATER, SpellClass.PRAYER, 18, SpellLabel.PRAYER, 0,
                listOf(
                        SpellEffectDescriptor(
// none (+2)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, 2
                        ),
                        SpellEffectDescriptor(
// basic (+2)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, 2
                        ),
                        SpellEffectDescriptor(
// advanced (+4)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.CREAT_GROUP, 4
                        ),
                        SpellEffectDescriptor(
// expert (+4 + all)
                                SpellTargetTypeMask.FRNUNDEADS, SpellTargetMode.ALL, 4
                        )
                )
        ),
        SpellDescriptor(
// Summon water elemental
                SpellType.COMBAT, SpellLevel.FIFTH, MagicSchoolType.WATER, SpellClass.SUMMON, 25, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
// none (pow*3)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.WATER_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
// basic (pow*3)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.WATER_ELEMENTAL, 3
                        ),
                        SpellEffectDescriptor(
// advanced (pow*4)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.WATER_ELEMENTAL, 4
                        ),
                        SpellEffectDescriptor(
// expert (pow*5)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.WATER_ELEMENTAL, 5
                        )
                )
        ),
        SpellDescriptor(
//////////////////////////////////////////////////////////////////////////
// New spells
//////////////////////////////////////////////////////////////////////////

// Summon Sprites
                SpellType.COMBAT, SpellLevel.SECOND, MagicSchoolType.WATER, SpellClass.SUMMON, 8, SpellLabel.NONE, 0,
                listOf(
                        SpellEffectDescriptor(
// none (pow*5)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.SPRITE, 5
                        ),
                        SpellEffectDescriptor(
// basic (pow*5)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.SPRITE, 5
                        ),
                        SpellEffectDescriptor(
// advanced (pow*7)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.SPRITE, 7
                        ),
                        SpellEffectDescriptor(
// expert (pow*10)
                                SpellTargetTypeMask.NONE, SpellTargetMode.SUMMON, CreatureType.SPRITE, 10
                        )
                )
        )

)
