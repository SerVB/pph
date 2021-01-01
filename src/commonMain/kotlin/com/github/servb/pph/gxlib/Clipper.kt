@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS", "FunctionName")
@file:OptIn(ExperimentalUnsignedTypes::class)

package com.github.servb.pph.gxlib

import com.github.servb.pph.util.ISizeInt
import com.github.servb.pph.util.Mutable
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.or
import com.github.servb.pph.util.x2
import com.github.servb.pph.util.y2
import com.soywiz.korma.geom.*
import com.soywiz.korma.math.clamp

enum class Alignment(override val v: Int) : UniqueValueEnum {
    AlignCenter(0),
    AlignTop(0b0001),
    AlignRight(0b0010),
    AlignBottom(0b0100),
    AlignLeft(0b1000),
    AlignTopRight(AlignTop or AlignRight),
    AlignBottomRight(AlignBottom or AlignRight),
    AlignTopLeft(AlignTop or AlignLeft),
    AlignBottomLeft(AlignBottom or AlignLeft);
}

/** Returns rectangle with specified size aligned in specified dst rect. */
fun AlignRect(ss: ISizeInt, dr: IRectangleInt, al: Alignment): RectangleInt {
    val sw = ss.width
    val sh = ss.height
    val dw = dr.width
    val dh = dr.height

    // Vertical alignment
    val y = when {
        al.v and Alignment.AlignTop.v != 0 -> dr.y
        al.v and Alignment.AlignBottom.v != 0 -> dr.y + dh - sh
        else -> dr.y + ((dh shr 1) - (sh shr 1))
    }

    // Horizontal alignment
    val x = when {
        al.v and Alignment.AlignLeft.v != 0 -> dr.x
        al.v and Alignment.AlignRight.v != 0 -> dr.x + dw - sw
        else -> dr.x + ((dw shr 1) - (sw shr 1))
    }

    return RectangleInt(x, y, ss.width, ss.height)
}

val cInvalidPoint = IPointInt(0x7fff, 0x7fff)
val cInvalidSize = ISizeInt(0xffff, 0xffff)
val cInvalidRect = IRectangleInt(0x7fff, 0x7fff, 0xffff, 0xffff)

private fun pow2(v: Int) = v * v

fun IsLineIntersectCircle(cp: IPointInt, cr: Int, p1: IPointInt, p2: IPointInt): Boolean {
    val x01 = p1.x - cp.x
    val y01 = p1.y - cp.y
    val x02 = p2.x - cp.x
    val y02 = p2.y - cp.y

    val dx = x02 - x01
    val dy = y02 - y01

    val a = pow2(dx) + pow2(dy)
    val b = 2 * (x01 * dx + y01 * dy)
    val c = pow2(x01) + pow2(y01) - pow2(cr)

    return when {
        -b < 0 -> c < 0
        -b < 2 * a -> 4 * a * c - pow2(b) < 0
        else -> a + b + c < 0
    }

    // Commented in sources:
    /*
    if ( !iRect(p1,p2).PtInRect(cp) ) return false;

    float dist = (( (p1.y - p2.y) * cp.x + (p2.x - p1.x) * cp.y + (p1.x * p2.y - p2.x * p1.y)) / sqrt( (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y) ));
    if (dist < 0 ) dist = -dist;

    bool res = cr > dist;
    OutputDebugString(iFormat(_T("Circle (%d, %d, %d), Line (%d, %d -> %d, %d) : %f\n"), cp.x, cp.y, cr, p1.x, p1.y, p2.x, p2.y, dist).CStr());
    return res;
    */
}

fun ClipPoint(pnt: PointInt, rect: IRectangleInt): Boolean {
    pnt.x = pnt.x.clamp(rect.x, rect.x2)
    pnt.y = pnt.y.clamp(rect.y, rect.y2)
    return true
}

fun ClipHLine(pnt1: PointInt, x2: Mutable<Int>, rect: IRectangleInt): Boolean {
    if (pnt1.y !in rect.y..rect.y2) {
        return false
    }
    pnt1.x = pnt1.x.clamp(rect.x, rect.x2)
    x2.element = x2.element.clamp(rect.x, rect.x2)
    return pnt1.x != x2.element
}

fun ClipVLine(pnt1: PointInt, y2: Mutable<Int>, rect: IRectangleInt): Boolean {
    if (pnt1.x !in rect.x..rect.x2) {
        return false
    }
    pnt1.y = pnt1.y.clamp(rect.y, rect.y2)
    y2.element = y2.element.clamp(rect.y, rect.y2)
    return pnt1.y != y2.element
}

