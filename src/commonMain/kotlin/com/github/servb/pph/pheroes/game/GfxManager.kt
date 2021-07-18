package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.mapEditor.exportSprites
import com.github.servb.pph.util.*
import com.soywiz.korio.stream.MemorySyncStream
import com.soywiz.korio.stream.readBytesExact
import com.soywiz.korio.stream.readShortArrayLE
import com.soywiz.korma.geom.*
import rootVfs

typealias SpriteId = SizeT
typealias BankId = SizeT

class iGfxManager {

    companion object {

        const val MaxSpriteBanks = 16
        const val MaxDynItems = 32

        // Helper to make SpriteUID from BankId + Sprite number
        // SpriteId explanation
        // SpriteId is an unique sprite identifier
        // It composed of Sprite Bank Identifier + Number of sprite in the bank
        // SpriteID = uint32
        // Upper 16 bit is a bank number
        // Lower 16 bit is a sprite number
        // [...BankID : SpriteNum ]
        fun MakeSpriteId(cat: BankId, sn: SizeT): SpriteId {
            check(cat < MaxSpriteBanks)
            return sn + (cat shl 16)
        }

        fun GetFlippedSpriteId(sid: SpriteId): SpriteId {
            return sid or (1 shl 31)
        }

        fun IsFlippedSprite(sid: SpriteId): Boolean = (sid and (1 shl 31)) == 1
    }

    enum class Effects {

        None,
        Transparent,
        ShadowIso,
        Shadow2D,
        Wired,
    }

    // 4*4 = 16 b per sprite
    class Sprite(
        val type_: UByte,
        val offset_: SizeT,  // offset from the start of data block ( in uin16 / pixels )
        val sizeW_: UShort,
        val sizeH_: UShort,
        val originX_: Short,
        val originY_: Short,
    )

    class SpriteBank {

        var dataSize: SizeT  // for statistics
        val props: MutableList<Sprite> = mutableListOf()
        lateinit var data: ShortArray

        fun GetSprite(num: SizeT): Sprite {
            check(num < 65536)  // just in case someone pass full sprite id
            check(num < props.size)
            return props[num]
        }

        fun Data(offset: SizeT): IDibIPixelPointer {
            return IDibPixelPointer(data.asUShortArray(), offset)
        }

        constructor() {
            dataSize = 0
        }

        lateinit var fileName: String

        suspend fun Load(fileName: String, gammaLevel: UInt): Boolean {
            this.fileName = fileName

            val gfxData = exportSprites(rootVfs[fileName])

            val stream = MemorySyncStream(gfxData)

            val bankHeaderMagicId: UInt = stream.ReadU32()
            val bankHeaderNumSprites: SizeT = stream.ReadS32()
            val bankHeaderDataLength: SizeT = stream.ReadS32()  // in bytes!!!
            val bankHeaderReserved: UInt = stream.ReadU32()

            data = stream.readShortArrayLE(bankHeaderDataLength / 2)
            dataSize = bankHeaderDataLength

            stream.readBytesExact(16)  // seek 16 bytes: skip header

            repeat(bankHeaderNumSprites) {
                val sprite = Sprite(
                    type_ = stream.ReadU8(),
                    offset_ = stream.ReadS32(),
                    sizeW_ = stream.ReadU16(),
                    sizeH_ = stream.ReadU16(),
                    originX_ = stream.ReadS16(),
                    originY_ = stream.ReadS16(),
                )

                props.add(sprite)
            }

            if (gammaLevel != 0u) {
                TODO()
            }

            return true
        }

        fun Unload() {
            props.clear()
            data = ShortArray(0)
        }

        suspend fun Reload(gammaLevel: UInt): Boolean {
            Unload()
            return Load(fileName, gammaLevel)
        }
    }

    class DynItem  // todo

    private var gammaLevel: UInt
    private val dynItems: Array<DynItem> = emptyArray()  // size == MaxDynItems  // todo
    private val dynPos: SizeT
    private val bank_: Array<SpriteBank> = Array(MaxSpriteBanks) { SpriteBank() }  // size == MaxSpriteBanks

    constructor() {
        dynPos = 0
        gammaLevel = 0u
    }

    fun SetGamma(glevel: UInt) {
        gammaLevel = glevel
    }

    suspend fun Load(cat: BankId, fileName: String): Boolean {
        check(cat < MaxSpriteBanks)
        if (cat >= MaxSpriteBanks) {
            return false
        }

        bank_[cat].Unload()
        return bank_[cat].Load(fileName, gammaLevel)
    }

