package com.github.servb.pph.gxlib

import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.SizeTArray
import com.soywiz.kmem.ByteArrayBuilder
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.SizeInt
import kotlin.properties.Delegates

private class CharDesc(val posX: Int, val width: SizeT, val reserved: UByte)

private const val MaxCharCode = 0x0500
private const val InvCharIdx = 0xFFFF

interface IiDibFont {

    enum class Decor {
        None,
        Shadow,
        Border,
    }

    enum class FillType {
        Solid,
        Gradient,
    }

    interface IComposeProps {

        val fillType: FillType
        val gradient: iGradient
        val faceColor: IDibPixel
        val borderColor: IDibPixel
        val decor: Decor
        val alpha: UByte
    }

    class ComposeProps : IComposeProps {

        override var fillType: FillType
        override var gradient: iGradient
        override var faceColor: IDibPixel
        override var borderColor: IDibPixel
        override var decor: Decor
        override var alpha: UByte

        constructor(other: IComposeProps) {
            fillType = other.fillType
            gradient = other.gradient
            faceColor = other.faceColor
            borderColor = other.borderColor
            decor = other.decor
            alpha = other.alpha
        }

        constructor(
            _faceColor: IDibPixel,
            _borderColor: IDibPixel = cColor.Black.pixel,
            _decor: Decor = Decor.Shadow,
            _alpha: UByte = 255u
        ) {
            faceColor = _faceColor
            borderColor = _borderColor
            decor = _decor
            alpha = _alpha
            fillType = FillType.Solid
            gradient = iGradient(IDibPixelPointer(ushortArrayOf(), 0), 0)
        }

        constructor(
            _gradient: iGradient,
            _borderColor: IDibPixel = cColor.Black.pixel,
            _decor: Decor = Decor.Shadow,
            _alpha: UByte = 255u
        ) {
            gradient = _gradient
            borderColor = _borderColor
            decor = _decor
            alpha = _alpha
            fillType = FillType.Gradient
            faceColor = 0u
        }
    }

    fun IsValidChar(c: Char): Boolean
    fun IsValidString(str: String): Boolean

    fun GetDefLeading(): Int
    fun GetCharWidth(c: Char): SizeT
    fun GetCharSize(c: Char): SizeInt
    fun GetFontHeight(): SizeT

    fun ComposeChar(c: Char, surf: iDib, pos: IPointInt, cmpProps: ComposeProps, layer: Decor): Int
}

// ComposeText is unused
class iDibFont : IiDibFont {

    private var m_fntHeight by Delegates.notNull<SizeT>()
    private val m_elDesc: MutableList<CharDesc> = mutableListOf()
    private val m_idxList: SizeTArray = SizeTArray(MaxCharCode)
    private var m_baseLine by Delegates.notNull<Int>()
    private var m_rawFont by Delegates.notNull<UByteArray>()

//    fun Init(buff: SyncInputStream, baseLine: Int): Boolean {
//        m_baseLine = baseLine
//        m_idxList.fill(InvCharIdx)
//        var cidx = 0
//        (0x20..0x7E).forEach { m_idxList[it] = cidx++ }
//        (0xC0..0x17E).forEach { m_idxList[it] = cidx++ }
//        (0x401..0x45F).forEach { m_idxList[it] = cidx++ }
//
//        val el_cnt: SizeT = buff.readU32BE().toInt()  // todo: or LE?
//        m_elDesc.clear()
//        val fh = buff.readU8()
//        m_fntHeight = fh
//
//        repeat(el_cnt) {
//            val posX = buff.readU16BE()  // todo: or LE?
//            val width = buff.readU8()
//            val reserved = buff.readU8().toUByte()
//            m_elDesc.add(CharDesc(posX, width, reserved))
//        }
//
//        val rawBufSiz: SizeT = buff.readU16BE()  // todo: or LE?
//        m_rawFont = UByteArray(rawBufSiz)
//        buff.read(m_rawFont)
//
//        return true
//    }

