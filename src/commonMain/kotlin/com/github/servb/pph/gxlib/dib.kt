@file:Suppress(
    "FunctionName", "ClassName", "PropertyName", "PrivatePropertyName", "NAME_SHADOWING",
    "LocalVariableName"
)

package com.github.servb.pph.gxlib

import com.github.servb.pph.util.*
import com.soywiz.kmem.clamp
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.Bitmap16
import com.soywiz.korim.color.BGR_565
import com.soywiz.korim.color.ColorFormat16
import com.soywiz.korim.color.RGBA_4444
import com.soywiz.korim.color.RGBA_5551
import com.soywiz.korma.geom.*

fun RGB16(r: Number, g: Number, b: Number): UShort {
    return (((r.toInt() and 0xF8) shl 8) or
            ((g.toInt() and 0xFC) shl 3) or
            ((b.toInt() and 0xF8) ushr 3)).toUShort()
}

fun Darken50(pixel: UInt): UInt {
    return (pixel and 0xF7DEu) shr 1
}

fun Darken25(pixel: UInt): UInt {
    var pixel = (pixel and 0xF7DEu) shr 1
    pixel += (pixel and 0xF7DEu) shr 1
    return pixel
}

val BWPAL = listOf(
    0x0000u, 0x0841u, 0x1082u, 0x18C3u, 0x2104u, 0x2945u, 0x3186u, 0x39C7u,
    0x4208u, 0x4A49u, 0x528Au, 0x5ACBu, 0x630Cu, 0x6B4Du, 0x738Eu, 0x7BCFu,
    0x8410u, 0x8C51u, 0x9492u, 0x9CD3u, 0xA514u, 0xAD55u, 0xB596u, 0xBDD7u,
    0xC618u, 0xCE59u, 0xD69Au, 0xDEDBu, 0xE71Cu, 0xEF5Du, 0xF79Eu, 0xFFDFu,
)

fun DarkenBWBlend(a: UInt, b: UInt): UInt {
    return ((a and 0xF7DEu) shr 1) + ((b and 0xF7DEu) shr 1)
}

fun DarkenBW(pixel: UInt): UInt {
    val chnl = ((pixel shr 6) and 0x1Fu).toInt()
    return DarkenBWBlend(BWPAL[chnl], 0x39E7u)
}

typealias IDibPixel = UShort

// const iDib::pixel *const
interface IDibIPixelIPointer {

    operator fun get(pos: SizeT): IDibPixel
    fun copy(): IDibIPixelPointer
}

// const iDib::pixel*
interface IDibIPixelPointer : IDibIPixelIPointer {

    fun incrementOffset(increment: SizeT)
}

// `iDib::pixel *const`
interface IDibPixelIPointer : IDibIPixelIPointer {

    operator fun set(pos: SizeT, value: IDibPixel)
    override fun copy(): IDibPixelPointer
}

// `iDib::pixel*`
class IDibPixelPointer(private val data: UShortArray, private var offset: SizeT) : IDibIPixelPointer,
    IDibPixelIPointer {

    override fun incrementOffset(increment: SizeT) {
        offset += increment
    }

    override fun get(pos: SizeT): IDibPixel = data[offset + pos]
    override fun copy(): IDibPixelPointer = IDibPixelPointer(data, offset)

    override fun set(pos: SizeT, value: IDibPixel) {
        data[offset + pos] = value
    }
}

private fun RED_MASK(type: IiDib.Type): IDibPixel = when (type) {
    IiDib.Type.RGB -> (0x1Fu shl 11).toUShort()
    IiDib.Type.RGBA -> (0xFu shl 12).toUShort()
    IiDib.Type.RGBCK -> (0x1Fu shl 11).toUShort()
}

private fun GREEN_MASK(type: IiDib.Type): IDibPixel = when (type) {
    IiDib.Type.RGB -> (0x3Fu shl 5).toUShort()
    IiDib.Type.RGBA -> (0xFu shl 8).toUShort()
    IiDib.Type.RGBCK -> (0x1Fu shl 6).toUShort()
}

private fun BLUE_MASK(type: IiDib.Type): IDibPixel = when (type) {
    IiDib.Type.RGB -> (0x1Fu)
    IiDib.Type.RGBA -> (0xFu shl 4).toUShort()
    IiDib.Type.RGBCK -> (0x1Fu shl 1).toUShort()
}

