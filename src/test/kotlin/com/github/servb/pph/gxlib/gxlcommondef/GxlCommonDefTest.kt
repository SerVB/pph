package com.github.servb.pph.gxlib.gxlcommondef

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class GxlCommonDefTest : StringSpec() {
    init {
        "size of Rand table should be 256" {
            iRandTable.size shouldBe 256
        }

        "Rand table should have unique elements" {
            iRandTable.toSet().size shouldBe iRandTable.size
        }
    }
}
