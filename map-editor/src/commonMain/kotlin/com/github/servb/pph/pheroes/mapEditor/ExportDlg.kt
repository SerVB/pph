package com.github.servb.pph.pheroes.mapEditor

import com.github.servb.pph.gxlib.Write
import com.github.servb.pph.iolib.xe.FindSolidArea
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.invoke
import com.soywiz.klogger.Logger
import com.soywiz.kmem.ByteArrayBuilder
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.BGRA
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.color.convertTo
import com.soywiz.korio.compression.compress
import com.soywiz.korio.compression.deflate.Deflate
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.std.localCurrentDirVfs
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.PointInt
import com.soywiz.korma.geom.RectangleInt
import kotlin.experimental.or

private val logger = Logger("ExportDlg")  // todo: without name, the compilation crashes

private fun RGB32_2_RGB16(rgba: RGBA): UShort {
    val bgra = RGBA.convertTo(rgba.value, BGRA)
    return (((bgra ushr 8) and 0xF800) or ((bgra ushr 5) and 0x7E0) or ((bgra ushr 3) and 0x1F)).toUShort()
}

private fun MakeSpanSprite(src: Bitmap32, buff: ByteArrayBuilder) {
    buff.clear()
    repeat(src.height) { yy ->
        val sptr: SizeT = src.index(0, yy)
        var tptr: SizeT = sptr

        var phIdx: SizeT? = null
        var fspace = 0
        var span_begin: Int = -42
        var span_len: Int = -42
        var mode = 0

        repeat(src.width) { xx ->
            if (src.data[tptr].a != 0) {
                if (mode == 0) {
                    span_begin = xx
                    span_len = 0
                    mode = 1
                }
                ++span_len
            } else {
                if (mode == 1) {
                    mode = 0
                    phIdx = buff.size
                    buff.Write(((fspace shl 8) or span_len).toUShort())
                    repeat(span_len) { zz ->
                        val pptr = sptr + span_begin + zz
                        val value: UShort = RGB32_2_RGB16(src.data[pptr])
                        buff.Write(value)
                    }
                    fspace = 0
                }
                ++fspace
            }
            ++tptr
        }

        if (mode == 1) {
            buff.Write(((1 shl 15) or (fspace shl 8) or span_len).toUShort())
            repeat(span_len) { zz ->
                val pptr = sptr + span_begin + zz
                val value: UShort = RGB32_2_RGB16(src.data[pptr])
                buff.Write(value)
            }
        } else if (phIdx == null) {
            buff.Write((1 shl 15).toUShort())
        } else {
            buff.data[phIdx!! + 1] = buff.data[phIdx!! + 1] or (1u shl (15 - 8)).toByte()
        }
    }
}

private fun MakeSprite(src: Bitmap32, buff: ByteArrayBuilder) {
    buff.clear()
    repeat(src.height) { yy ->
        val pSrc = src.index(0, yy)
        repeat(src.width) { xx ->
            val value: UShort = RGB32_2_RGB16(src.data[pSrc + xx])
            buff.Write(value)
        }
    }
}

private fun CropSprite(dib: Bitmap32, anchor: PointInt): Bitmap32 {
    val nrc = FindSolidArea(dib)
    val odib = dib.slice(nrc).extract()
    anchor.x += nrc.x
    anchor.y += nrc.y
    return odib
}

private const val SPFT_RAW = 0x0u
private const val SPFT_SPANNED = 0x1u
private const val SPF_SHADOW = 0xEu
private const val SPF_SHT_NONE = 0x0u
private const val SPF_SHT_BASIC = 0x2u
private const val SPF_SHT_GUI = 0x4u
private const val SPF_SHT_ALT = 0x6u

private class GfxBankExporter {

    private class BankImage(
        val imageType: UByte,  // type props
        val dataOffset: SizeT,  // offset of the sprite data from the begining of data block ( in uint16 )
        val imageWidth: UShort,
        val imageHeight: UShort,
        val originX: Short,
        val originY: Short,
    ) {

        companion object {

            const val SIZE = 1 + 4 + 2 + 2 + 2 + 2
        }
    }

    private val catalog: MutableList<BankImage> = mutableListOf()
    private val pixels: ByteArrayBuilder = ByteArrayBuilder()
    private val m_procData: ByteArrayBuilder = ByteArrayBuilder()