private fun COLOR_MASK(type: IiDib.Type): IDibPixel = when (type) {
    IiDib.Type.RGB -> (0xFFFFu)
    IiDib.Type.RGBA -> (0xFFFu shl 4).toUShort()
    IiDib.Type.RGBCK -> (0x7FFFu shl 1).toUShort()
}

private fun ALPHA_MASK(type: IiDib.Type): IDibPixel = when (type) {
    IiDib.Type.RGB -> 0u
    IiDib.Type.RGBA -> 0xFu
    IiDib.Type.RGBCK -> 0x1u
}

private fun BlitDibBlock_RGB(dst: IDibPixelIPointer, src: IDibIPixelIPointer, size: SizeT) {
    (0 until size).forEach {
        dst[it] = src[it]
    }
}

private fun BlitDibBlock_RGBA(dst: IDibPixelIPointer, src: IDibIPixelIPointer, size: SizeT): Unit = TODO()

private fun BlitDibBlock_RGBCK(dst: IDibPixelIPointer, src: IDibIPixelIPointer, size: SizeT) {
    (0 until size).forEach {
        if ((src[it] and ALPHA_MASK(IiDib.Type.RGBCK)) > 0u) {
            dst[it] = (src[it] and RED_MASK(IiDib.Type.RGBCK)) or
                    (src[it] and GREEN_MASK(IiDib.Type.RGBCK)) or
                    ((src[it] and BLUE_MASK(IiDib.Type.RGBCK)).toInt() shr 1).toUShort()
        }
    }
}

private fun BlitDibBlockAlpha(dst: IDibPixelIPointer, src: IDibIPixelIPointer, a: UByte, size: SizeT): Unit = TODO()

private fun FillDibBlock(dst: IDibPixelIPointer, src: IDibPixel, size: SizeT) {
    (0 until size).forEach {
        dst[it] = src
    }
}

private fun FillDibBlockAlpha(dst: IDibPixelIPointer, src: IDibPixel, a: UByte, size: SizeT) {
    val alpha = a.toUInt()
    val sb = src.toUInt() and 0x1Fu
    val sg = (src.toUInt() shr 5) and 0x3Fu
    val sr = (src.toUInt() shr 11) and 0x1Fu

    (0 until size).forEach { xx ->
        val b = dst[xx].toUInt()
        val db = b and 0x1Fu
        val dg = (b shr 5) and 0x3Fu
        val dr = (b shr 11) and 0x1Fu

        dst[xx] = ((((alpha * (sb - db)) shr 8) + db) or
                ((((alpha * (sg - dg)) shr 8) + dg) shl 5) or
                ((((alpha * (sr - dr)) shr 8) + dr) shl 11)).toUShort()
    }
    // second variant commented in sources:
//    uint8 inv_a = 255-a;
//    uint16 sr = a * ((src & RED_MASK[iDib::RGB]) >> 11);
//    uint16 sg = a * ((src & GREEN_MASK[iDib::RGB]) >> 5);
//    uint16 sb = a * ((src & BLUE_MASK[iDib::RGB]));
//    for (uint32 xx=0; xx<size; ++xx, ++dst) {
//        uint16 dr = inv_a * ((*dst & RED_MASK[iDib::RGB]) >> 11);
//        uint16 dg = inv_a * ((*dst & GREEN_MASK[iDib::RGB]) >> 5);
//        uint16 db = inv_a * ((*dst & BLUE_MASK[iDib::RGB]));
//        *dst = (((sr+dr)>>8)<<11 | ((sg+dg)>>8)<<5 | ((sb+db)>>8));
//    }
}

private fun SetDibPixelAlpha(dst: IDibPixelIPointer, src: IDibPixel, a: UByte) {
    val inv_a = (255u - a).toUByte()
    val sr = (a * ((src and RED_MASK(IiDib.Type.RGB)).toUInt() shr 11)).toUShort()
    val sg = (a * ((src and GREEN_MASK(IiDib.Type.RGB)).toUInt() shr 5)).toUShort()
    val sb = (a * (src and BLUE_MASK(IiDib.Type.RGB))).toUShort()
    val dr = (inv_a * ((dst[0] and RED_MASK(IiDib.Type.RGB)).toUInt() shr 11)).toUShort()
    val dg = (inv_a * ((dst[0] and GREEN_MASK(IiDib.Type.RGB)).toUInt() shr 5)).toUShort()
    val db = (inv_a * (dst[0] and BLUE_MASK(IiDib.Type.RGB))).toUShort()
    dst[0] = ((((sr + dr) shr 8) shl 11) or
            (((sg + dg) shr 8) shl 5) or
            ((sb + db) shr 8)).toUShort()
}

