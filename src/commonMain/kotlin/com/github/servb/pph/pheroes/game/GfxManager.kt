package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.util.*
import com.soywiz.korio.compression.deflate.Deflate
import com.soywiz.korio.compression.uncompress
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

            val gfxData = rootVfs[fileName].readAll().uncompress(Deflate)

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
    fun BlitAlpha(sid: SpriteId, to: iDib, pos: IPointInt, a: UByte): Unit = TODO()

    // alpha - 0-63
    fun BlitAlpha(sid: SpriteId, to: iDib, src: IRectangleInt, pos: IPointInt, a: UByte): Unit = TODO()

    fun BlitEffect(sid: SpriteId, to: iDib, pos: IPointInt, efx: Effects): Unit = TODO()

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

    fun BlitMasked(sid: SpriteId, mid: SpriteId, to: iDib, pos: IPointInt): Unit = TODO()

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
    ): Unit = TODO()

    private fun BlitTransparent(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, dst: IPointInt): Unit = TODO()

    private fun BlitShadowIso(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, dst: IPointInt): Unit = TODO()

    private fun BlitShadow2D(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, dst: IPointInt): Unit = TODO()

    private fun BlitWired(spr: Sprite, data: IDibIPixelIPointer, srf: iDib, dst: IPointInt): Unit = TODO()
}