    fun Init(bmp: Bitmap, baseLine: Int): Boolean {
        m_baseLine = baseLine
        m_idxList.fill(InvCharIdx)
        var cidx = 0
        (0x20..0x7E).forEach { m_idxList[it] = cidx++ }
        (0xC0..0x17E).forEach { m_idxList[it] = cidx++ }
        (0x401..0x45F).forEach { m_idxList[it] = cidx++ }

        m_fntHeight = bmp.height - 1

        m_elDesc.clear()
        var lastAnch = 0
        (0..bmp.width).forEach { xx ->
            if (bmp.getRgba(xx, 0) == Colors.RED || xx == bmp.width) {
                val width = xx - lastAnch - 1
                val posX = lastAnch
                val reserved: UByte = 0u
                m_elDesc.add(CharDesc(posX, width, reserved))
                lastAnch = xx
            }
        }

        fun Pack8PixelsToByte(bmp: Bitmap, yy: Int, pos: Int, cnt: Int): UByte {
            var res: UByte = 0u
            repeat(cnt) { nn ->
                if (bmp.getRgba(pos + nn, yy).r > 0) {
                    res = res or (1u shl nn).toUByte()
                }
            }
            return res
        }

        val bb = ByteArrayBuilder()

        val lineBuffSiz = (bmp.width + 7) / 8
        val lineBuff = UByteArray(lineBuffSiz)
        (1 until bmp.height).forEach { yy ->
            lineBuff.fill(0u)
            var pos = 0
            repeat(lineBuffSiz) { xx ->
                var cnt = 8
                if (pos + 8 >= bmp.width) {
                    cnt = bmp.width - pos
                }
                lineBuff[xx] = Pack8PixelsToByte(bmp, yy, pos, cnt)
                pos += 8
            }
            bb.append(lineBuff.asByteArray())
        }

        m_rawFont = bb.toByteArray().asUByteArray()

        return true
    }

    override fun IsValidChar(c: Char): Boolean = c.toInt() < MaxCharCode && m_idxList[c] != InvCharIdx
    override fun IsValidString(str: String): Boolean = str.all(this::IsValidChar)

    override fun GetDefLeading(): Int = m_fntHeight + 1
    override fun GetCharWidth(c: Char): SizeT = if (!IsValidChar(c)) 0 else (m_elDesc[m_idxList[c]].width + 1)
    override fun GetCharSize(c: Char): SizeInt = SizeInt(GetCharWidth(c), m_fntHeight)
    override fun GetFontHeight(): SizeT = m_fntHeight

    override fun ComposeChar(
        c: Char,
        surf: iDib,
        pos: IPointInt,
        cmpProps: IiDibFont.ComposeProps,
        layer: IiDibFont.Decor
    ): Int {
        if (c == 0x20.toChar()) {
            return m_elDesc[m_idxList[c]].width + 1
        }

        val stride: SizeT = m_rawFont.size / m_fntHeight
        val bitval = 8 - (m_elDesc[m_idxList[c]].posX and 7)
        var rawFontIdx = m_elDesc[m_idxList[c]].posX ushr 3

        repeat(m_fntHeight) { yy ->
            val lineClr = if (cmpProps.fillType == IiDibFont.FillType.Gradient) {
                cmpProps.gradient.GradValue(yy)
            } else {
                cmpProps.faceColor
            }
            var trawFontIdx = rawFontIdx
            var tbitval = bitval
            (0..m_elDesc[m_idxList[c]].width).forEach { xx ->
                if (tbitval == 0) {
                    ++trawFontIdx
                    tbitval = 8
                }
                if ((m_rawFont[trawFontIdx].toInt() and (1 shl (8 - tbitval))) != 0) {
                    if (layer == IiDibFont.Decor.None) {
                        val ox = pos.x + xx
                        val oy = pos.y + yy
                        if (surf.IsValidPos(ox, oy)) {
                            val pix = if (cmpProps.alpha == 255u.toUByte()) {
                                lineClr
                            } else {
                                BlendPixels(surf.GetPixel(ox, oy), lineClr, cmpProps.alpha)
                            }
                            surf.PutPixel(ox, oy, pix)
                        }
                    } else if (layer == IiDibFont.Decor.Shadow) {
                        val ox = pos.x + xx + 1
                        val oy = pos.y + yy + 1
                        if (surf.IsValidPos(ox, oy)) {
                            val pix = if (cmpProps.alpha == 255u.toUByte()) {
                                cmpProps.borderColor
                            } else {
                                BlendPixels(surf.GetPixel(ox, oy), cmpProps.borderColor, cmpProps.alpha)
                            }
                            surf.PutPixel(ox, oy, pix)
                        }
                    } else if (layer == IiDibFont.Decor.Border) {
                        surf.FillRect(
                            IRectangleInt(pos.x + xx - 1, pos.y + yy - 1, 3, 3),
                            cmpProps.borderColor,
                            cmpProps.alpha
                        )
                    }
                }
                --tbitval
            }
            rawFontIdx += stride
        }

        return m_elDesc[m_idxList[c]].width + 1
    }

    private companion object {

        private operator fun SizeTArray.get(idx: Char) = this[idx.toInt()]
    }
}