    fun Reload(): Unit = TODO()

    fun NumSprites(cat: BankId): SizeT {
        check(cat < MaxSpriteBanks)
        if (cat >= MaxSpriteBanks) {
            return 0
        }

        return bank_[cat].props.size
    }

    fun FirstId(cat: BankId): SpriteId {
        check(cat < MaxSpriteBanks)
        if (cat >= MaxSpriteBanks) {
            return 0
        }

        return MakeSpriteId(cat, 0)
    }

    fun EndId(cat: BankId): SpriteId {
        check(cat < MaxSpriteBanks)
        if (cat >= MaxSpriteBanks) {
            return 0
        }

        return MakeSpriteId(cat, bank_[cat].props.size)
    }

    fun Dimension(sid: SpriteId): SizeInt {
        val catId = sid ushr 16
        val sprId = sid and 0xFFFF

        check(catId < MaxSpriteBanks)
        check(sprId < bank_[catId].props.size)

        val sprPtr = bank_[catId].GetSprite(sprId)

        return SizeInt(sprPtr.sizeW_.toInt(), sprPtr.sizeH_.toInt())
    }

    fun Anchor(sid: SpriteId): PointInt {
        val catId = sid ushr 16
        val sprId = sid and 0xFFFF

        check(catId < MaxSpriteBanks)
        check(sprId < bank_[catId].props.size)

        val sprPtr = bank_[catId].GetSprite(sprId)

        return PointInt(sprPtr.originX_.toInt(), sprPtr.originY_.toInt())
    }

    fun Shadow(sid: SpriteId): UByte = TODO()

    fun Blit(sid: SpriteId, to: iDib, pos: IPointInt) {
        val catId = sid ushr 16
        val sprId = sid and 0xFFFF

        check(catId < MaxSpriteBanks)
        check(sprId < bank_[catId].props.size)

        val sprPtr = bank_[catId].GetSprite(sprId)
        val pixels = bank_[catId].Data(sprPtr.offset_)

        val src_rect = IRectangleInt(0, 0, sprPtr.sizeW_.toInt(), sprPtr.sizeH_.toInt())
        BlitNormal(sprPtr, pixels, to, src_rect, pos)
    }

    fun Blit(sid: SpriteId, to: iDib, src: IRectangleInt, pos: IPointInt) {
        val catId = sid ushr 16
        val sprId = sid and 0xFFFF

        check(catId < MaxSpriteBanks)
        check(sprId < bank_[catId].props.size)

        val sprPtr = bank_[catId].GetSprite(sprId)
        val pixels = bank_[catId].Data(sprPtr.offset_)

        BlitNormal(sprPtr, pixels, to, src, pos)
    }

    // alpha - 0-63
    fun BlitAlpha(sid: SpriteId, to: iDib, pos: IPointInt, a: UByte) {
        // All other effects works only for span-sprites
        val catId = sid ushr 16
        val sprId = sid and 0xFFFF

        check(catId < MaxSpriteBanks)
        check(sprId < bank_[catId].props.size)

        val sprPtr = bank_[catId].GetSprite(sprId)
        val pixels = bank_[catId].Data(sprPtr.offset_)
        val src_rect = IRectangleInt(0, 0, sprPtr.sizeW_.toInt(), sprPtr.sizeH_.toInt())

        if (a == 63u.toUByte()) {
            BlitNormal(sprPtr, pixels, to, src_rect, pos)
        } else {
            BlitAlpha(sprPtr, pixels, to, src_rect, pos, a)
        }
    }

    // alpha - 0-63
    fun BlitAlpha(sid: SpriteId, to: iDib, src: IRectangleInt, pos: IPointInt, a: UByte): Unit = TODO()

    fun BlitEffect(sid: SpriteId, to: iDib, pos: IPointInt, efx: Effects) {
        // All other effects works only for span-sprites
        val catId = sid ushr 16
        val sprId = sid and 0xFFFF

        check(catId < MaxSpriteBanks)
        check(sprId < bank_[catId].props.size)

        val sprPtr = bank_[catId].GetSprite(sprId)
        val pixels = bank_[catId].Data(sprPtr.offset_)

        when (efx) {
            Effects.Transparent -> BlitTransparent(sprPtr, pixels, to, pos)
            Effects.ShadowIso -> BlitShadowIso(sprPtr, pixels, to, pos)
            Effects.Shadow2D -> BlitShadow2D(sprPtr, pixels, to, pos)
            Effects.Wired -> TODO()
            Effects.None -> {
                check(false) { "Just in case" }
                Blit(sid, to, pos)
            }
        }
    }

