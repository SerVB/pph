package com.github.servb.pph.pheroes.Game

import com.soywiz.klock.DateTime
import com.soywiz.klock.Month
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DateTest : StringSpec() {

    init {
        "test timestamp" {
            val expected = DateTime(2021, Month.September, 13)
            val actual = expected.toTimestamp().timestampToDateTime()

            actual shouldBe expected
        }

        "test FormatDate" {
            gTextMgr.Init()
            val expected = "20 Feb 2019 (1:02)"
            val actual = FormatDate(DateTime(2019, Month.February, 20, 1, 2).toTimestamp(), bShowTime = true)

            actual shouldBe expected
        }
    }
}