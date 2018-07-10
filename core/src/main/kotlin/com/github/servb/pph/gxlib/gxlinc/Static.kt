package com.github.servb.pph.gxlib.gxlinc

import com.github.servb.pph.util.helpertype.UniqueValueEnum

enum class Inc(override val v: Int) : UniqueValueEnum {
    GXLF_DOUBLESIZE(0x01),
    GXLF_LANDSCAPE(0x02),
    GXLF_DEV_LANDSCAPE(0x04),
    GXLF_DEV_VGARES(0x08),
    GXLF_LHANDER(0x10),
    GXLF_ENABLESOUND(0x20),
    GXLF_REALVGA(0x40);
}
