package com.github.servb.pph.pheroes.Game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.util.contains
import com.github.servb.pph.util.helpertype.and
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.math.clamp

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
