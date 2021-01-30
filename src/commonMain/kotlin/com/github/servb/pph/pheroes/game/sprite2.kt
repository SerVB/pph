package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.util.SizeT

interface BlitterOperation {

    operator fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer)
}

object SetOp : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) {
        dst[0] = 0xFFFFu
    }
}

object CopyOp : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) {
        dst[0] = src[0]
    }
}

class ConstAlphaBlendOp(val a: UByte) : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) = TODO()
}

object Shadow50Op : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) {
        dst[0] = Darken50(dst[0].toUInt()).toUShort()
    }
}

object Shadow25Op : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) {
        dst[0] = Darken25(dst[0].toUInt()).toUShort()
    }
}

object DotedOp : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) {
        if ((dst.offset and 3) == 0) {
            dst[0] = Darken50(dst[0].toUInt()).toUShort()
        } /*else *dst = 0;*/  // commented in sources
    }
}

private val tintGradient = listOf(
    0x4101u, 0x4101u, 0x4921u, 0x4942u, 0x5142u, 0x5162u, 0x5983u, 0x61a3u,
    0x61e3u, 0x6a04u, 0x7224u, 0x7a65u, 0x8285u, 0x8ac6u, 0x8ae6u, 0x9327u,
    0xa368u, 0xa389u, 0xabc9u, 0xb40au, 0xbc4bu, 0xc48cu, 0xcccdu, 0xd4edu,
    0xdd2eu, 0xe54fu, 0xed90u, 0xedd0u, 0xf5f1u, 0xf611u, 0xf632u, 0xf652u,
)

object TintShadow : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) {
        val chnl = ((dst[0].toUInt() shr 6) and 0x1Fu).toInt()
        //*dst = Darken25((chnl) | (chnl << 6) | (chnl << 11));  // commented in sources
        dst[0] = DarkenBWBlend(tintGradient[chnl], 0x39E7u).toUShort()
    }
}

private fun BLTLOOP(src: IDibIPixelPointer, dst: IDibPixelPointer, cnt: SizeT, op: BlitterOperation) {
    repeat(cnt) {
        op(dst, src)
        src.incrementOffset(1)
        dst.incrementOffset(1)
    }
}

// Skips one span
fun SpanSkip(src: IDibIPixelIPointer): Int = TODO()

// Blits non clipped span line
fun SpanFast(op: BlitterOperation, src: IDibIPixelIPointer, dst: IDibPixelIPointer): Int = TODO()

// Blits clipped span line
fun Span(
    op: BlitterOperation,
    src: IDibIPixelIPointer,
    dst: IDibPixelIPointer,
    clipIn: IDibIPixelIPointer,
    clipOut: IDibIPixelIPointer
): Int = TODO()
