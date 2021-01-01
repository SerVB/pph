@file:OptIn(ExperimentalUnsignedTypes::class)

package com.github.servb.pph.gxlib

import com.soywiz.korim.color.RGBA
import com.soywiz.korim.color.RGB_565
import com.soywiz.korim.color.convertTo
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DibTest : StringSpec() {

    init {
        "test colors from KorGE match the gxl ones".config(enabled = false) {
            for (r in 0..255) {
                for (g in 0..255) {
                    for (b in 0..255) {
                        val gxlColor = RGB16(r, g, b)
                        val korgeColor = RGBA.convertTo(RGBA(r, g, b).value, RGB_565).toUShort()

                        withClue("RGB16($r, $g, $b)") {
                            korgeColor shouldBe gxlColor
                        }
                    }
                }
            }
        }

        "test constants size" {
            BWPAL.size shouldBe 32
        }
    }
}
