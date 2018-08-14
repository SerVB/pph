package com.github.servb.pph.pheroes.common.common.artifact

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class HeroArtifactCell(
        override val v: Int,
        val assign: ArtifactAssign? = null
) : UniqueValueEnum, CountValueEnum {
    UNDEFINED(-1),
    HEAD(0, ArtifactAssign.HEAD),
    NECK(1, ArtifactAssign.NECK),
    TORSO(2, ArtifactAssign.TORSO),
    LEFT_HAND(3, ArtifactAssign.HANDS),
    RIGHT_HAND(4, ArtifactAssign.HANDS),
    LEFT_FINGERS(5, ArtifactAssign.FINGERS),
    RIGHT_FINGERS(6, ArtifactAssign.FINGERS),
    SHOULDERS(7, ArtifactAssign.SHOULDERS),
    LEGS(8, ArtifactAssign.LEGS),
    FEET(9, ArtifactAssign.FEET),
    MISC1(10, ArtifactAssign.MISC),
    MISC2(11, ArtifactAssign.MISC),
    MISC3(12, ArtifactAssign.MISC),
    MISC4(13, ArtifactAssign.MISC),
    COUNT(14);
}
