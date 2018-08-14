package com.github.servb.pph.pheroes.common.common.artifact

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class RandomArtifact(override val v: Int) : UniqueValueEnum, CountValueEnum {
    RAND(0xFF00),
    RAND_L1(0xFF01),
    RAND_L2(0xFF02),
    RAND_L3(0xFF03),
    RAND_L4(0xFF04);
}
