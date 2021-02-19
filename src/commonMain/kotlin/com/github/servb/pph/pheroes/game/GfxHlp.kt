package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.*
import com.github.servb.pph.util.helpertype.and
import com.soywiz.korma.geom.*
import com.soywiz.korma.math.clamp
import kotlin.math.absoluteValue

const val DLG_FRAME_SIZE = 16
const val DEF_BTN_HEIGHT = 19

val gradBtnText = ushortArrayOf(
    RGB16(255, 160, 0),
    RGB16(255, 164, 10),
    RGB16(255, 169, 21),
    RGB16(255, 175, 36),
    RGB16(255, 181, 52),
    RGB16(255, 188, 68),
    RGB16(255, 195, 85),
    RGB16(255, 203, 105),
    RGB16(255, 211, 123),
    RGB16(255, 218, 140),
    RGB16(255, 224, 155),
    RGB16(255, 230, 170),
    RGB16(255, 235, 180)
) // todo: check size == 13

val defGrad = iGradient(IDibPixelPointer(gradBtnText, 0), 13)

val dlgfc_hdr: iTextComposer.IFontConfig =
    iTextComposer.FontConfig(iTextComposer.FontSize.LARGE, IiDibFont.ComposeProps(RGB16(255, 180, 64)))
val dlgfc_topic: iTextComposer.IFontConfig =
    iTextComposer.FontConfig(iTextComposer.FontSize.MEDIUM, IiDibFont.ComposeProps(RGB16(255, 255, 0)))
val dlgfc_plain: iTextComposer.IFontConfig =
    iTextComposer.FontConfig(iTextComposer.FontSize.MEDIUM, IiDibFont.ComposeProps(RGB16(210, 210, 220)))
val dlgfc_stopic: iTextComposer.IFontConfig =
    iTextComposer.FontConfig(iTextComposer.FontSize.SMALL, IiDibFont.ComposeProps(RGB16(255, 255, 0)))
val dlgfc_splain: iTextComposer.IFontConfig =
    iTextComposer.FontConfig(iTextComposer.FontSize.SMALL, IiDibFont.ComposeProps(RGB16(210, 210, 220)))

val btnfc_normal: iTextComposer.IFontConfig = iTextComposer.FontConfig(
    iTextComposer.FontSize.MEDIUM,
    IiDibFont.ComposeProps(defGrad, cColor.Black.pixel, IiDibFont.Decor.Border)
)
val btnfc_pressed: iTextComposer.IFontConfig = iTextComposer.FontConfig(
    iTextComposer.FontSize.MEDIUM,
    IiDibFont.ComposeProps(defGrad, cColor.Black.pixel, IiDibFont.Decor.Border)
)
val btnfc_disabled: iTextComposer.IFontConfig = iTextComposer.FontConfig(
    iTextComposer.FontSize.MEDIUM,
    IiDibFont.ComposeProps(RGB16(160, 96, 32), cColor.Black.pixel, IiDibFont.Decor.Border)
)

const val DLG_SHADOW_OFFSET = 5

private fun dist_a(dx: Int, dy: Int): Int {
    return if (dx > dy) {
        (61685 * dx + 26870 * dy) shr 16
    } else {
        (61685 * dy + 26870 * dx) shr 16
    }
}

private fun dist_b(dx: Int, dy: Int): Int {
    return dx + dy - minOf(dx, dy) / 2
}

private class grad_shader {

    var rx: Int = 0
    var ry: Int = 0
    var rw: Int = 0
    var rh: Int = 0

    companion object {

        const val Max_Height = 256
        const val Max_Radii = 256 + 128

        val INSTANCE = grad_shader()  // try to avoid allocations
    }

    fun init(x: Int, y: Int, w: Int, h: Int) {
        rx = x
        ry = y
        rw = w
        rh = h
    }

    val vshade = UIntArray(Max_Height)
    val rshade = UIntArray(Max_Radii)

