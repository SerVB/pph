package com.github.servb.pph.gxlib.gxlmetrics

import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class PointGenerator : Gen<Point> {
    override fun constants() = emptyList<Point>()
    override fun random() = generateSequence {
        Point(Gen.int().random().first(), Gen.int().random().first())
    }
}

class MutablePointGenerator : Gen<MutablePoint> {
    override fun constants() = emptyList<MutablePoint>()
    override fun random() = generateSequence {
        MutablePoint(Gen.int().random().first(), Gen.int().random().first())
    }
}

class PointTest : StringSpec() {
    init {
        "default constructor should give (0, 0) point" {
            Point() shouldBe Point(0, 0)
        }

        "copy constructor (XYHolder)" {
            assertAll(MutablePointGenerator()) { other ->
                Point(other) shouldBe Point(other.x, other.y)
            }
        }

        "+ XYHolder" {
            assertAll(PointGenerator(), MutablePointGenerator()) { p, other ->
                (p + other) shouldBe Point(p.x + other.x, p.y + other.y)
            }
        }

        "- XYHolder" {
            assertAll(PointGenerator(), MutablePointGenerator()) { p, other ->
                (p - other) shouldBe Point(p.x - other.x, p.y - other.y)
            }
        }

        "+ Int offset" {
            assertAll(PointGenerator(), Gen.int()) { p, offs ->
                (p + offs) shouldBe Point(p.x + offs, p.y + offs)
            }
        }

        "- Int offset" {
            assertAll(PointGenerator(), Gen.int()) { p, offs ->
                (p - offs) shouldBe Point(p.x - offs, p.y - offs)
            }
        }
    }
}

class MutablePointTest : StringSpec() {
    init {
        "default constructor should give (0, 0) point" {
            MutablePoint() shouldBe MutablePoint(0, 0)
        }

        "copy constructor (XYHolder)" {
            assertAll(PointGenerator()) { other ->
                MutablePoint(other) shouldBe MutablePoint(other.x, other.y)
            }
        }

        "+ XYHolder" {
            assertAll(MutablePointGenerator(), PointGenerator()) { p, other ->
                (p + other) shouldBe MutablePoint(p.x + other.x, p.y + other.y)
            }
        }

        "- XYHolder" {
            assertAll(MutablePointGenerator(), PointGenerator()) { p, other ->
                (p - other) shouldBe MutablePoint(p.x - other.x, p.y - other.y)
            }
        }

        "+ Int offset" {
            assertAll(MutablePointGenerator(), Gen.int()) { p, offs ->
                (p + offs) shouldBe MutablePoint(p.x + offs, p.y + offs)
            }
        }

        "- Int offset" {
            assertAll(MutablePointGenerator(), Gen.int()) { p, offs ->
                (p - offs) shouldBe MutablePoint(p.x - offs, p.y - offs)
            }
        }

        "+= XYHolder" {
            assertAll(MutablePointGenerator(), PointGenerator()) { p, other ->
                val initial = p.copy()
                p += other
                p shouldBe MutablePoint(initial.x + other.x, initial.y + other.y)
            }
        }

        "-= XYHolder" {
            assertAll(MutablePointGenerator(), PointGenerator()) { p, other ->
                val initial = p.copy()
                p -= other
                p shouldBe MutablePoint(initial.x - other.x, initial.y - other.y)
            }
        }

        "+= Int offset" {
            assertAll(MutablePointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p += offs
                p shouldBe MutablePoint(initial.x + offs, initial.y + offs)
            }
        }

        "-= Int offset" {
            assertAll(MutablePointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p -= offs
                p shouldBe MutablePoint(initial.x - offs, initial.y - offs)
            }
        }

        ".MoveX" {
            assertAll(MutablePointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p.MoveX(offs)
                p shouldBe MutablePoint(initial.x + offs, initial.y)
            }
        }

        ".MoveY" {
            assertAll(MutablePointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p.MoveY(offs)
                p shouldBe MutablePoint(initial.x, initial.y + offs)
            }
        }

        ".Move" {
            assertAll(MutablePointGenerator(), Gen.int(), Gen.int()) { p, dx, dy ->
                val initial = p.copy()
                p.Move(dx, dy)
                p shouldBe MutablePoint(initial.x + dx, initial.y + dy)
            }
        }
    }
}