    fun BlitTile(sid: SpriteId, to: iDib, dst: IRectangleInt) {
        BlitTile(sid, to, Dimension(sid).asRectangle(), dst)
    }

    fun BlitTile(sid: SpriteId, to: iDib, src: IRectangleInt, dst: IRectangleInt) {
        if (src.isEmpty) {
            return
        }

        // Tile Dib
        var numx = dst.width / src.width
        var numy = dst.height / src.height
        if ((dst.width % src.width) != 0) {
            ++numx
        }
        if ((dst.height % src.height) != 0) {
            ++numy
        }

        repeat(numy) { yy ->
            repeat(numx) { xx ->
                val w = minOf(src.width, dst.width - xx * src.width)
                val h = minOf(src.height, dst.height - yy * src.height)
                Blit(
                    sid,
                    to,
                    IRectangleInt(src.x, src.y, w, h),
                    IPointInt(dst.x + xx * src.width, dst.y + yy * src.height)
                )
            }
        }
    }

    fun BlitMasked(sid: SpriteId, mid: SpriteId, to: iDib, pos: IPointInt) {
        val catSId = sid ushr 16
        val sprSId = sid and 0xFFFF

        check(catSId < MaxSpriteBanks)
        check(sprSId < bank_[catSId].props.size)

        val catMId = mid ushr 16
        val sprMId = mid and 0xFFFF

        check(catMId < MaxSpriteBanks)
        check(sprMId < bank_[catMId].props.size)

        val sprPtr = bank_[catSId].GetSprite(sprSId)
        val sprPixels = bank_[catSId].Data(sprPtr.offset_)

        val mskPtr = bank_[catMId].GetSprite(sprMId)
        val mskPixels = bank_[catMId].Data(mskPtr.offset_)

        check((sprPtr.type_ and 1u) == 0u.toUByte()) { "sprite should be RAW" }
        check((mskPtr.type_ and 1u) == 1u.toUByte()) { "while mask is spanned" }

        val sprOrigin = IPointInt(sprPtr.originX_.toInt(), sprPtr.originY_.toInt())
        val sprSize = ISizeInt(sprPtr.sizeW_.toInt(), sprPtr.sizeH_.toInt())

        // correct sprite position according to anchor
        val pos = pos + sprOrigin

        val src_rect = RectangleInt(0, 0, sprSize.width, sprSize.height)
        val dst_rect = RectangleInt(pos.x, pos.y, sprSize.width, sprSize.height)  // ? was 'siz' = to.Size - pos
        if (!iClipRectRect(dst_rect, to.GetSize().asRectangle(), src_rect, sprSize.asRectangle())) {
            return
        }

        // calc dest ptr
        val dptr = to.GetPtr(dst_rect.asPoint())
        val sptr = sprPixels.copy()
        // <*1*>
        val mptr = mskPixels.copy(1)
        val eptr = mskPixels.copy(mskPixels[0].toInt() + 1)
        val dstStride = to.GetWidth()
        val srcStride = sprSize.width

        // Spanned sprite
        val op = CopyOp

        if (src_rect.asSize() == sprSize) {
            // sprite non clipped - use lighting fast blit
            while (mptr.offset != eptr.offset) {
                mptr.incrementOffset(MaskedSpanFast(op, mptr, sptr, dptr))
                dptr.incrementOffset(dstStride)
                sptr.incrementOffset(srcStride)
            }
        } else {
            // sprite clipped - full blown blit
            dptr.incrementOffset(-src_rect.x)

            val toSkip = src_rect.y
            sptr.incrementOffset(sprSize.width * toSkip)
            repeat(toSkip) {
                mptr.incrementOffset(SpanSkip(mptr))
            }

            val clipIn = dptr.copy(src_rect.x)
            repeat(src_rect.height) {
                mptr.incrementOffset(MaskedSpan(op, mptr, sptr, dptr, clipIn, clipIn.copy(src_rect.width)))
                dptr.incrementOffset(dstStride)
                clipIn.incrementOffset(dstStride)
                sptr.incrementOffset(srcStride)
            }
        }
    }

    fun MakeDib(sid: SpriteId, dst: iDib): Unit = TODO()

    private fun FlipSprite(sid: SpriteId): Unit = TODO()

