package com.github.servb.pph.gxlib.gxlmetrics

import com.github.servb.pph.util.UintGenerator
import io.kotlintest.matchers.exactly
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import unsigned.ui

class SizeGenerator : Gen<Size> {
    override fun constants() = listOf(Size())
    override fun random() = generateSequence {
        Size(Gen.int().random().first().ui, Gen.int().random().first().ui)
    }
}

class MutableSizeGenerator : Gen<MutableSize> {
    override fun constants() = listOf(MutableSize())
    override fun random() = generateSequence {
        MutableSize(Gen.int().random().first().ui, Gen.int().random().first().ui)
    }
}

class WHHolderTest : StringSpec() {
    init {
        ".IsZero()" {
            assertAll(SizeGenerator()) { s ->
                s.IsZero() shouldBe (s.w.v == 0 && s.h.v == 0)
            }
        }

        ".GetAspectRatio()" {
            assertAll(SizeGenerator()) { s ->
                if (!s.IsZero()) {
                    s.GetAspectRatio() shouldBe exactly(s.w.toFloat() / s.h.toFloat())
                }
            }
        }
    }
}

class SizeTest : StringSpec() {
    init {
        "default constructor should give (0, 0) size" {
            Size() shouldBe Size(0.ui, 0.ui)
        }

        "copy constructor (WHHolder)" {
            assertAll(SizeGenerator()) { other ->
                Size(other) shouldBe Size(other.w, other.h)
            }
        }

        "+ WHHolder" {
            assertAll(SizeGenerator(), MutableSizeGenerator()) { s, other ->
                (s + other) shouldBe Size(s.w + other.w, s.h + other.h)
            }
        }

        "- WHHolder" {
            assertAll(SizeGenerator(), MutableSizeGenerator()) { s, other ->
                (s - other) shouldBe Size(s.w - other.w, s.h - other.h)
            }
        }

        "+ Uint" {
            assertAll(SizeGenerator(), UintGenerator()) { s, offs ->
                (s + offs) shouldBe Size(s.w + offs, s.h + offs)
            }
        }

        "- Uint" {
            assertAll(SizeGenerator(), UintGenerator()) { s, offs ->
                (s - offs) shouldBe Size(s.w - offs, s.h - offs)
            }
        }
    }
}

class MutableSizeTest : StringSpec() {
    init {
        "default constructor should give (0, 0) size" {
            MutableSize() shouldBe MutableSize(0.ui, 0.ui)
        }

        "copy constructor (WHHolder)" {
            assertAll(SizeGenerator()) { other ->
                MutableSize(other) shouldBe MutableSize(other.w, other.h)
            }
        }

        "+ WHHolder" {
            assertAll(MutableSizeGenerator(), SizeGenerator()) { s, other ->
                (s + other) shouldBe MutableSize(s.w + other.w, s.h + other.h)
            }
        }

        "- WHHolder" {
            assertAll(MutableSizeGenerator(), SizeGenerator()) { s, other ->
                (s - other) shouldBe MutableSize(s.w - other.w, s.h - other.h)
            }
        }

        "+ Uint" {
            assertAll(MutableSizeGenerator(), UintGenerator()) { s, offs ->
                (s + offs) shouldBe MutableSize(s.w + offs, s.h + offs)
            }
        }

        "- Uint" {
            assertAll(MutableSizeGenerator(), UintGenerator()) { s, offs ->
                (s - offs) shouldBe MutableSize(s.w - offs, s.h - offs)
            }
        }

        ".InflateSize Uint offset" {
            assertAll(MutableSizeGenerator(), UintGenerator()) { s, offs ->
                val initial = s.copy()
                s += offs
                s shouldBe MutableSize(initial.w + offs, initial.h + offs)
            }

            assertAll(MutableSizeGenerator(), UintGenerator()) { s, offs ->
                val initial = s.copy()
                s.InflateSize(offs)
                s shouldBe MutableSize(initial.w + offs, initial.h + offs)
            }
        }

        ".InflateSize Uint Uint offsets" {
            assertAll(MutableSizeGenerator(), UintGenerator(), UintGenerator()) { s, widthOffset, heightOffset ->
                val initial = s.copy()
                s.InflateSize(widthOffset, heightOffset)
                s shouldBe MutableSize(initial.w + widthOffset, initial.h + heightOffset)
            }
        }

        ".DeflateSize Uint offset" {
            assertAll(MutableSizeGenerator(), UintGenerator()) { s, offs ->
                val initial = s.copy()
                s -= offs
                s shouldBe MutableSize(initial.w - offs, initial.h - offs)
            }

            assertAll(MutableSizeGenerator(), UintGenerator()) { s, offs ->
                val initial = s.copy()
                s.DeflateSize(offs)
                s shouldBe MutableSize(initial.w - offs, initial.h - offs)
            }
        }

        ".DeflateSize Uint Uint offsets" {
            assertAll(MutableSizeGenerator(), UintGenerator(), UintGenerator()) { s, widthOffset, heightOffset ->
                val initial = s.copy()
                s.DeflateSize(widthOffset, heightOffset)
                s shouldBe MutableSize(initial.w - widthOffset, initial.h - heightOffset)
            }
        }

        ".Zero() should reset metrics" {
            assertAll(MutableSizeGenerator()) { s ->
                s.Zero()
                s shouldBe MutableSize(0.ui, 0.ui)
            }
        }
    }
}
