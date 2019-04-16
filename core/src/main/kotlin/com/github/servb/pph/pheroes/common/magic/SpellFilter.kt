package com.github.servb.pph.pheroes.common.magic

@ExperimentalUnsignedTypes
data class SpellFilter(
        val typeMask: UByte,
        val levelMask: UByte,
        val schoolMask: UByte,
        val reserved: UByte = 0u
) {
    constructor(typeMask: SpellTypeMask, levelMask: SpellLevelMask, schoolMask: MagicSchoolMask) : this(
            typeMask = typeMask.v.toUByte(),
            levelMask = levelMask.v.toUByte(),
            schoolMask = schoolMask.v.toUByte()
    )
}

@ExperimentalUnsignedTypes
typealias SpellList = List<UShort>