// unused methods in the whole game:
// BlendToDibXY, BlitToDCXY
interface IiDib {

    enum class Type(val colorFormat16: ColorFormat16) {
        RGB(BGR_565),  // todo: why need to swap colors (B<->R)?

        //        RGB(RGB_565),
        RGBA(RGBA_4444),
        RGBCK(RGBA_5551),
    }

    fun GetPixel(x: Int, y: Int): IDibPixel
    fun GetPixel(pos: IPointInt): IDibPixel
    fun GetBuffLen(): SizeT
    fun GetPtr(pos: IPointInt): IDibIPixelPointer
    fun GetPtr(): IDibIPixelPointer
    fun GetType(): Type
    fun IsEmpty(): Boolean
    fun GetSize(): SizeInt
    fun GetWidth(): SizeT
    fun GetHeight(): SizeT

    fun CopyToDibXY(dib: iDib, pos: IPointInt, a: UByte)
    fun CopyToDibXY(dib: iDib, pos: IPointInt)
    fun CopyRectToDibXY(dib: iDib, srect: IRectangleInt, pos: IPointInt, a: UByte = 255u)

    val backingBitmap: Bitmap16
}

// unused methods in the whole game:
// Triangle, Line
class iDib : IiDib {

    private lateinit var m_RGB: Bitmap16
    private lateinit var m_dibType: IiDib.Type

    constructor() {
        m_dibType = IiDib.Type.RGB
    }

    constructor(dib: IiDib) {
        Init(dib)
    }

    constructor(dib: IiDib, rect: IRectangleInt) {
        Init(dib, rect)
    }

    constructor(siz: ISizeInt, dtype: IiDib.Type) {
        Init(siz, dtype)
    }

    fun Init(dib: IiDib) {
        m_dibType = dib.GetType()
        Allocate(dib)
    }

    fun Init(dib: IiDib, rect: IRectangleInt) {
        m_dibType = dib.GetType()
        Allocate(rect.asSize())
        val src_ptr = dib.GetPtr(rect.asPoint())
        val dst_ptr = this.GetPtr()
        (0 until rect.height).forEach {
            (0 until rect.width).forEach { xx ->
                dst_ptr[xx] = src_ptr[xx]
            }
            dst_ptr.incrementOffset(rect.width)
            src_ptr.incrementOffset(dib.GetWidth())
        }
    }

    fun Init(siz: ISizeInt, dtype: IiDib.Type) {
        m_dibType = dtype
        Allocate(siz)
    }

    fun Cleanup() {
        m_RGB = Bitmap16(0, 0)
        m_dibType = IiDib.Type.RGB
    }

    fun Resize(siz: ISizeInt) {
        Cleanup()
        Allocate(siz)
    }

    fun `$destruct`() {
        Cleanup()
    }

    fun setTo(other: IiDib) {
        val clone = iDib(other)
        Swap(clone)
    }

    fun IsValidPos(x: Int, y: Int): Boolean = x in 0 until m_RGB.width && y in 0 until m_RGB.height

    fun PutPixelSafe(x: Int, y: Int, clr: IDibPixel) {
        if (IsValidPos(x, y)) {
            PutPixel(x, y, clr)
        }
    }

    fun PutPixel(x: Int, y: Int, clr: IDibPixel) {
        m_RGB.data[m_RGB.index(x, y)] = clr.toShort()
    }

    fun PutPixel(pos: IPointInt, clr: IDibPixel) = PutPixel(pos.x, pos.y, clr)

    override fun GetPixel(x: Int, y: Int): IDibPixel = m_RGB.data[m_RGB.index(x, y)].toUShort()
    override fun GetPixel(pos: IPointInt): IDibPixel = GetPixel(pos.x, pos.y)
    override fun GetBuffLen(): SizeT = m_RGB.width * m_RGB.width * IDibPixel.SIZE_BYTES
    override fun GetPtr(pos: IPointInt): IDibPixelPointer =
        IDibPixelPointer(m_RGB.data.asUShortArray(), m_RGB.index(pos.x, pos.y))