    private fun BlitNormal(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, src: IRectangleInt, dst: IPointInt) {
        val pos = PointInt(dst)
        val origin = PointInt(spr.originX_.toInt(), spr.originY_.toInt())
        val size = SizeInt(spr.sizeW_.toInt(), spr.sizeH_.toInt())

        // correct sprite position according to anchor
        pos.x += origin.x
        pos.y += origin.y

        // check bounds & clip
        // __NOT_NECESSARY__ if ( (pos.x + (sint32)size.w) <= 0 || (pos.y + (sint32)size.h) <= 0) return;
        val src_rect = RectangleInt(src)
        //iSize siz( srf.GetWidth() - pos.x, srf.GetHeight() - pos.y);
        //iRect dst_rect(pos,siz);
        //__SAME_AS__
        val dst_rect = RectangleInt(pos.x, pos.y, size.width, size.height)
        if (!iClipRectRect(dst_rect, srf.GetSize().asRectangle(), src_rect, size.asRectangle())) {
            return
        }

        // calc dest ptr
        val dst_clr = srf.GetPtr(dst_rect.asPoint())
        val dstStride = srf.GetWidth()

        // blit
        if ((spr.type_ and 1u) != 0u.toUByte()) {
            // Spanned sprite
            val op = CopyOp
            // ptr, dst_clr, dstStrude, src_rect(.x .y .w, .h )

            // <*1*>
            val endIdx = data.offset + data[0].toInt() + 1
            val ptr = data.copy(1)

            if (src_rect.asSize() == size) {
                // sprite non clipped - use lighting fast blit
                while (ptr.offset != endIdx) {
                    ptr.incrementOffset(SpanFast(op, ptr, dst_clr))
                    dst_clr.incrementOffset(dstStride)
                }
            } else {
                // sprite clipped - full blown blit
                dst_clr.incrementOffset(-src_rect.x)

                val toSkip = src_rect.y
                repeat(toSkip) {
                    ptr.incrementOffset(SpanSkip(ptr))
                }

                val clipIn = dst_clr.copy(src_rect.x)
                repeat(src_rect.height) {
                    ptr.incrementOffset(Span(op, ptr, dst_clr, clipIn, clipIn.copy(src_rect.width)))
                    dst_clr.incrementOffset(dstStride)
                    clipIn.incrementOffset(dstStride)
                }
            }
        } else {
            // Raw sprite
            val src_clr = data.copy(src_rect.y * size.width + src_rect.x)

            repeat(dst_rect.height) {
                //BlitDibBlock_RGB(dst_clr,src_clr,dst_rect.w);  // commented in sources
                repeat(dst_rect.width) { xx ->
                    dst_clr[xx] = src_clr[xx]
                }
                src_clr.incrementOffset(size.width)
                dst_clr.incrementOffset(dstStride)
            }
        }
    }

    private fun BlitAlpha(
        sprPtr: Sprite,
        pixels: IDibIPixelIPointer,
        srf: iDib,
        src: IRectangleInt,
        pos: IPointInt,
        a: UByte
    ) {
        // return if fully transparent
        if (a == 0u.toUByte()) {
            return
        }

        val origin = IPointInt(sprPtr.originX_.toInt(), sprPtr.originY_.toInt())
        val size = ISizeInt(sprPtr.sizeW_.toInt(), sprPtr.sizeH_.toInt())

        // correct sprite position according to anchor
        val pos = PointInt(pos)
        pos += origin

        // check bounds & clip
        // __NOT_NECESSARY__ if ( (pos.x + (sint32)size.w) <= 0 || (pos.y + (sint32)size.h) <= 0) return;
        val src_rect = RectangleInt(src)
        //iSize siz( srf.GetWidth() - pos.x, srf.GetHeight() - pos.y);
        //iRect dst_rect(pos,siz);
        //__SAME_AS__
        val dst_rect = RectangleInt(pos.x, pos.y, size.width, size.height)
        if (!iClipRectRect(dst_rect, srf.GetSize().asRectangle(), src_rect, size.asRectangle())) {
            return
        }

        // calc dest ptr
        val dst_clr = srf.GetPtr(dst_rect.asPoint())
        val dstStride = srf.GetWidth()

        // blit
        if ((sprPtr.type_.toUInt() and 1u) != 0u) {
            // Spanned sprite
            val op = ConstAlphaBlendOp(a)
            // ptr, dst_clr, dstStrude, src_rect(.x .y .w, .h )

            // <*1*>
            val eptr = pixels.copy(1 + pixels[0].toInt())
            val ptr = pixels.copy(1)

            if (src_rect.asSize() == size) {
                // sprite non clipped - use lighting fast blit
                while (ptr.offset != eptr.offset) {
                    ptr.incrementOffset(SpanFast(op, ptr, dst_clr))
                    dst_clr.incrementOffset(dstStride)
                }
            } else {
                // sprite clipped - full blown blit
                dst_clr.incrementOffset(-src_rect.x)

                val toSkip = src_rect.x
                repeat(toSkip) {
                    ptr.incrementOffset(SpanSkip(ptr))
                }

                val clipIn = dst_clr.copy(src_rect.x)
                repeat(src_rect.height) {
                    ptr.incrementOffset(Span(op, ptr, dst_clr, clipIn, clipIn.copy(src_rect.width)))
                    dst_clr.incrementOffset(dstStride)
                    clipIn.incrementOffset(dstStride)
                }
            }
        } else {
            // Raw sprite
            val src_clr = pixels.copy(src_rect.y * size.width + src_rect.x)

            repeat(dst_rect.height) {
                //BlitDibBlock_RGB(dst_clr,src_clr,dst_rect.w);  // commented in sources
                repeat(dst_rect.width) { xx ->
                    dst_clr[xx] = src_clr[xx]
                }
                src_clr.incrementOffset(size.width)
                dst_clr.incrementOffset(dstStride)
            }
        }
    }

