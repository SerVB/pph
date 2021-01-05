package com.github.servb.pph.pheroes.Game

import com.github.servb.pph.gxlib.IDibPixelIPointer
import com.github.servb.pph.gxlib.IiDib
import com.github.servb.pph.gxlib.copyTo
import com.github.servb.pph.gxlib.iDib
import com.github.servb.pph.util.ISizeInt
import com.github.servb.pph.util.SizeT
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.color.convertTo
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.PointInt
import kotlin.properties.Delegates

private const val R16MASK: UInt = 0xF800u
private const val G16MASK: UInt = 0x07E0u
private const val B16MASK: UInt = 0x001Fu

private val RB16MASK: UInt = R16MASK or B16MASK
private const val RB16ROUND: UInt = 0x8010u
private const val G16ROUND: UInt = 0u  // 0x0400u  // commented in sources

private fun blend16fast(p1: UInt, p2: UInt, a: UInt): UInt {
    val rbdst = p2 and RB16MASK
    var res = (rbdst + ((((p1 and RB16MASK) - rbdst) * a + RB16ROUND) shr 5)) and RB16MASK
    val gdst = p2 and G16MASK
    res = res or ((gdst + ((((p1 and G16MASK) - gdst) * a + G16ROUND) shr 5)) and G16MASK)
    return res
}

private fun blit_16aline(src: Bitmap32, srcOffset: Int, dst: IDibPixelIPointer, npix: SizeT): Int {
    var srcOffset = srcOffset
    val pdst = dst.copy()
    repeat(npix) {
        val srcColor = src.data[srcOffset]
        val srcAlpha = (srcColor.a.toUInt() shr 3) and 0x1Fu
        val srcRGB565 = RGBA.convertTo(srcColor.value, IiDib.Type.RGB.colorFormat16)

        pdst[0] = blend16fast(srcRGB565.toUInt(), pdst[0].toUInt(), srcAlpha).toUShort()

        ++srcOffset
        pdst.incrementOffset(1)
    }
    return srcOffset
}

// InitMotionBlur, InitBump, InitLight and related are deleted as unused in sources
class iCreditsComposer {

    private val m_back: iDib = iDib()
    private var m_pos: SizeT by Delegates.notNull()
    private lateinit var m_logo: Bitmap32

    private var m_bEnd: Boolean = false
    private var m_scrPos: Int = 0
    private var m_bShowCredits: Boolean
    private var t: Double by Delegates.notNull()
    private var dt: Double by Delegates.notNull()

    constructor() {
        m_bShowCredits = false
    }

    suspend fun Init() {
        m_back.Init(ISizeInt(320, 720), IiDib.Type.RGB)
        resourcesVfs["pheroes/bin/Resources/hmm/GFX/Pix/MenuBack.png"].readBitmap().copyTo(m_back)

        m_logo = resourcesVfs["pheroes/bin/Resources/hmm/GFX/Pix/title_hmm.png"].readBitmap().toBMP32()

        m_pos = 0
        t = 0.0
        dt = 0.03  // adjust this if too fast or slow...
    }

    fun Compose(surface: iDib) {
        val xval = ((m_pos++) / 2) % 720

        if (xval < 240) {
            m_back.CopyRectToDibXY(surface, IRectangleInt(0, 720 - xval, 320, xval), PointInt())
            m_back.CopyRectToDibXY(surface, IRectangleInt(0, 0, 320, 240 - xval), IPointInt(0, xval))
        } else {
            m_back.CopyRectToDibXY(surface, IRectangleInt(0, 720 - xval, 320, 240), PointInt())
        }

        t += dt

        //Compose credits
        ++m_scrPos
        val composed: SizeT = 0
        if (m_bShowCredits) {
            // todo: port code of text drawing
        }

        if (composed == 0) {
            m_bEnd
        }

        // compose 'Heroes' logo directly on surface  // todo: won't work on other resolutions
        var srcOffset = 0
        val dst = surface.GetPtr()
        // magic position constant ;)
        dst.incrementOffset(320 * 4 + 45)
        repeat(m_logo.height) {
            srcOffset = blit_16aline(m_logo, srcOffset, dst, m_logo.width)
            dst.incrementOffset(320)
        }
    }

    fun StartCredits() {
        check(!m_bShowCredits)
        m_bEnd = false
        m_bShowCredits = true
        m_scrPos = 15
    }

    fun StopCredits() {
        check(m_bShowCredits)
        m_bShowCredits = false
    }

    fun IsCreaditsStarted(): Boolean = m_bShowCredits

    fun IsCreaditsEnd(): Boolean = m_bEnd
}
