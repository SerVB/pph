package com.github.servb.pph.pheroes.Game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.util.*
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.lang.substr
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt

private const val CCODE = '#'
private val dots = "..."

private fun HexChar2Int(c: Char): UByte {
    return when (c) {
        in '0'..'9' -> (c - '0').toUByte()
        in 'A'..'F' -> (c - 'A' + 10).toUByte()
        else -> throw IllegalArgumentException("bad char: $c (${c.toInt()})")
    }
}

private fun ProcessChar(
    text: String,
    textIdx: Mutable<Int>,
    sid: Mutable<SpriteId>,
    cfg: iTextComposer.FontConfig
): Boolean {
    if (text[textIdx.element] == CCODE) {
        ++textIdx.element
        when (text[textIdx.element]) {
            'I' -> {
                ++textIdx.element
                val r = HexChar2Int(text[textIdx.element++]).toInt()
                val g = HexChar2Int(text[textIdx.element++]).toInt()
                val b = HexChar2Int(text[textIdx.element++]).toInt()
                val a = HexChar2Int(text[textIdx.element++]).toInt()
                sid.element = (r shl 12) or (g shl 8) or (b shl 4) or a
            }
            'A' -> {
                ++textIdx.element
                val r = HexChar2Int(text[textIdx.element++]).toInt()
                val g = HexChar2Int(text[textIdx.element++]).toInt()
                cfg.cmpProps.alpha = ((r shl 4) or g).toUByte()
            }
            'S' -> {
                ++textIdx.element
                val r = HexChar2Int(text[textIdx.element++]).toInt()
                check(r >= 0 && r < iTextComposer.FontSize.COUNT.v)
                cfg.fontSize = iTextComposer.FontSize.values().first { it.v == r }
            }
            'F' -> {
                ++textIdx.element
                val r = HexChar2Int(text[textIdx.element++]).toInt()
                val g = HexChar2Int(text[textIdx.element++]).toInt()
                val b = HexChar2Int(text[textIdx.element++]).toInt()
                cfg.cmpProps.faceColor = ((r shl 12) or (g shl 7) or (b shl 1)).toUShort()
            }
            'B' -> {
                ++textIdx.element
                val r = HexChar2Int(text[textIdx.element++]).toInt()
                val g = HexChar2Int(text[textIdx.element++]).toInt()
                val b = HexChar2Int(text[textIdx.element++]).toInt()
                cfg.cmpProps.borderColor = ((r shl 12) or (g shl 7) or (b shl 1)).toUShort()
            }
        }
        return false
    } else if (text[textIdx.element] == '\r') {
        ++textIdx.element
        return false
    }
    return true
}

class iTextComposer {

    enum class FontSize(override val v: Int, val resourcePath: String) : CountValueEnum, UniqueValueEnum {
        SMALL(0, "pheroes/bin/fnt_sml.png"),
        MEDIUM(1, "pheroes/bin/fnt_med.png"),
        LARGE(2, "pheroes/bin/fnt_big.png"),
        COUNT(3, "FontSize.COUNT"),
    }

    interface IFontConfig {

        val fontSize: FontSize
        val cmpProps: IiDibFont.IComposeProps
    }

    class FontConfig(override var fontSize: FontSize, override var cmpProps: IiDibFont.ComposeProps) : IFontConfig {

        constructor(other: IFontConfig) : this(other.fontSize, IiDibFont.ComposeProps(other.cmpProps))
    }

    private val m_fontConfig: FontConfig = FontConfig(FontSize.MEDIUM, IiDibFont.ComposeProps(RGB16(255, 255, 255)))
    private lateinit var m_Fonts: MutableList<iDibFont>

    suspend fun Init(): Boolean {
        m_Fonts = mutableListOf()

        repeat(FontSize.COUNT.v) { nn ->
            val resourcePath = FontSize.values().first { it.v == nn }.resourcePath
            val bmp = resourcesVfs[resourcePath].readBitmap().toBMP32()
            val next = iDibFont()
            if (!next.Init(bmp, if (nn == 2) 2 else 0)) {
                return false
            }
            m_Fonts.add(next)
        }
        return true
    }

