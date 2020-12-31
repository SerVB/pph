package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.pheroes.common.common.MineralSetC

const val CTL_INCOME: Int = 500

@ExperimentalUnsignedTypes
data class CTCNSTCAP(val siz: CastleSizeMask, val type: Int) {

    constructor(siz: CastleSizeMask, type: CastleTypeMask) : this(siz, type.v)

    fun Support(_type: CastleType, _siz: CastleSize) =
            (type and (1 shl _type.v)) != 0 &&
                    (siz.v and (1 shl _siz.v)) != 0
}

@ExperimentalUnsignedTypes
data class CTLCNST_DESC_STRUCT(
        val name: String,
        val type: CastleConstructionType,
        val price: MineralSetC,
        val depend: Set<CastleConstruction>,
        val caps: CTCNSTCAP,
        val fparam: Int,
        val sparam: Int
)
