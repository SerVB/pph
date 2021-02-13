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

// Non clipped span blit
fun SegmentFast(op: BlitterOperation, src: IDibIPixelIPointer, dst: IDibPixelIPointer, count: SizeT) {
    BLTLOOP(src.copy(), dst.copy(), count, op)
}

// Clipped span blit
fun Segment(
    op: BlitterOperation,
    src: IDibIPixelIPointer,
    dst: IDibPixelIPointer,
    count: SizeT,
    clipIn: IDibIPixelIPointer,
    clipOut: IDibIPixelIPointer
) {
    val ptrSrc = src.copy()
    val ptrDst = dst.copy()

    // validate clip params
    check(clipIn.offset < clipOut.offset)
    val clipBoundDiff = clipIn.offset - ptrSrc.offset
    val leftClipSkips: SizeT = minOf(maxOf(clipBoundDiff, 0), count)
    check(leftClipSkips <= count)

    ptrSrc.incrementOffset(leftClipSkips)
    ptrDst.incrementOffset(leftClipSkips)

    var count = count - leftClipSkips

    if (0 != count && ptrDst.offset < clipOut.offset) {
        check(ptrDst.offset <= clipOut.offset)
        val copyCount: SizeT = minOf(clipOut.offset - ptrDst.offset, count)
        check(copyCount <= count)
        count -= copyCount
        BLTLOOP(ptrSrc, ptrDst, copyCount, op)
    }
}

// Skips one span
fun SpanSkip(src: IDibIPixelIPointer): Int {
    val newSrc = src.copy()
    var code: UInt
    do {
        code = newSrc[0].toUInt()
        val len = code and 0x00FFu
        newSrc.incrementOffset(len.toInt() + 1)
    } while ((code and 0x8000u) == 0u)
    return newSrc.offset - src.offset
}

// Blits non clipped span line
fun SpanFast(op: BlitterOperation, src: IDibIPixelIPointer, dst: IDibPixelIPointer): Int {
    val ptrSrc = src.copy()
    val ptrDst = dst.copy()
    var code: UInt
    do {
        code = ptrSrc[0].toUInt()
        ptrSrc.incrementOffset(1)
        if (code == 0x8000u) {
            break
        }

        val len: SizeT = (code and 0x00FFu).toInt()
        val offset: SizeT = ((code shr 8) and 0x007Fu).toInt()
        ptrDst.incrementOffset(offset)
        SegmentFast(op, ptrSrc, ptrDst, len)
        ptrSrc.incrementOffset(len)
        ptrDst.incrementOffset(len)
    } while ((code and 0x8000u) == 0u)

    return ptrSrc.offset - src.offset
}

// Blits clipped span line
fun Span(
    op: BlitterOperation,
    src: IDibIPixelIPointer,
    dst: IDibPixelIPointer,
    clipIn: IDibIPixelIPointer,
    clipOut: IDibIPixelIPointer
): Int {
    val ptrSrc = src.copy()
    val ptrDst = dst.copy()
    var code: UInt
    do {
        code = ptrSrc[0].toUInt()
        ptrSrc.incrementOffset(1)
        if (code == 0x8000u) {
            break
        }

        val len: SizeT = (code and 0x00FFu).toInt()
        val offset: SizeT = ((code shr 8) and 0x007Fu).toInt()
        ptrDst.incrementOffset(offset)
        Segment(op, ptrSrc, ptrDst, len, clipIn, clipOut)
        ptrSrc.incrementOffset(len)
        ptrDst.incrementOffset(len)
    } while ((code and 0x8000u) == 0u)

    return ptrSrc.offset - src.offset
}
