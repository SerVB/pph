package com.github.servb.pph.gxlib.gxlmetrics

import io.kotlintest.matchers.exactly
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SizecGenerator : Gen<Sizec> {
    override fun constants() = listOf(Size().toSizec())
    override fun random() = generateSequence {
        Size(Gen.nats().random().first(), Gen.nats().random().first()).toSizec()
    }
}

class SizeGenerator : Gen<Size> {
    override fun constants() = listOf(Size())
    override fun random() = generateSequence {
        Size(Gen.nats().random().first(), Gen.nats().random().first())
    }
}

class SizecTest : StringSpec() {
    init {
        ".IsZero()" {
            assertAll(SizecGenerator()) { s ->
                s.IsZero() shouldBe (s.w == 0 && s.h == 0)
            }
        }

        ".GetAspectRatio()" {
            assertAll(SizecGenerator()) { s ->
                if (!s.IsZero()) {
                    s.GetAspectRatio() shouldBe exactly(s.w.toFloat() / s.h.toFloat())
                }
            }
        }

        "+ Sizec" {
            assertAll(SizecGenerator(), SizeGenerator()) { s, other ->
                (s + other) shouldBe Size(s.w + other.w, s.h + other.h).toSizec()
            }
        }

        "- Sizec" {
            assertAll(SizecGenerator(), SizeGenerator()) { s, other ->
                (s - other) shouldBe Size(s.w - other.w, s.h - other.h).toSizec()
            }
        }

        "+ Int" {
            assertAll(SizecGenerator(), Gen.int()) { s, offs ->
                (s + offs) shouldBe Size(s.w + offs, s.h + offs)
            }
        }

        "- Int" {
            assertAll(SizecGenerator(), Gen.int()) { s, offs ->
                (s - offs) shouldBe Size(s.w - offs, s.h - offs)
            }
        }
    }
}

class SizeTest : StringSpec() {
    init {
        "default constructor should give (0, 0) size" {
            Size() shouldBe Size(0, 0)
        }

        "copy constructor (Sizec)" {
            assertAll(SizecGenerator()) { other ->
                Size(other) shouldBe Size(other.w, other.h)
            }
        }

        "+ Sizec" {
            assertAll(SizeGenerator(), SizecGenerator()) { s, other ->
                (s + other) shouldBe Size(s.w + other.w, s.h + other.h)
            }
        }

        "- Sizec" {
            assertAll(SizeGenerator(), SizecGenerator()) { s, other ->
                (s - other) shouldBe Size(s.w - other.w, s.h - other.h)
            }
        }

        "+ Int" {
            assertAll(SizeGenerator(), Gen.int()) { s, offs ->
                (s + offs) shouldBe Size(s.w + offs, s.h + offs)
            }
        }

        "- Int" {
            assertAll(SizeGenerator(), Gen.int()) { s, offs ->
                (s - offs) shouldBe Size(s.w - offs, s.h - offs)
            }
        }

        ".InflateSize Int offset" {
            assertAll(SizeGenerator(), Gen.int()) { s, offs ->
                val initial = s.copy()
                s.InflateSize(offs)
                s shouldBe Size(initial.w + offs, initial.h + offs)
            }

            assertAll(SizeGenerator(), Gen.int()) { s, offs ->
                val initial = s.copy()
                s.InflateSize(offs)
                s shouldBe Size(initial.w + offs, initial.h + offs)
            }
        }

        ".InflateSize Int Int offsets" {
            assertAll(SizeGenerator(), Gen.int(), Gen.int()) { s, widthOffset, heightOffset ->
                val initial = s.copy()
                s.InflateSize(widthOffset, heightOffset)
                s shouldBe Size(initial.w + widthOffset, initial.h + heightOffset)
            }
        }

        ".DeflateSize Int offset" {
            assertAll(SizeGenerator(), Gen.int()) { s, offs ->
                val initial = s.copy()
                s.DeflateSize(offs)
                s shouldBe Size(initial.w - offs, initial.h - offs)
            }

            assertAll(SizeGenerator(), Gen.int()) { s, offs ->
                val initial = s.copy()
                s.DeflateSize(offs)
                s shouldBe Size(initial.w - offs, initial.h - offs)
            }
        }

        ".DeflateSize Int Int offsets" {
            assertAll(SizeGenerator(), Gen.int(), Gen.int()) { s, widthOffset, heightOffset ->
                val initial = s.copy()
                s.DeflateSize(widthOffset, heightOffset)
                s shouldBe Size(initial.w - widthOffset, initial.h - heightOffset)
            }
        }

        ".Zero() should reset metrics" {
            val zero = Size(0, 0).toSizec()

            assertAll(SizeGenerator()) { s ->
                s.Zero()
                s shouldBe zero
            }
        }
    }
}