    override fun GetPtr(): IDibPixelPointer = IDibPixelPointer(m_RGB.data.asUShortArray(), 0)
    override fun GetType(): IiDib.Type = m_dibType
    override fun IsEmpty(): Boolean = m_RGB.data.isEmpty()
    override fun GetSize(): SizeInt = m_RGB.size.asInt()
    override fun GetWidth(): SizeT = m_RGB.width
    override fun GetHeight(): SizeT = m_RGB.height

    fun Fade(a: UByte) {
        if (a == 0u.toUByte()) {
            Fill(cColor.Black.pixel)
        } else if (a != 255u.toUByte()) {
            Fill(cColor.Black.pixel, (255u.toUByte() - a).toUByte())
        }
    }

    fun Fill(clr: IDibPixel, a: UByte = 255u) {
        if (a == 255u.toUByte()) {
            FillDibBlock(GetPtr(), clr, m_RGB.area)
        } else {
            FillDibBlockAlpha(GetPtr(), clr, a, m_RGB.area)
        }
    }

    fun FillRect(rc: IRectangleInt, clr: IDibPixel, a: UByte = 255u) {
        val drect = RectangleInt(rc)
        if (!ClipRect(drect, GetSize().asRectangle())) {
            return
        }
        val dstPtr = GetPtr(drect.asPoint())
        repeat(drect.height) {
            if (a == 255u.toUByte()) {
                FillDibBlock(dstPtr, clr, drect.width)
            } else {
                FillDibBlockAlpha(dstPtr, clr, a, drect.width)
            }
            dstPtr.incrementOffset(GetWidth())
        }
    }

    fun DarkenBWRect(rc: IRectangleInt) {
        val drect = RectangleInt(rc)
        if (!ClipRect(drect, GetSize().asRectangle())) {
            return
        }
        val dstPtr = GetPtr(drect.asPoint())
        repeat(drect.height) {
            (0 until drect.width).forEach { xx ->
                dstPtr[xx] = DarkenBW(dstPtr[xx].toUInt()).toUShort()
            }
            dstPtr.incrementOffset(GetWidth())
        }
    }

    fun Darken50Rect(rc: IRectangleInt) {
        val drect = RectangleInt(rc)
        if (!ClipRect(drect, GetSize().asRectangle())) {
            return
        }
        val dstPtr = GetPtr(drect.asPoint())
        repeat(drect.height) {
            (0 until drect.width).forEach { xx ->
                dstPtr[xx] = Darken50(dstPtr[xx].toUInt()).toUShort()
            }
            dstPtr.incrementOffset(GetWidth())
        }
    }

    fun Darken25Rect(rc: IRectangleInt) {
        val drect = RectangleInt(rc)
        if (!ClipRect(drect, GetSize().asRectangle())) {
            return
        }
        val dstPtr = GetPtr(drect.asPoint())
        repeat(drect.height) {
            (0 until drect.width).forEach { xx ->
                dstPtr[xx] = Darken25(dstPtr[xx].toUInt()).toUShort()
            }
            dstPtr.incrementOffset(GetWidth())
        }
    }

    fun HGradientRect(rc: IRectangleInt, c1: IDibPixel, c2: IDibPixel): Unit = TODO()

    fun VGradientRect(rc: IRectangleInt, c1: IDibPixel, c2: IDibPixel): Unit = TODO()

    fun FrameRect(rc: IRectangleInt, clr: IDibPixel, a: UByte = 255u) {
        val drect = RectangleInt(rc)
        if (!ClipRect(drect, GetSize().asRectangle())) {
            return
        }
        HLine(rc.topLeft2, rc.x2, clr, a)
        HLine(rc.bottomLeft2, rc.x2, clr, a)
        VLine(rc.topLeft2, rc.y2, clr, a)
        VLine(rc.topRight2, rc.y2, clr, a)
    }

