package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class CastleSizeMask(override val v: Int) : UniqueValueEnum {
    S(1 shl CastleSize.SMALL.v),
    M(1 shl CastleSize.MEDIUM.v),
    L(1 shl CastleSize.LARGE.v),
    ML(M.v or L.v),
    SML(S.v or M.v or L.v);
}
