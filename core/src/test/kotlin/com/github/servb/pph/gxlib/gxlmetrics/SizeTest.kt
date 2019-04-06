package com.github.servb.pph.gxlib.gxlmetrics

import io.kotlintest.matchers.exactly
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

@ExperimentalUnsignedTypes
class SizecGenerator : Gen<Sizec> {
    override fun constants() = listOf(Size().toSizec())
    override fun random() = generateSequence {
        Size(Gen.nats().random().first().toUInt(), Gen.nats().random().first().toUInt()).toSizec()
    }
}

@ExperimentalUnsignedTypes
class SizeGenerator : Gen<Size> {
    override fun constants() = listOf(Size())
    override fun random() = generateSequence {
        Size(Gen.nats().random().first().toUInt(), Gen.nats().random().first().toUInt())
    }
}

@ExperimentalUnsignedTypes
class SizecTest : StringSpec() {
    init {
        ".IsZero()" {
            assertAll(SizecGenerator()) { s ->
                s.IsZero() shouldBe (s.w == 0u && s.h == 0u)
            }
        }

        ".GetAspectRatio()" {
            assertAll(SizecGenerator()) { s ->
                if (!s.IsZero()) {
                    s.GetAspectRatio() shouldBe exactly(s.w.toLong().toFloat() / s.h.toLong().toFloat())
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
            assertAll(SizecGenerator(), Gen.nats()) { s, offs ->
                (s + offs.toUInt()) shouldBe Size(s.w + offs.toUInt(), s.h + offs.toUInt())
            }
        }

        "- Int" {
            assertAll(SizecGenerator(), Gen.nats()) { s, offs ->
                (s - offs.toUInt()) shouldBe Size(s.w - offs.toUInt(), s.h - offs.toUInt())
            }
        }
    }
}

@ExperimentalUnsignedTypes
class SizeTest : StringSpec() {
    init {
        "default constructor should give (0, 0) size" {
            Size() shouldBe Size(0u, 0u)
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
            assertAll(SizeGenerator(), Gen.nats()) { s, offs ->
                (s + offs.toUInt()) shouldBe Size(s.w + offs.toUInt(), s.h + offs.toUInt())
            }
        }

        "- Int" {
            assertAll(SizeGenerator(), Gen.nats()) { s, offs ->
                (s - offs.toUInt()) shouldBe Size(s.w - offs.toUInt(), s.h - offs.toUInt())
            }
        }

        ".InflateSize Int offset" {
            assertAll(SizeGenerator(), Gen.nats()) { s, offs ->
                val initial = s.copy()
                s.InflateSize(offs.toUInt())
                s shouldBe Size(initial.w + offs.toUInt(), initial.h + offs.toUInt())
            }

            assertAll(SizeGenerator(), Gen.nats()) { s, offs ->
                val initial = s.copy()
                s.InflateSize(offs.toUInt())
                s shouldBe Size(initial.w + offs.toUInt(), initial.h + offs.toUInt())
            }
        }

        ".InflateSize Int Int offsets" {
            assertAll(SizeGenerator(), Gen.nats(), Gen.nats()) { s, widthOffset, heightOffset ->
                val initial = s.copy()
                s.InflateSize(widthOffset.toUInt(), heightOffset.toUInt())
                s shouldBe Size(initial.w + widthOffset.toUInt(), initial.h + heightOffset.toUInt())
            }
        }

        ".DeflateSize Int offset" {
            assertAll(SizeGenerator(), Gen.nats()) { s, offs ->
                val initial = s.copy()
                s.DeflateSize(offs.toUInt())
                s shouldBe Size(initial.w - offs.toUInt(), initial.h - offs.toUInt())
            }

            assertAll(SizeGenerator(), Gen.nats()) { s, offs ->
                val initial = s.copy()
                s.DeflateSize(offs.toUInt())
                s shouldBe Size(initial.w - offs.toUInt(), initial.h - offs.toUInt())
            }
        }

        ".DeflateSize Int Int offsets" {
            assertAll(SizeGenerator(), Gen.nats(), Gen.nats()) { s, widthOffset, heightOffset ->
                val initial = s.copy()
                s.DeflateSize(widthOffset.toUInt(), heightOffset.toUInt())
                s shouldBe Size(initial.w - widthOffset.toUInt(), initial.h - heightOffset.toUInt())
            }
        }

        ".Zero() should reset metrics" {
            val zero = Size(0u, 0u).toSizec()

            assertAll(SizeGenerator()) { s ->
                s.Zero()
                s shouldBe zero
            }
        }
    }
}