    fun precalc() {
        // total presetup cost is:
        // height: sqrt, div
        // 256   : div
        val half = rh / 2
        // vshade upper
        vshade[0] = 128u
        var n = 0
        while (n != half) {
            val arg = n
            // base is: 1 - sqrt(x)
            var value = 65535 - 256 * int_sqrt(((arg * 65536) / half).toUInt()).toInt()
            // safe 4'th power - faster falloff
            value = value shr 8
            value = value * value
            value = value shr 8
            value = value * value
            vshade[n] = value.toUInt()

            ++n
        }
        // vshare lower
        while (n != rh) {
            val arg = half - (n - half)
            // base is: 1 - sqrt(x)
            val value = 65536 - 256 * int_sqrt(((arg * 65536) / half).toUInt()).toInt()
            vshade[n] = (value shr 2).toUInt()

            ++n
        }

        // rshade [ 0..255..x ]
        val radii = minOf(rw, rh)
        check(radii <= Max_Radii)
        repeat(Max_Radii) { r ->
            val arg = (256 * r) / radii
            val value = 65536 - (arg * arg)
            rshade[r] = if (value > 0) {
                value shr 1
            } else {
                0
            }.toUInt()
        }
    }

    fun draw(ptr: IDibPixelIPointer, stride: Int) {
        // setup circle centre points
        val px = rx + rw / 2
        val py = ry + rh - rh / 4

        repeat(rh) { yy ->
            val pline = ptr.copy((yy + ry) * stride + rx)
            val ady = (py - (ry + yy)).absoluteValue
            // tight pixel loop
            repeat(rw) { xx ->
                val dx = px - (rx + xx)
                // fast eq.dist approximation
                val dst = dist_a(dx.absoluteValue, ady)
                // combined and normalized
                var value = (rshade[dst] + vshade[yy]) shr 8
                // correct overflow (its possible to get rid of
                // if we normalize beforehead, but wtf? )
                value = minOf(255u, value)
                // apply gamma and inverse
                value = 255u - ((value * value) shr 8)
                // factor is ready to multiply: [0..255] range
                val src = pline[0]
                val sr: UShort = (value * ((src.toUInt() and (0x1Fu shl 11)) shr 11)).toUShort()
                val sg: UShort = (value * ((src.toUInt() and (0x3Fu shl 5)) shr 5)).toUShort()
                val sb: UShort = (value * (src.toUInt() and 0x1Fu)).toUShort()
                pline[0] =
                    (((sr.toUInt() shr 8) shl 11) or ((sg.toUInt() shr 8) shl 5) or (sb.toUInt() shr 8)).toUShort()
                pline.incrementOffset(1)
            }
        }
    }
}

fun Tile3x3Sprite(dib: iDib, rc: IRectangleInt, sid: SpriteId) {
    val ssiz = gGfxMgr.Dimension(sid)
    check((ssiz.width % 3) == 0 && (ssiz.height % 3) == 0)
    val elw = ssiz.width / 3
    val elh = ssiz.height / 3

    val xcnt = (rc.width - elw * 2 + elw - 1) / elw
    val ycnt = (rc.height - elh * 2 + elh - 1) / elh
    var xpos = rc.x + elw
    var ypos = rc.y + elh
    var xx = 0
    var yy = 0

    while (yy < ycnt) {
        while (xx < xcnt) {
            gGfxMgr.Blit(sid, dib, IRectangleInt(elw, elh, elw, elh), IPointInt(xpos, ypos))
            xpos += elw
            ++xx
        }
        ypos += elh
        xpos = rc.x + elw
        ++yy
    }

    xpos = rc.x + elw
    xx = 0
    while (xx < xcnt) {
        gGfxMgr.Blit(sid, dib, IRectangleInt(elw, 0, elw, elh), IPointInt(xpos, rc.y))
        gGfxMgr.Blit(sid, dib, IRectangleInt(elw, elh * 2, elw, elh), IPointInt(xpos, rc.y + rc.height - elh))
        xpos += elw
        ++xx
    }

    ypos = rc.y + elh
    yy = 0
    while (yy < ycnt) {
        gGfxMgr.Blit(sid, dib, IRectangleInt(0, elh, elw, elh), IPointInt(rc.x, ypos))
        gGfxMgr.Blit(sid, dib, IRectangleInt(elw * 2, elh, elw, elh), IPointInt(rc.x + rc.width - elw, ypos))
        ypos += elh
        ++yy
    }

    gGfxMgr.Blit(sid, dib, IRectangleInt(0, 0, elw, elh), IPointInt(rc.x, rc.y))
    gGfxMgr.Blit(sid, dib, IRectangleInt(0, elh * 2, elw, elh), IPointInt(rc.x, rc.y + rc.height - elh))
    gGfxMgr.Blit(sid, dib, IRectangleInt(elw * 2, 0, elw, elh), IPointInt(rc.x + rc.width - elw, rc.y))
    gGfxMgr.Blit(
        sid,
        dib,
        IRectangleInt(elw * 2, elh * 2, elw, elh),
        IPointInt(rc.x + rc.width - elw, rc.y + rc.height - elh)
    )
}