    fun SetFontSize(fs: FontSize) {
        m_fontConfig.fontSize = fs
    }

    fun CropString(text: String, fc: FontConfig, width: SizeT, bDot: Boolean = true): String {
        var txtWidth = GetTextSize(text, fc).width
        if (text.isEmpty() || txtWidth <= width) return text

        val dotsWidth = GetTextSize(dots, fc).width
        var strLen = text.length

        do {
            --strLen
            txtWidth -= m_Fonts[fc.fontSize.v].GetCharWidth(text[strLen])
        } while (strLen > 0 && (txtWidth + dotsWidth) > width)

        if (strLen == 0) {
            return ""
        }
        return text.take(strLen) + dots
    }

    fun GetTextSize(text: String, cfg: FontConfig): SizeInt {
        val res = SizeInt(2, m_Fonts[cfg.fontSize.v].GetFontHeight())

        val textIdx = Mutable(0)

        while (textIdx.element < text.length) {
            val spriteId = Mutable(-1)
            if (ProcessChar(text, textIdx, spriteId, cfg)) {
                val nw = m_Fonts[cfg.fontSize.v].GetCharWidth(text[textIdx.element])
                res.width += nw
                ++textIdx.element
            } else if (spriteId.element != -1) {
                TODO("res.width += gGfxMgr.Dimension(spriteId.element).w")
            }
        }

        return res
    }

    fun GetTextBoxSize(text: String, width: SizeT, fc: IFontConfig): SizeInt {
        if (text.isEmpty()) {
            return SizeInt()
        }

        var height = 0
        val tbIdx = 0  // always 0, can simplify
        val curIdx = Mutable(0)
        var lbIdx = 0
        var lsIdx = 0
        val spriteId = Mutable(0)
        val ffc = FontConfig(fc)

        while (curIdx.element < text.length) {
            if (!ProcessChar(text, curIdx, spriteId, ffc)) {
                continue
            }
            if (text[curIdx.element] == '\n') {
                height += 11
                ++curIdx.element
                lbIdx = curIdx.element
                lsIdx = 0
            } else {
                if (text[curIdx.element] == ' ') {
                    lsIdx = curIdx.element
                }
                val ts = GetTextSize(text.substr(lbIdx - tbIdx, curIdx.element - lbIdx + 1), ffc)
                if (ts.width > width) {
                    if (lsIdx != 0) {
                        curIdx.element = lsIdx
                    } else {
                        --curIdx.element
                    }
                    height += 11
                    ++curIdx.element
                    lbIdx = curIdx.element
                    lsIdx = 0
                } else {
                    ++curIdx.element
                }
            }
        }

        if (curIdx.element != lbIdx) {
            height += 11
        }

        return SizeInt(width, height)
    }

    fun TextOut(
        fcc: IFontConfig,
        dib: iDib,
        pos: IPointInt,
        text: String,
        rect: IRectangleInt = cInvalidRect,
        al: Alignment = Alignment.AlignTopLeft,
        offset: IPointInt = IPointInt(0, 0)
    ) {
        val op = PointInt(pos)
        val fc = FontConfig(fcc)

        if (rect != cInvalidRect) {
            val orc = AlignRect(GetTextSize(text, fc), rect, al)
            op.setTo(orc.x, orc.y)
        }

        op.x += offset.x
        op.y += offset.y

        // move baseline
        val foy = op.y

        val textIdx = Mutable(0)

        while (textIdx.element < text.length) {
            val spriteId = Mutable(-1)
            if (ProcessChar(text, textIdx, spriteId, fc)) {
                // Shadow or border
                if (fc.cmpProps.decor != IiDibFont.Decor.None) {
                    m_Fonts[fc.fontSize.v].ComposeChar(
                        text[textIdx.element],
                        dib,
                        IPointInt(op.x, foy),
                        fc.cmpProps,
                        fc.cmpProps.decor
                    )
                }
                // Text face
                val nw = m_Fonts[fc.fontSize.v].ComposeChar(
                    text[textIdx.element],
                    dib,
                    IPointInt(op.x, foy),
                    fc.cmpProps,
                    IiDibFont.Decor.None
                )
                op.x += nw
                ++textIdx.element
            } else if (spriteId.element != -1) {
                TODO("calls to gGfxMgr")
            }
        }
    }

