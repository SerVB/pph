package com.github.servb.pph.gxlib.gxlcommontpl

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.*

@ExperimentalUnsignedTypes
class GxlCommonTplTest : StringSpec() {
    init {
        "iCLAMP" {
            forAll(
                    Table4(
                            Headers4("min", "max", "value", "expected"),
                            listOf(
                                    row(1, 2, 3, 2),
                                    row(1, 1, 1, 1),
                                    row(1, 1, 0, 1),
                                    row(10, 30, 12, 12),
                                    row(10, 30, 16, 16),
                                    row(10, 15, 10, 10),
                                    row(10, 15, 15, 15)
                            )
                    )
            ) { min, max, value, expected ->
                iCLAMP(min, max, value) shouldBe expected
            }
        }

        "iDIF" {
            // TODO: Test all overloads
            forAll(
                    Table3(
                            Headers3("a", "b", "expected"),
                            listOf(
                                    row(2, 3, 1),
                                    row(1, 1, 0),
                                    row(1, 0, 1),
                                    row(30, 12, 18),
                                    row(30, 16, 14),
                                    row(15, 10, 5),
                                    row(15, 15, 0)
                            )
                    )
            ) { a, b, expected ->
                iDIF(a, b) shouldBe expected
            }
        }

        "iALIGN" {
            forAll(
                    Table3(
                            Headers3("a", "b", "expected"),
                            listOf(
                                    row(2u, 3u, 3u),
                                    row(1u, 1u, 1u),
                                    row(1u, 10u, 10u),
                                    row(30u, 12u, 36u),
                                    row(30u, 16u, 32u),
                                    row(15u, 10u, 20u),
                                    row(15u, 15u, 15u)
                            )
                    )
            ) { a, b, expected ->
                iALIGN(a, b) shouldBe expected
            }
        }

        "iWRAP" {
            forAll(
                    Table4(
                            Headers4("val", "minv", "wrap", "expected"),
                            listOf(
                                    row(2, 3, 5, 4),
                                    row(1, 1, 2, 1),
                                    row(1, 10, 34, 25),
                                    row(30, 12, 16, 26),
                                    row(30, 16, 32, 30),
                                    row(15, 10, 12, 13),
                                    row(15, 15, 16, 15)
                            )
                    )
            ) { value, minv, wrap, expected ->
                iWRAP(value.toShort(), minv.toShort(), wrap.toShort()) shouldBe expected.toShort()
            }
        }
    }
}
