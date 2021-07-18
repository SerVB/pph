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

class ConstAlphaBlendOp(val alpha: UByte) : BlitterOperation {

    override fun invoke(dst: IDibPixelIPointer, src: IDibIPixelIPointer) {
        /* commented in sources

                if (!alpha) return;
                uint8 inv_a = 63-alpha;
                uint16 sr = alpha * ((*src & 0xF800) >> 11);
                uint16 sg = alpha * ((*src & 0x7E0) >> 5);
                uint16 sb = alpha * ((*src & 0x1F));
                uint16 dr = inv_a * ((*dst & 0xF800) >> 11);
                uint16 dg = inv_a * ((*dst & 0x7E0) >> 5);
                uint16 db = inv_a * ((*dst & 0x1F));
                *dst = (((sr+dr)>>6)<<11 | ((sg+dg)>>6)<<5 | ((sb+db)>>6));
        */
        val a = src[0].toUInt()
        val b = dst[0].toUInt()
        val sb = a and 0x1Fu
        val sg = (a shr 5) and 0x3Fu
        val sr = (a shr 11) and 0x1Fu
        val db = b and 0x1Fu
        val dg = (b shr 5) and 0x3Fu
        val dr = (b shr 11) and 0x1Fu

        dst[0] = (
                (((alpha * (sb - db)) shr 6) + db) or
                        ((((alpha * (sg - dg)) shr 6) + dg) shl 5) or
                        ((((alpha * (sr - dr)) shr 6) + dr) shl 11)
                ).toUShort()
    }
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

// Clipped masked span blit
fun Segment(
    op: BlitterOperation,
    src: IDibIPixelIPointer,
    dst: IDibPixelIPointer,
    mask: IDibIPixelIPointer,
    count: SizeT,
    clipIn: IDibIPixelIPointer,
    clipOut: IDibIPixelIPointer
) {
    val ptrSrc = src.copy()
    val ptrDst = dst.copy()
    val ptrMask = mask.copy()

    // validate clip params
    check(clipIn.offset < clipOut.offset)
    val clipBoundDiff = clipIn.offset - ptrSrc.offset
    val leftClipSkips: SizeT = minOf(maxOf(clipBoundDiff, 0), count)
    check(leftClipSkips <= count)

    ptrSrc.incrementOffset(leftClipSkips)
    ptrDst.incrementOffset(leftClipSkips)
    ptrMask.incrementOffset(leftClipSkips)

    var count = count - leftClipSkips

    if (0 != count && ptrDst.offset < clipOut.offset) {
        check(ptrDst.offset <= clipOut.offset)
        val copyCount: SizeT = minOf(clipOut.offset - ptrDst.offset, count)
        check(copyCount <= count)
        count -= copyCount
        ptrMask.incrementOffset(copyCount)
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

// Blits non clipped masked span line
// ( NOTE: mask is a span sprite! ) returns ptr to the mask
fun MaskedSpanFast(
    op: BlitterOperation,
    mask: IDibIPixelIPointer,
    src: IDibIPixelIPointer,
    dst: IDibPixelIPointer,
): Int {
    val ptrDst = dst.copy()
    val ptrSrc = src.copy()
    val ptrMask = mask.copy()
    var code: UInt
    do {
        code = ptrMask[0].toUInt()
        ptrMask.incrementOffset(1)
        if (code == 0x8000u) {
            break
        }

        val len: SizeT = (code and 0x00FFu).toInt()
        val offset: SizeT = ((code shr 8) and 0x007Fu).toInt()
        ptrDst.incrementOffset(offset)
        ptrSrc.incrementOffset(offset)
        SegmentFast(op, ptrSrc, ptrDst, len)
        ptrMask.incrementOffset(len)
        ptrDst.incrementOffset(len)
        ptrSrc.incrementOffset(len)
    } while ((code and 0x8000u) == 0u)

    return ptrMask.offset - mask.offset
}

// Blits clipped masked span line
// ( NOTE: mask is a span sprite! ) returns ptr to the mask
fun MaskedSpan(
    op: BlitterOperation,
    mask: IDibIPixelIPointer,
    src: IDibIPixelIPointer,
    dst: IDibPixelIPointer,
    clipIn: IDibIPixelIPointer,
    clipOut: IDibIPixelIPointer,
): Int {

    val ptrDst = dst.copy()
    val ptrSrc = src.copy()
    val ptrMask = mask.copy()
    var code: UInt
    do {
        code = ptrMask[0].toUInt()
        ptrMask.incrementOffset(1)
        if (code == 0x8000u) {
            break
        }

        val len: SizeT = (code and 0x00FFu).toInt()
        val offset: SizeT = ((code shr 8) and 0x007Fu).toInt()
        ptrDst.incrementOffset(offset)
        ptrSrc.incrementOffset(offset)
        Segment(op, ptrSrc, ptrDst, ptrMask, len, clipIn, clipOut)
        ptrMask.incrementOffset(len)
        ptrDst.incrementOffset(len)
        ptrSrc.incrementOffset(len)
    } while ((code and 0x8000u) == 0u)

    return ptrMask.offset - mask.offset
}