    private fun BlitTransparent(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, dst: IPointInt) {
        if (0u.toUByte() == (spr.type_ and 1u)) {
            check(false)
            return
        }

        val origin = PointInt(spr.originX_.toInt(), spr.originY_.toInt())
        val size = SizeInt(spr.sizeW_.toInt(), spr.sizeH_.toInt())

        // correct sprite position according to anchor
        val pos = dst + origin

        val src_rect = RectangleInt(size.asRectangle())
        val dst_rect = RectangleInt(pos.x, pos.y, size.width, size.height)
        if (!iClipRectRect(dst_rect, srf.GetSize().asRectangle(), src_rect, size.asRectangle())) {
            return
        }

        // calc dest ptr
        val dst_clr = srf.GetPtr(dst_rect.asPoint())
        val dstStride = srf.GetWidth()

        val op = Shadow25Op

        // <*1*>
        val eptr = data.copy(data[0].toInt() + 1)
        val ptr = data.copy(1)

        if (src_rect.asSize() == size) {
            // sprite non clipped - use lighting fast blit
            while (ptr.offset != eptr.offset) {
                ptr.incrementOffset(SpanFast(op, ptr, dst_clr))
                dst_clr.incrementOffset(dstStride)
            }
        } else {
            // sprite clipped - full blown blit
            dst_clr.incrementOffset(-src_rect.x)

            val toSkip = src_rect.y
            repeat(toSkip) {
                ptr.incrementOffset(SpanSkip(ptr))
            }

            val clipIn = dst_clr.copy(src_rect.x)
            repeat(src_rect.height) {
                ptr.incrementOffset(Span(op, ptr, dst_clr, clipIn, clipIn.copy(src_rect.width)))
                dst_clr.incrementOffset(dstStride)
                clipIn.incrementOffset(dstStride)
            }
        }
    }