    fun HLine(pos: IPointInt, x2: Int, clr: IDibPixel, a: UByte = 255u) {
        val dpos1 = PointInt(minOf(pos.x, x2), pos.y)
        val dx2 = Mutable(maxOf(pos.x, x2))
        if (!ClipHLine(dpos1, dx2, GetSize().asRectangle())) {
            return
        }
        val len = dx2.element - dpos1.x + 1
        val dstPtr = GetPtr(dpos1)
        if (a == 255u.toUByte()) {
            FillDibBlock(dstPtr, clr, len)
        } else {
            FillDibBlockAlpha(dstPtr, clr, a, len)
        }
    }

    fun VLine(pos: IPointInt, y2: Int, clr: IDibPixel, a: UByte = 255u) {
        val dpos1 = PointInt(pos.x, minOf(pos.y, y2))
        val dy2 = Mutable(maxOf(pos.y, y2))
        if (!ClipVLine(dpos1, dy2, GetSize().asRectangle())) {
            return
        }
        val dstPtr = GetPtr(dpos1)
        val h = dy2.element - dpos1.y
        repeat(h) {
            if (a == 255u.toUByte()) {
                dstPtr[0] = clr
            } else {
                SetDibPixelAlpha(dstPtr, clr, a)
            }
            dstPtr.incrementOffset(GetWidth())
        }
    }

    private fun Swap(other: iDib) {
        this.m_RGB = other.m_RGB.also { other.m_RGB = this.m_RGB }
        this.m_dibType = other.m_dibType.also { other.m_dibType = this.m_dibType }
    }

    private fun Allocate(siz: ISizeInt) {
        m_RGB = Bitmap16(siz.width, siz.height, format = m_dibType.colorFormat16)
    }

    private fun Allocate(dib: IiDib) {
        m_RGB = dib.backingBitmap.clone()
    }

    override fun CopyToDibXY(dib: iDib, pos: IPointInt, a: UByte) = TODO()

    override fun CopyToDibXY(dib: iDib, pos: IPointInt) = TODO()

    override fun CopyRectToDibXY(dib: iDib, srect: IRectangleInt, pos: IPointInt, a: UByte) {
        val src_rect = RectangleInt(srect)
        val siz = SizeInt(dib.GetWidth() - pos.x, dib.GetHeight() - pos.y)
        val dst_rect = RectangleInt(pos.x, pos.y, siz.width, siz.height)
        if (!iClipRectRect(dst_rect, dib.GetSize().asRectangle(), src_rect, GetSize().asRectangle())) {
            return
        }

        val src_clr = IDibPixelPointer(m_RGB.data.asUShortArray(), m_RGB.index(src_rect.x, src_rect.y))
        val dst_clr = IDibPixelPointer(dib.m_RGB.data.asUShortArray(), dib.m_RGB.index(dst_rect.x, dst_rect.y))

        if (a == 255u.toUByte()) {
            repeat(dst_rect.height) { yy ->
                when (m_dibType) {
                    IiDib.Type.RGB -> BlitDibBlock_RGB(dst_clr, src_clr, dst_rect.width)
                    IiDib.Type.RGBA -> BlitDibBlock_RGBA(dst_clr, src_clr, dst_rect.width)
                    IiDib.Type.RGBCK -> BlitDibBlock_RGBCK(dst_clr, src_clr, dst_rect.width)
                }

                src_clr.incrementOffset(m_RGB.width)
                dst_clr.incrementOffset(dib.GetWidth())
            }
        } else {
            repeat(dst_rect.height) { yy ->
                BlitDibBlockAlpha(dst_clr, src_clr, a, dst_rect.width)
                src_clr.incrementOffset(m_RGB.width)
                dst_clr.incrementOffset(dib.GetWidth())
            }
        }
    }

    override val backingBitmap: Bitmap16 get() = m_RGB
}

fun BlendPixels(dst: IDibPixel, src: IDibPixel, a: UByte): IDibPixel {
    val invA = 255u - a
    val sr = a * ((src.toUInt() and (0x1Fu shl 11)) shr 11)
    val sg = a * ((src.toUInt() and (0x3Fu shl 5)) shr 5)
    val sb = a * ((src.toUInt() and 0x1Fu))
    val dr = invA * ((dst.toUInt() and (0x1Fu shl 11)) shr 11)
    val dg = invA * ((dst.toUInt() and (0x3Fu shl 5)) shr 5)
    val db = invA * ((dst.toUInt() and 0x1Fu))
    return ((((sr + dr) shr 8) shl 11) or
            (((sg + dg) shr 8) shl 5) or
            ((sb + db) shr 8)).toUShort()
}