fun FrameRoundRect(surf: iDib, rect: IRectangleInt, clr: IDibPixel) {
    surf.HLine(IPointInt(rect.x + 1, rect.y), rect.x + rect.width - 2, clr)
    surf.HLine(IPointInt(rect.x + 1, rect.y + rect.height - 1), rect.x + rect.width - 2, clr)
    surf.VLine(IPointInt(rect.x, rect.y + 1), rect.y + rect.height - 1, clr)
    surf.VLine(IPointInt(rect.x + rect.width - 1, rect.y + 1), rect.y + rect.height - 1, clr)
    surf.PutPixel(rect.x + 1, rect.y + 1, clr)
    surf.PutPixel(rect.x + rect.width - 2, rect.y + 1, clr)
    surf.PutPixel(rect.x + 1, rect.y + rect.height - 2, clr)
    surf.PutPixel(rect.x + rect.width - 2, rect.y + rect.height - 2, clr)
}

fun DrawRoundRect(surf: iDib, rect: IRectangleInt, fClr: IDibPixel, bClr: IDibPixel) {
    surf.FillRect(IRectangleInt(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2), bClr)
    FrameRoundRect(surf, rect, fClr)
}

fun ComposeDlgBkgnd(surf: iDib, rect: IRectangleInt, pid: PlayerId, bDecs: Boolean) {
    val nval = pid.v + 1
    val toffs = if (bDecs) 12 else 10
    if (rect.isEmpty) {
        return
    }

    // shadow
    surf.Darken50Rect(IRectangleInt(rect.x2 + 1, rect.y + DLG_SHADOW_OFFSET, DLG_SHADOW_OFFSET, rect.height))
    surf.Darken50Rect(
        IRectangleInt(
            rect.x + DLG_SHADOW_OFFSET,
            rect.y2 + 1,
            rect.width - DLG_SHADOW_OFFSET,
            DLG_SHADOW_OFFSET
        )
    )

    // tile background
    val bkRect = RectangleInt(rect)
    bkRect.rect.inflate(-10)
    gGfxMgr.BlitTile(if (bDecs) GfxId.PDGG_BKTILE2.v else GfxId.PDGG_BKTILE.v, surf, bkRect)

    // Shade
    if (bDecs) {
        val shader = grad_shader.INSTANCE
        shader.init(bkRect.x, bkRect.y, bkRect.width, bkRect.height)
        shader.precalc()
        shader.draw(surf.GetPtr(), surf.GetWidth())
    }

    // top/bottom tiles
    gGfxMgr.BlitTile(
        GfxId.PDGG_DLG_HTILES(nval),
        surf,
        IRectangleInt(rect.x + toffs, rect.y, rect.width - toffs * 2, 10)
    )
    gGfxMgr.BlitTile(
        GfxId.PDGG_DLG_HTILES(nval),
        surf,
        IRectangleInt(rect.x + toffs, rect.y2 - 9, rect.width - toffs * 2, 10)
    )

    val hgr = iDib(ISizeInt(rect.width - (toffs * 2), 1), IiDib.Type.RGB)
    hgr.HGradientRect(IRectangleInt(0, 0, hgr.GetWidth() / 2, 1), RGB16(64, 0, 0), RGB16(255, 192, 64))
    hgr.HGradientRect(IRectangleInt(hgr.GetWidth() / 2, 0, hgr.GetWidth() / 2, 1), RGB16(255, 192, 64), RGB16(64, 0, 0))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y + 1))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y + 8))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y2 - 8))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y2 - 1))

    // left/right tiles
    gGfxMgr.BlitTile(
        GfxId.PDGG_DLG_VTILES(nval),
        surf,
        IRectangleInt(rect.x, rect.y + toffs, 10, rect.height - toffs * 2)
    )
    gGfxMgr.BlitTile(
        GfxId.PDGG_DLG_VTILES(nval),
        surf,
        IRectangleInt(rect.x2 - 9, rect.y + toffs, 10, rect.height - toffs * 2)
    )

    val vgr = iDib(ISizeInt(1, rect.height - (toffs * 2)), IiDib.Type.RGB)
    vgr.VGradientRect(IRectangleInt(0, 0, 1, vgr.GetHeight() / 2), RGB16(64, 0, 0), RGB16(255, 192, 64))
    vgr.VGradientRect(
        IRectangleInt(0, vgr.GetHeight() / 2, 1, vgr.GetHeight() / 2),
        RGB16(255, 192, 64),
        RGB16(64, 0, 0)
    )
    vgr.CopyToDibXY(surf, IPointInt(rect.x + 1, rect.y + toffs))
    vgr.CopyToDibXY(surf, IPointInt(rect.x + 8, rect.y + toffs))
    vgr.CopyToDibXY(surf, IPointInt(rect.x2 - 8, rect.y + toffs))
    vgr.CopyToDibXY(surf, IPointInt(rect.x2 - 1, rect.y + toffs))

    // corners
    if (bDecs) {
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS(0), surf, IPointInt(rect.x - 2, rect.y - 2))
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS(1), surf, IPointInt(rect.x2 - 27, rect.y - 2))
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS(2), surf, IPointInt(rect.x - 2, rect.y2 - 27))
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS(3), surf, IPointInt(rect.x2 - 27, rect.y2 - 27))
    } else {
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS_SMALL(nval), surf, IPointInt(rect.x, rect.y))
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS_SMALL(nval), surf, IPointInt(rect.x2 - 9, rect.y))
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS_SMALL(nval), surf, IPointInt(rect.x, rect.y2 - 9))
        gGfxMgr.Blit(GfxId.PDGG_DLG_CORNERS_SMALL(nval), surf, IPointInt(rect.x2 - 9, rect.y2 - 9))
    }
}

