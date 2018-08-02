package com.github.servb.pph.gxlib.gxlmetrics

import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class RectcGenerator : Gen<Rectc> {
    override fun constants() = listOf(Rect().toRectc())
    override fun random() = generateSequence {
        Rect(Gen.int().random().first(), Gen.int().random().first(),
                Gen.nats().random().first(), Gen.nats().random().first()).toRectc()
    }
}

class RectGenerator : Gen<Rect> {
    override fun constants() = listOf(Rect())
    override fun random() = generateSequence {
        Rect(Gen.int().random().first(), Gen.int().random().first(),
                Gen.nats().random().first(), Gen.nats().random().first())
    }
}

class RectcTest : StringSpec() {
    init {
        "x1 x2 y1 y2" {
            assertAll(RectcGenerator()) { r ->
                r.x1 shouldBe r.x
                r.x2 shouldBe r.x + r.w - 1

                r.y1 shouldBe r.y
                r.y2 shouldBe r.y + r.h - 1
            }
        }

        "center" {
            assertAll(RectcGenerator()) { r ->
                r.Center() shouldBe Point(r.x + r.w / 2, r.y + r.h / 2)
            }
        }

        "corners" {
            assertAll(RectcGenerator()) { r ->
                r.TopLeft() shouldBe Point(r.x1, r.y1)
                r.TopRight() shouldBe Point(r.x2, r.y1)

                r.BottomRight() shouldBe Point(r.x2, r.y2)
                r.BottomLeft() shouldBe Point(r.x1, r.y2)
            }
        }

        "point and size" {
            assertAll(RectcGenerator()) { r ->
                r.Point() shouldBe Point(r.x, r.y)
                r.Size() shouldBe Size(r.w, r.h)
            }
        }

        "PtInRect" {
            assertAll(RectcGenerator(), Gen.int(), Gen.int()) { r, x, y ->
                r.PtInRect(x, y) shouldBe (r.x <= x && x < r.x + r.w && r.y <= y && y < r.y + r.h)
                r.PtInRect(Point(x, y)) shouldBe r.PtInRect(x, y)
            }
        }

        "IsEmpty" {
            assertAll(RectcGenerator()) { r ->
                r.IsEmpty() shouldBe (r.w * r.h == 0)
            }
        }

        "+ Int offs" {
            assertAll(RectcGenerator(), Gen.int()) { r, offs ->
                (r + offs) shouldBe Rect(r.x + offs, r.y + offs, r.w + offs, r.h + offs).toRectc()
            }
        }

        "- Int offs" {
            assertAll(RectcGenerator(), Gen.int()) { r, offs ->
                (r - offs) shouldBe Rect(r.x - offs, r.y - offs, r.w - offs, r.h - offs).toRectc()
            }
        }
    }
}

class RectTest : StringSpec() {
    init {
        "constructor Pointc Pointc should give a rect by two edges" {
            assertAll(PointcGenerator(), PointcGenerator()) { a, b ->
                val r = Rect(a, b)

                val (minX, maxX) = listOf(a.x, b.x).sorted()
                val (minY, maxY) = listOf(a.y, b.y).sorted()

                val w = maxX - minX + 1
                val h = maxY - minY + 1

                r.x shouldBe minX
                r.y shouldBe minY

                r.w shouldBe w
                r.h shouldBe h
            }
        }

        "default constructor should give (0, 0, 0, 0) Rect" {
            Rect() shouldBe Rect(0, 0, 0, 0)
        }

        "constructor Point Size should give Rect that doesn't change when initial objects change" {
            assertAll(PointGenerator(), SizeGenerator()) { p, s ->
                val r = Rect(p, s)

                r.x shouldBe p.x
                r.y shouldBe p.y
                r.w shouldBe s.w
                r.h shouldBe s.h

                val initialP = Point(p)
                val initialS = Size(s)

                p.x += 42
                p.y += 42
                s.w += 42
                s.h += 42

                r.x shouldBe initialP.x
                r.y shouldBe initialP.y
                r.w shouldBe initialS.w
                r.h shouldBe initialS.h
            }
        }

        "constructor Rect should give Rect that doesn't change when the initial Rect changes" {
            assertAll(RectGenerator()) { rect ->
                val r = Rect(rect)

                val (x, y, w, h) = r

                x shouldBe rect.x
                y shouldBe rect.y
                w shouldBe rect.w
                h shouldBe rect.h

                rect.x += 42
                rect.y += 42
                rect.w += 42
                rect.h += 42

                r.x shouldBe x
                r.y shouldBe y
                r.w shouldBe w
                r.h shouldBe h
            }
        }

        "reset" {
            val reset = Rect().toRectc()

            assertAll(RectGenerator()) { r ->
                r.Reset()
                r shouldBe reset
            }
        }

        // TODO: Provide remaining tests
    }
}