    fun Put(sprite: Bitmap32, name: String, spanned: Boolean = true, crop: Boolean = false) {
        val dib = sprite.slice().extract()
        val type = if (spanned) {
            SPFT_SPANNED
        } else {
            SPFT_RAW
        }.toUByte()
        AddSprite(dib, name, PointInt(), type, crop)
    }

    fun Put(sprite: Bitmap32, name: String, rc: IRectangleInt, spanned: Boolean = true, crop: Boolean = false) {
        val part = sprite.slice(RectangleInt(rc)).extract()
        Put(part, name, spanned, crop)
    }

    fun Put(sprite: iSprite, name: String) {
        val type = (if (sprite.spanned) {
            SPFT_SPANNED
        } else {
            SPFT_RAW
        } or (sprite.shadow.toUInt() shl 1)).toUByte()
        AddSprite(sprite.dib, name, PointInt(sprite.anchor.x, sprite.anchor.y), type, sprite.crop)
    }

    fun Export(): ByteArray {
        // header
        val hdrBits = uintArrayOf(
            0x1A464253u,
            catalog.size.toUInt(),
            pixels.size.toUInt(),
            0xffffffffu,
        )

        val dest = ByteArrayBuilder()

        hdrBits.forEach { dest.Write(it) }
        dest.append(pixels.toByteArray())
        hdrBits.forEach { dest.Write(it) }

        catalog.forEach {
            dest.Write(it.imageType)
            dest.Write(it.dataOffset)
            dest.Write(it.imageWidth)
            dest.Write(it.imageHeight)
            dest.Write(it.originX)
            dest.Write(it.originY)
        }

        logger.info { "- Done: ${pixels.size} bytes in ${catalog.size} sprites (Dsc: ${catalog.size * BankImage.SIZE} bytes)." }

        return dest.toByteArray()
    }

    fun Size(): SizeT = catalog.size

    fun Clear() {
        catalog.clear()
        pixels.size = 0
    }

    private fun Validate(sprite: Bitmap32, type: UByte): Boolean {
        MakeSpanSprite(sprite, m_procData)
        val spannedSize = m_procData.size
        MakeSprite(sprite, m_procData)
        val rawSize = m_procData.size

        val spanRequested = (type and 1u) == 1u.toUByte()

        if (rawSize < spannedSize && spanRequested) {
            return false
        }

        return true
    }

    private fun AddSprite(sprite: Bitmap32, name: String, origin: PointInt, type: UByte, crop: Boolean) {
        if (!Validate(sprite, type)) {
            logger.info { "$name -!- Warning: spanned sprite is bigger than raw!" }
        }

        val dib = if (crop) {
            CropSprite(sprite, origin)
        } else {
            sprite
        }

        val img = BankImage(
            imageType = type,
            dataOffset = pixels.size / 2,
            imageWidth = dib.width.toUShort(),
            imageHeight = dib.height.toUShort(),
            originX = origin.x.toShort(),
            originY = origin.y.toShort(),
        )

        catalog.add(img)

        if ((type and 1u) != 0u.toUByte()) {
            MakeSpanSprite(dib, m_procData)

            check(m_procData.size < 65536 * 2) { "Im too lazy to split uint32 ;)" }

            pixels.Write((m_procData.size / 2).toUShort())  // <*1*>
            pixels.append(m_procData.toByteArray())
        } else {
            MakeSprite(dib, m_procData)
            pixels.append(m_procData.toByteArray())
        }
    }
}

suspend fun exportSprites(spriteSetFile: VfsFile): ByteArray {
    val spriteMgr = iSpriteMgr()
    spriteMgr.Init(spriteSetFile)
    val spriteList = mutableListOf<Pair<String, Int>>()
    spriteMgr.m_SpriteHash.forEach { (k, v) ->
        spriteList.add(k to v.id)
    }
    spriteList.sortBy { it.second }

    val gBank = GfxBankExporter()
    spriteList.forEach { (k, _) ->
        gBank.Put(spriteMgr[k], k)
    }
    return gBank.Export()
}

suspend fun main() {
    val sprites = exportSprites(localCurrentDirVfs["resourcesRoot/pheroes/bin/Resources/hmm/GFX/spriteset.xml"])
    val compressed = sprites.compress(Deflate)

    val dir = localCurrentDirVfs["resourcesRoot/Game/Data"]
    if (!dir.exists()) {
        dir.mkdir()
    }

    val file = dir["game.gfx"]
    file.write(compressed)
}