    fun TextOut(
        dib: iDib,
        pos: IPointInt,
        text: String,
        rect: IRectangleInt = cInvalidRect,
        al: Alignment = Alignment.AlignTopLeft,
        offset: IPointInt = IPointInt(0, 0)
    ) {
        val fc = FontConfig(m_fontConfig)
        TextOut(fc, dib, pos, text, rect, al, offset)
    }

    fun TextBoxOut(fc: IFontConfig, dib: iDib, text: String, orc: IRectangleInt): Int {
        if (text.isEmpty()) {
            return 0
        }

        var height = 0
        val rect = RectangleInt(orc)
        rect.height = 11
        val tbIdx = 0  // always 0, can simplify
        val curIdx = Mutable(0)
        var lbIdx = 0
        var lsIdx = 0
        val spriteId = Mutable(0)
        val ffc = FontConfig(fc)

        while (curIdx.element < text.length) {
            if (!ProcessChar(text, curIdx, spriteId, ffc)) {
                continue
            }
            if (text[curIdx.element] == '\n') {
                if (lbIdx != curIdx.element) {
                    TextOut(
                        ffc,
                        dib,
                        rect.asPoint(),
                        text.substr(lbIdx - tbIdx, curIdx.element - lbIdx),
                        rect,
                        Alignment.AlignCenter
                    )
                }
                height += 11
                rect.y += 11
                ++curIdx.element
                lbIdx = curIdx.element
                lsIdx = 0
            } else {
                if (text[curIdx.element] == ' ') {
                    lsIdx = curIdx.element
                }
                val ts = GetTextSize(text.substr(lbIdx - tbIdx, curIdx.element - lbIdx + 1), ffc)
                if (ts.width > rect.width) {
                    if (lsIdx != 0) {
                        curIdx.element = lsIdx
                    } else {
                        --curIdx.element
                    }
                    if (lbIdx != curIdx.element) {
                        TextOut(
                            ffc,
                            dib,
                            rect.asPoint(),
                            text.substr(lbIdx - tbIdx, curIdx.element - lbIdx + 1),
                            rect,
                            Alignment.AlignCenter
                        )
                    }
                    height += 11
                    rect.y += 11
                    ++curIdx.element
                    lbIdx = curIdx.element
                    lsIdx = 0
                } else {
                    ++curIdx.element
                }
            }
        }

        if (curIdx.element != lbIdx) {
            TextOut(
                ffc,
                dib,
                rect.asPoint(),
                text.substr(lbIdx - tbIdx, curIdx.element - lbIdx),
                rect,
                Alignment.AlignCenter
            )
            height += 11
        }

        // commented in sources:
        /*
        sint32 lineBegin=0;
        sint32 linePos = 0;
        sint32 height = 0;
        iRect rect(orc);
        rect.h=10;

        sint32 spos = text.Find(_T(' '),linePos);
        while (1) {
            iStringT tTxt = text.Mid(lineBegin, (spos ==-1)?0:spos-lineBegin);
            iSize sSiz = GetTextSize(tTxt, fc );
            if ( sSiz.w > orc.w && linePos>lineBegin) {
                iStringT oText = text.Mid(lineBegin, linePos-lineBegin-1);
                TextOut(fc,dib,rect.point(),oText,rect,AlignCenter);
                height += 11;
                rect.y += 11;
                lineBegin = linePos;
            }
            if (spos == -1) break;
            linePos = spos+1;
            spos = text.Find(_T(' '),linePos);
        }
        TextOut(fc,dib,rect.point(),text.Mid(lineBegin),rect,AlignCenter);
        height += 11;
        */

        return height
    }

    fun TextBoxOut(dib: iDib, text: String, orc: IRectangleInt): Int {
        val fc = FontConfig(m_fontConfig)
        return TextBoxOut(fc, dib, text, orc)
    }
}
