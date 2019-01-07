package com.github.servb.pph.pheroes.common.castle

import com.github.servb.pph.util.helpertype.UniqueValueEnum

@ExperimentalUnsignedTypes
enum class CastleTypeMask(override val v: Int) : UniqueValueEnum {
    CITADEL(1 shl CastleType.CITADEL.v),
    STRONGHOLD(1 shl CastleType.STRONGHOLD.v),
    TOWER(1 shl CastleType.TOWER.v),
    DUNGEON(1 shl CastleType.DUNGEON.v),
    FORTRESS(1 shl CastleType.FORTRESS.v),
    NECROPOLIS(1 shl CastleType.NECROPOLIS.v),
    MIGHT(CITADEL.v or STRONGHOLD.v),
    MAGIC(TOWER.v or DUNGEON.v),
    MISC(FORTRESS.v or NECROPOLIS.v),
    ALL(MIGHT.v or MAGIC.v or MISC.v);
}
