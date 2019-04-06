package com.github.servb.pph.gxlib.gxlmetrics

import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

@ExperimentalUnsignedTypes
class PointcGenerator : Gen<Pointc> {
    override fun constants() = listOf(Point().toPointc())
    override fun random() = generateSequence {
        Point(Gen.int().random().first(), Gen.int().random().first()).toPointc()
    }
}

@ExperimentalUnsignedTypes
class PointGenerator : Gen<Point> {
    override fun constants() = listOf(Point())
    override fun random() = generateSequence {
        Point(Gen.int().random().first(), Gen.int().random().first())
    }
}

@ExperimentalUnsignedTypes
class PointcTest : StringSpec() {
    init {
        "+ Pointc" {
            assertAll(PointcGenerator(), PointGenerator()) { p, other ->
                (p + other) shouldBe Point(p.x + other.x, p.y + other.y).toPointc()
            }
        }

        "- Pointc" {
            assertAll(PointcGenerator(), PointGenerator()) { p, other ->
                (p - other) shouldBe Point(p.x - other.x, p.y - other.y).toPointc()
            }
        }

        "+ Int offset" {
            assertAll(PointcGenerator(), Gen.int()) { p, offs ->
                (p + offs) shouldBe Point(p.x + offs, p.y + offs).toPointc()
            }
        }

        "- Int offset" {
            assertAll(PointcGenerator(), Gen.int()) { p, offs ->
                (p - offs) shouldBe Point(p.x - offs, p.y - offs).toPointc()
            }
        }
    }
}

@ExperimentalUnsignedTypes
class PointTest : StringSpec() {
    init {
        "default constructor should give (0, 0) point" {
            Point() shouldBe Point(0, 0)
        }

        "copy constructor (Pointc)" {
            assertAll(PointcGenerator()) { other ->
                Point(other) shouldBe Point(other.x, other.y)
            }
        }

        "+ Pointc" {
            assertAll(PointGenerator(), PointcGenerator()) { p, other ->
                (p + other) shouldBe Point(p.x + other.x, p.y + other.y)
            }
        }

        "- Pointc" {
            assertAll(PointGenerator(), PointcGenerator()) { p, other ->
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

        "+= Pointc" {
            assertAll(PointGenerator(), PointcGenerator()) { p, other ->
                val initial = p.copy()
                p += other
                p shouldBe Point(initial.x + other.x, initial.y + other.y)
            }
        }

        "-= Pointc" {
            assertAll(PointGenerator(), PointcGenerator()) { p, other ->
                val initial = p.copy()
                p -= other
                p shouldBe Point(initial.x - other.x, initial.y - other.y)
            }
        }

        "+= Sizec" {
            assertAll(PointGenerator(), SizecGenerator()) { p, other ->
                val initial = p.copy()
                p += other
                p shouldBe Point(initial.x + other.w.toInt(), initial.y + other.h.toInt())
            }
        }

        "-= Sizec" {
            assertAll(PointGenerator(), SizecGenerator()) { p, other ->
                val initial = p.copy()
                p -= other
                p shouldBe Point(initial.x - other.w.toInt(), initial.y - other.h.toInt())
            }
        }

        "+= Int offset" {
            assertAll(PointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p += offs
                p shouldBe Point(initial.x + offs, initial.y + offs)
            }
        }

        "-= Int offset" {
            assertAll(PointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p -= offs
                p shouldBe Point(initial.x - offs, initial.y - offs)
            }
        }

        ".MoveX" {
            assertAll(PointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p.MoveX(offs)
                p shouldBe Point(initial.x + offs, initial.y)
            }
        }

        ".MoveY" {
            assertAll(PointGenerator(), Gen.int()) { p, offs ->
                val initial = p.copy()
                p.MoveY(offs)
                p shouldBe Point(initial.x, initial.y + offs)
            }
        }

        ".Move" {
            assertAll(PointGenerator(), Gen.int(), Gen.int()) { p, dx, dy ->
                val initial = p.copy()
                p.Move(dx, dy)
                p shouldBe Point(initial.x + dx, initial.y + dy)
            }
        }
    }
}
