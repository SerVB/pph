package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.GfxId
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.*
import com.github.servb.pph.util.helpertype.and
import com.soywiz.korma.geom.*
import com.soywiz.korma.math.clamp

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

    // tile background  // todo
    val bkRect = RectangleInt(rect)
    bkRect.rect.inflate(-10)
    gGfxMgr.BlitTile(if (bDecs) GfxId.PDGG_BKTILE2.v else GfxId.PDGG_BKTILE.v, surf, bkRect)

    // Shade
    // todo

    // top/bottom tiles  // todo
//    gGfxMgr.BlitTile(PDGG_DLG_HTILES+nval, surf, iRect(rect.x+toffs,rect.y,rect.w-toffs*2,10));
//    gGfxMgr.BlitTile(PDGG_DLG_HTILES+nval, surf, iRect(rect.x+toffs,rect.y2()-9,rect.w-toffs*2,10));

    val hgr = iDib(ISizeInt(rect.width - (toffs * 2), 1), IiDib.Type.RGB)
    hgr.HGradientRect(IRectangleInt(0, 0, hgr.GetWidth() / 2, 1), RGB16(64, 0, 0), RGB16(255, 192, 64))
    hgr.HGradientRect(IRectangleInt(hgr.GetWidth() / 2, 0, hgr.GetWidth() / 2, 1), RGB16(255, 192, 64), RGB16(64, 0, 0))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y + 1))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y + 8))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y2 - 8))
    hgr.CopyToDibXY(surf, IPointInt(rect.x + toffs, rect.y2 - 1))

    // left/right tiles
//    gGfxMgr.BlitTile(PDGG_DLG_VTILES+nval, surf, iRect(rect.x,rect.y+toffs,10,rect.h-toffs*2));
//    gGfxMgr.BlitTile(PDGG_DLG_VTILES+nval, surf, iRect(rect.x2()-9,rect.y+toffs,10,rect.h-toffs*2));

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
    // todo
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