enum class cColor(val pixel: IDibPixel) {

    White(0xFFFFu),
    Black(0x0000u),
    Gray192(0xC618u),
    Gray128(0x8410u),
    Gray64(0x4208u),
    Red(0xF800u),
    Green64(0x0200u),
    Green128(0x03E0u),
    Green192(0x05E0u),
    Green(0x07E0u),
    Blue(0x001Fu),
    Blue64(0x0008u),
    Blue128(0x0010u),
    Blue192(0x0018u),
    Yello(0xFFE0u),
    Yello192(0xBDE0u),
}

// immutable
class iGradient {

    private val m_pGrad: IDibIPixelIPointer
    private val m_cnt: SizeT

    constructor(pGrad: IDibIPixelIPointer, cnt: SizeT) {
        m_pGrad = pGrad
        m_cnt = cnt
    }

    constructor(other: iGradient) {
        m_pGrad = other.m_pGrad
        m_cnt = other.m_cnt
    }

    fun Count(): SizeT = m_cnt
    fun GradValue(pos: SizeT): IDibPixel {
        if (!IsValid()) {
            return 0u
        }
        return m_pGrad[pos.clamp(0, m_cnt - 1)]
    }

    fun IsValid(): Boolean = m_cnt > 0
}

interface IiPalette {

    operator fun get(nIndex: SizeT): IDibPixel
}

class iPalette : IiPalette {

    private val m_Palette = UShortArray(256)

    constructor()

    constructor(pal: IDibIPixelIPointer) {
        Init(pal)
    }

    fun Init(pal: IDibIPixelIPointer) {
        m_Palette.indices.forEach {
            m_Palette[it] = pal[it]
        }
    }

    override fun get(nIndex: SizeT): IDibPixel {
        check(nIndex in m_Palette.indices)
        return m_Palette[nIndex]
    }

    operator fun set(nIndex: SizeT, value: IDibPixel) {
        check(nIndex in m_Palette.indices)
        m_Palette[nIndex] = value
    }

    fun GetPtr(): IDibPixelPointer {
        return IDibPixelPointer(m_Palette, 0)
    }
}

// iPaletteDib – skipped – maybe it's not needed

// skipped as unused – Rotate
object iDibTransform {

    fun FastStretch(src: IiDib, dst: iDib, boffs: SizeT) {
        val pDst = dst.GetPtr()
        (0 until dst.GetHeight()).forEach { yy ->
            val pSrc = src.GetPtr(IPointInt(0, yy shl boffs))
            (0 until dst.GetWidth()).forEach { xx ->
                pDst[0] = pSrc[0]
                pSrc.incrementOffset(1 shl boffs)
                pDst.incrementOffset(1)
            }
        }
    }

    fun PyramidalStretch(src: IiDib, dst: iDib, boffs: SizeT): Unit = TODO()
}

class iDibReaderImpl  // todo

interface IiDibReader {

    enum class IMG_TYPE {
        IT_UNKNOWN,
        IT_BMP,
        IT_GIF,
        IT_JPG,
    }

    fun FromFile(dib: iDib, fname: String): Boolean
    fun FromStream(dib: iDib, pFile: Nothing?, it: IMG_TYPE): Boolean
    // FromFile, FromStream for iPaletteDib – skipped – maybe it's not needed
}

class iDibReader : IiDibReader {

    private val pimpl: iDibReaderImpl by lazy { TODO() }

    //    constructor()
    fun `$destruct`(): Unit = TODO()
    fun Init(): Boolean = TODO()
    fun Cleanup(): Unit = TODO()
    override fun FromFile(dib: iDib, fname: String): Boolean = TODO()
    override fun FromStream(dib: iDib, pFile: Nothing?, it: IiDibReader.IMG_TYPE): Boolean = TODO()
}

fun SaveDibBitmap32(dib: IiDib, fname: String): Boolean = TODO()
fun SaveDibBitmap16(dib: IiDib, fname: String): Boolean = TODO()

fun Bitmap.copyTo(dib: iDib) {
    this.copy(0, 0, dib.backingBitmap, 0, 0, this.width, this.height)
}
