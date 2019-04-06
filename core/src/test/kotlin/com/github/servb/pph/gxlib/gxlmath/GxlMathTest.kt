package com.github.servb.pph.gxlib.gxlmath

import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

@ExperimentalUnsignedTypes
class GxlMathTest : StringSpec() {
    init {
        "PI shouldn't differ extremely" {
            PI shouldBe (Math.PI.toFloat() plusOrMinus Math.PI.toFloat() * 1e-6f)
        }

        "int_sqrt should give sqrt" {
            for (i in 0u until 1000u) {
                // TODO: Remove toLong
                int_sqrt(i) shouldBe Math.sqrt(i.toLong().toDouble()).toLong().toUInt()
            }
        }
    }
}