fun BlitIcon(dib: iDib, sid: SpriteId, orc: IRectangleInt) =
    BlitIcon(dib, sid, orc, 63u)  // todo: remove after KT-44180

fun BlitIcon(
    dib: iDib,
    sid: SpriteId,
    orc: IRectangleInt,
    a: UByte/* = 63u*/,
    offset: IPointInt = IPointInt(0, 0)
) {  // alpha - 0-63
    val ssiz = gGfxMgr.Dimension(sid)
    val offs = gGfxMgr.Anchor(sid)
    val op = IPointInt(
        orc.x + (orc.width / 2 - ssiz.width / 2) - offs.x + offset.x,
        orc.y + (orc.height / 2 - ssiz.height / 2) - offs.y + offset.y
    )
    if (a == 63u.toUByte()) {
        gGfxMgr.Blit(sid, dib, op)
    } else {
        gGfxMgr.BlitAlpha(sid, dib, op, a)
    }
}

fun ButtonFrame(dib: iDib, rc: IRectangleInt, state: Int) {
    val ltClr = if ((state and iButton.State.Pressed) != 0) cColor.Black else cColor.White
    val rbClr = if ((state and iButton.State.Pressed) != 0) cColor.White else cColor.Black
    dib.HLine(IPointInt(rc.x + 1, rc.y), rc.x2 - 1, ltClr.pixel, 64.toUByte())
    dib.HLine(IPointInt(rc.x + 1, rc.y2), rc.x2 - 1, rbClr.pixel, 64.toUByte())
    dib.VLine(IPointInt(rc.x, rc.y + 1), rc.y2, ltClr.pixel, 64.toUByte())
    dib.VLine(IPointInt(rc.x2, rc.y + 1), rc.y2, rbClr.pixel, 64.toUByte())
    //if (state&iButton::Disabled) dib.Darken25Rect(rc);  // commented in sources
}

fun GetButtonFont(state: Int): iTextComposer.IFontConfig {
    if ((state and iButton.State.Disabled) != 0) {
        return btnfc_disabled
    } else if ((state and iButton.State.Pressed) != 0) {
        return btnfc_pressed
    } else {
        return btnfc_normal
    }
}