fun ClipLine(pnt1: PointInt, pnt2: PointInt, rect: IRectangleInt): Boolean {
    val d = PointInt()

    /* CLIPCODE( pnt1, c1 ) */
    var c1 = 0u
    if (pnt1.x < rect.x) c1 = 8u or c1
    if (pnt1.x > rect.x2) c1 = 4u or c1
    if (pnt1.y < rect.y) c1 = 2u or c1
    if (pnt1.y > rect.y2) c1 = 1u or c1
    /* CLIPCODE( pnt2, c2 ) */
    var c2 = 0u
    if (pnt2.x < rect.x) c2 = 8u or c2
    if (pnt2.x > rect.x2) c2 = 4u or c2
    if (pnt2.y < rect.y) c2 = 2u or c2
    if (pnt2.y > rect.y2) c2 = 1u or c2

    while (c1 or c2 != 0u) {
        if (c1 and c2 == 0u) {
            return false
        }
        d.x = pnt2.x - pnt1.x
        d.y = pnt2.y - pnt1.y
        if (c1 != 0u) {
            if (pnt1.x < rect.x) {
                pnt1.y += d.y * (rect.x - pnt1.x) / d.x
                pnt1.x = rect.x
            } else if (pnt1.x > rect.x2) {
                pnt1.y += d.y * (rect.x2 - pnt1.x) / d.x
                pnt1.x = rect.x2
            } else if (pnt1.y < rect.y) {
                pnt1.x += d.x * (rect.y - pnt1.y) / d.y
                pnt1.y = rect.y
            } else if (pnt1.y > rect.y2) {
                pnt1.x += d.x * (rect.y2 - pnt1.y) / d.y
                pnt1.y = rect.y2
            }
            /* CLIPCODE( pnt1, c1 ) */
            c1 = 0u
            if (pnt1.x < rect.x) c1 = 8u or c1
            if (pnt1.x > rect.x2) c1 = 4u or c1
            if (pnt1.y < rect.y) c1 = 2u or c1
            if (pnt1.y > rect.y2) c1 = 1u or c1
        } else {
            if (pnt2.x < rect.x) {
                pnt2.y += d.y * (rect.x - pnt2.x) / d.x
                pnt2.x = rect.x
            } else if (pnt2.x > rect.x2) {
                pnt2.y += d.y * (rect.x2 - pnt2.x) / d.x
                pnt2.x = rect.x2
            } else if (pnt2.y < rect.y) {
                pnt2.x += d.x * (rect.y - pnt2.y) / d.y
                pnt2.y = rect.y
            } else if (pnt2.y > rect.y2) {
                pnt2.x += d.x * (rect.y2 - pnt2.y) / d.y
                pnt2.y = rect.y2
            }
            /* CLIPCODE( pnt2, c2 ) */
            c2 = 0u
            if (pnt2.x < rect.x) c2 = 8u or c2
            if (pnt2.x > rect.x2) c2 = 4u or c2
            if (pnt2.y < rect.y) c2 = 2u or c2
            if (pnt2.y > rect.y2) c2 = 1u or c2
        }
    }
    return true
}

fun IntersectRect(dst_rect: RectangleInt, src_rect1: IRectangleInt, src_rect2: IRectangleInt): Boolean {
    val x = maxOf(src_rect1.x, src_rect2.x)
    val y = maxOf(src_rect1.y, src_rect2.y)
    val right = minOf(src_rect1.right, src_rect2.right)
    val bottom = minOf(src_rect1.bottom, src_rect2.bottom)

    val width = right - x
    val height = bottom - y

    return if (width > 0 && height > 0) {
        dst_rect.setTo(x, y, width, height)
        true
    } else {
        false
    }
}

fun IsIntersectRect(src_rect1: IRectangleInt, src_rect2: IRectangleInt): Boolean {
    val x = maxOf(src_rect1.x, src_rect2.x)
    val y = maxOf(src_rect1.y, src_rect2.y)
    val right = minOf(src_rect1.right, src_rect2.right)
    val bottom = minOf(src_rect1.bottom, src_rect2.bottom)
    return right - x > 0 && bottom - y > 0
}

fun ClipRect(rc: RectangleInt, rect: IRectangleInt): Boolean {
    if (rc.x < rect.x) {
        val v = rect.x - rc.x
        if (v > rc.width) return false
        rc.width -= v
        rc.x = rect.x
    }

    if (rc.y < rect.y) {
        val v = rect.y - rc.y
        if (v > rc.height) return false
        rc.height -= v
        rc.y = rect.y
    }

    if (rect.x2 < rc.x2) {
        val v = rc.x2 - rect.x2
        if (v > rc.width) return false
        rc.width -= v
    }

    if (rect.y2 < rc.y2) {
        val v = rc.y2 - rect.y2
        if (v > rc.height) return false
        rc.height -= v
    }
    return rc.width > 0 && rc.height > 0
}

fun iClipRectRect(
    dst_rc: RectangleInt,
    dst_rect: IRectangleInt,
    src_rc: RectangleInt,
    src_rect: IRectangleInt
): Boolean {
    //iSize cl = src_rc
    var clw = src_rc.width
    var clh = src_rc.height

    // check ridiculous cases
    if (!IsIntersectRect(dst_rc, dst_rect) || !IsIntersectRect(src_rc, src_rect)) return false

    // clip src left
    if (src_rc.x < 0) {
        clw += src_rc.x
        dst_rc.x -= src_rc.x
        src_rc.x = 0
    }
    // clip src top
    if (src_rc.y < 0) {
        clh += src_rc.y
        dst_rc.y -= src_rc.y
        src_rc.y = 0
    }
    // clip src right
    if (src_rc.x + clw > src_rect.width) {
        clw = src_rect.width - src_rc.x
    }
    // clip src bottom
    if (src_rc.y + clh > src_rect.height) {
        clh = src_rect.height - src_rc.y
    }
    // clip dest left
    if (dst_rc.x < dst_rect.x) {
        dst_rc.x -= dst_rect.x
        clw += dst_rc.x
        src_rc.x -= dst_rc.x
        dst_rc.x = dst_rect.x
    }
    // clip dest top
    if (dst_rc.y < dst_rect.y) {
        dst_rc.y -= dst_rect.y
        clh += dst_rc.y
        src_rc.y -= dst_rc.y
        dst_rc.y = dst_rect.y
    }
    // clip dest right
    if (dst_rc.x + clw >= dst_rect.x2 + 1) {
        clw = (dst_rect.x2 - dst_rc.x + 1)
    }
    // clip dest bottom
    if (dst_rc.y + clh >= dst_rect.y2 + 1) {
        clh = (dst_rect.y2 - dst_rc.y + 1)
    }
    // bail out on zero size
    if (clw <= 0 || clh <= 0) {
        return false
    }

    dst_rc.width = clw; dst_rc.height = clh
    src_rc.width = clw; src_rc.height = clh

    return true
}
