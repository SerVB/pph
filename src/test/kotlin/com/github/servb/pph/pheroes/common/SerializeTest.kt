package com.github.servb.pph.pheroes.common

import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.memory.DynamicBuffer
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

@ExperimentalUnsignedTypes
class SerializeTest : StringSpec({
    "point" {
        "DynamicBuffer serialize unserialize" {
            val point = Point(10, 2).toPointc()
            val buff = DynamicBuffer()

            serialize(buff, point)
            buff.seek(0)

            val actualPoint = unserializePoint(buff)
            actualPoint shouldBe point
        }

        // TODO: test stream version
    }
})