fun ComposeDlgButton(dib: iDib, rc: IRectangleInt, state: Int) {
    val btnSid = if ((state and iButton.State.Disabled.v) != 0) {
        GfxId.PDGG_DLG_BTN(0)
    } else if ((state and iButton.State.Pressed.v) != 0) {
        GfxId.PDGG_DLG_BTN(1)
    } else {
        GfxId.PDGG_DLG_BTN(2)
    }

    // Compose outer frame
    val rect = RectangleInt(rc)
    rect.rect.inflate(1)
    dib.HLine(IPointInt(rect.x + 2, rect.y), rect.x + rect.width - 3, 0u)
    dib.HLine(IPointInt(rect.x + 2, rect.y + rect.height - 1), rect.x + rect.width - 3, 0u)
    dib.VLine(IPointInt(rect.x, rect.y + 2), rect.y + rect.height - 2, 0u)
    dib.VLine(IPointInt(rect.x + rect.width - 1, rect.y + 2), rect.y + rect.height - 2, 0u)

    // Compose button background
    Tile3x3Sprite(dib, rc, btnSid)
}

fun ComposeDlgTextButton(dib: iDib, rc: IRectangleInt, state: Int, textKey: TextResId) {
    ComposeDlgButton(dib, rc, state)

    if (textKey != TextResId.TRID_VOID) {
        val fc = GetButtonFont(state)
        gTextComposer.TextOut(
            fc,
            dib,
            rc.asPoint(),
            gTextMgr[textKey],
            rc,
            Alignment.AlignCenter,
            if ((state and iButton.State.Pressed.v) != 0) PointInt(1, 1) else PointInt()
        )
    }
}

fun ComposeDlgIconButton(dib: iDib, rc: IRectangleInt, state: Int, sid: SpriteId) {
    ComposeDlgButton(dib, rc, state)

    // Compose Icon
    val ssiz = gGfxMgr.Dimension(sid)
    //val op = IPointInt(rc.x+(rc.width/2-ssiz.width/2), rc.y+(rc.height/2-ssiz.height/2))  // unused in sources
    BlitIcon(
        dib,
        sid,
        rc,
        if ((state and iButton.State.Disabled) != 0) 32u else 63u,
        if ((state and iButton.State.Pressed) != 0) IPointInt(1, 1) else IPointInt(0, 0)
    )
}

fun ComposeProgressBar(bVertical: Boolean, dib: iDib, rc: IRectangleInt, clr: IDibPixel, cval: Int, mval: Int) {
    val bclr = Darken50(clr.toUInt()).toUShort()
    dib.FillRect(rc, bclr)
    val trc = if (bVertical) {
        val mg = ((cval * rc.height) / mval).clamp(0, rc.height)
        RectangleInt(rc.x, rc.y + rc.height - mg, rc.width, mg)
    } else {
        val mg = ((cval * rc.width) / mval).clamp(0, rc.width)
        RectangleInt(rc.x, rc.y, mg, rc.height)
    }
    dib.FillRect(trc, clr)

    dib.HLine(IPointInt(rc.x, rc.y), rc.x + rc.width - 2, cColor.White.pixel, 48u)
    dib.VLine(IPointInt(rc.x, rc.y + 1), rc.y + rc.height - 1, cColor.White.pixel, 48u)
    dib.HLine(IPointInt(rc.x + 1, rc.y + rc.height - 1), rc.x + rc.width - 1, cColor.Black.pixel, 48u)
    dib.VLine(IPointInt(rc.x + rc.width - 1, rc.y + 1), rc.y + rc.height - 1, cColor.Black.pixel, 48u)
}

internal val stars = listOf(
    RGB16(220, 220, 220),
    RGB16(192, 192, 192),
    RGB16(160, 160, 160),
    RGB16(128, 128, 128)
)  // todo: check length in tests

fun FillStaredRect(surf: iDib, rect: IRectangleInt, anchor: IPointInt) {
    val rand = iRandomizer()
    surf.FillRect(rect, cColor.Black.pixel)
    repeat(2048) { nn ->
        // todo: doesn't support wide screen?
        val px = (rand.Rand() - anchor.x) % 320
        val py = (rand.Rand() - anchor.y) % 240
        if (rect.contains(px, py)) {
            surf.PutPixel(px, py, stars[nn % stars.size])
        }
    }
}