    private fun BlitShadowIso(sprPtr: Sprite, pixels: IDibIPixelIPointer, srf: iDib, pos: IPointInt) {
        check(1u.toUByte() == (sprPtr.type_ and 1u)) { "sprite must be spanned" }

        val origin = PointInt(sprPtr.originX_.toInt(), sprPtr.originY_.toInt())
        val opos = origin + pos

        // sprite rect -> sprite shadow rect
        val sprSz = SizeInt(sprPtr.sizeW_.toInt(), sprPtr.sizeH_.toInt())
        val hhgt: SizeT = (sprPtr.sizeH_.toInt() + 1) / 2
        check(hhgt > 1)
        opos.x -= hhgt
        opos.y += sprSz.height - hhgt - 1
        sprSz.width += hhgt - 1
        sprSz.height -= hhgt

        if (opos.x + sprSz.width <= 0 || opos.y + sprSz.height <= 0) {
            return
        }
        val src_rect = RectangleInt(0, 0, sprSz.width, sprSz.height)
        val siz = SizeInt(srf.GetWidth() - pos.x, srf.GetHeight() - pos.y)
        val dst_rect = RectangleInt(opos.x, opos.y, siz.width, siz.height)
        if (!iClipRectRect(dst_rect, srf.GetSize().asRectangle(), src_rect, sprSz.asRectangle())) {
            return
        }

        val dst_clr = srf.GetPtr(dst_rect.asPoint())

        val ptr = pixels.copy(1)
        val eptr = pixels.copy(1 + pixels[0].toInt())
        val dstStride = srf.GetWidth()

        val op = Shadow50Op

        if (src_rect.asSize() == sprSz) {
            while (ptr.offset != eptr.offset) {
                //ptr = ProcessShadowSpanLine(ptr, dst_clr);  // commented in sources
                ptr.incrementOffset(SpanFast(op, ptr, dst_clr))
                dst_clr.incrementOffset(1)  // shadow skew
                dst_clr.incrementOffset(dstStride)
                //if ( ptr != eptr ) ptr = SkipSpanLine(ptr);  // shadow skip  // commented in sources
                if (ptr.offset != eptr.offset) {
                    ptr.incrementOffset(SpanSkip(ptr))  // shadow skip
                }
            }
        } else {
            dst_clr.incrementOffset(-src_rect.x)

            val toSkip = src_rect.x
            repeat(toSkip) {
                //ptr = SkipSpanLine(ptr);  // commented in sources
                ptr.incrementOffset(SpanSkip(ptr))
//                check(ptr.offset != eptr.offset) { "skip by factor of two" }
                //ptr = SkipSpanLine(ptr);  // commented in sources
                ptr.incrementOffset(SpanSkip(ptr))
            }

            val clipIn = dst_clr.copy(src_rect.x)
            dst_clr.incrementOffset(src_rect.y)
            for (yy in 0 until src_rect.height) {
                //ptr = ProcessShadowClipSpanLine(ptr, dst_clr, clipIn, clipIn + src_rect.w );  // commented in sources
                ptr.incrementOffset(Span(op, ptr, dst_clr, clipIn, clipIn.copy(src_rect.width)))
                clipIn.incrementOffset(dstStride)
                dst_clr.incrementOffset(dstStride)
                dst_clr.incrementOffset(1)
                if (ptr.offset != eptr.offset) {
                    ptr.incrementOffset(SpanSkip(ptr))  // shadow skip
                    //ptr = SkipSpanLine(ptr);  // shadow skip  // commented in sources
                } else {
                    break
                }
            }
        }
    }

    private fun BlitShadow2D(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, dst: IPointInt) {
        if (0 == (spr.type_ and 1u).toInt()) {
            check(false)
            return
        }

        val origin = IPointInt(spr.originX_.toInt(), spr.originY_.toInt())
        val size = ISizeInt(spr.sizeW_.toInt(), spr.sizeH_.toInt())

        // correct sprite position according to anchor
        val pos = PointInt(dst)
        pos += dst
        pos += IPointInt(-1, 2)  // shadow displacement

        val src_rect = RectangleInt(size.asRectangle())
        val dst_rect = RectangleInt(pos.x, pos.y, size.width, size.height)
        if (!iClipRectRect(dst_rect, srf.GetSize().asRectangle(), src_rect, size.asRectangle())) {
            return
        }

        // calc dest ptr
        val dst_clr = srf.GetPtr(dst_rect.asPoint())
        val dstStride = srf.GetWidth()

        // blit
        // Spanned sprite
        val op = Shadow50Op

        // <*1*>
        val eptr = data.copy(1 + data[0].toInt())
        val ptr = data.copy(1)

        if (src_rect.asSize() == size) {
            // sprite non clipped - use lighting fast blit
            while (ptr.offset != eptr.offset) {
                ptr.incrementOffset(SpanFast(op, ptr, dst_clr))
                dst_clr.incrementOffset(dstStride)
            }
        } else {
            // sprite clipped - full blown blit
            dst_clr.incrementOffset(-src_rect.x)

            val toSkip = src_rect.y
            repeat(toSkip) {
                ptr.incrementOffset(SpanSkip(ptr))
            }

            val clipIn = dst_clr.copy(src_rect.x)
            repeat(src_rect.height) {
                ptr.incrementOffset(Span(op, ptr, dst_clr, clipIn, clipIn.copy(src_rect.width)))
                dst_clr.incrementOffset(dstStride)
                clipIn.incrementOffset(dstStride)
            }
        }
    }

    private fun BlitWired(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, dst: IPointInt): Unit = TODO()
}
