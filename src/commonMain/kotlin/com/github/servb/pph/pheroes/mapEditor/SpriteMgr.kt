package com.github.servb.pph.pheroes.mapEditor

import com.github.servb.pph.gxlib.cInvalidRect
import com.github.servb.pph.util.SizeT
import com.soywiz.klogger.Logger
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.lang.format
import com.soywiz.korio.serialization.xml.Xml
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.PointInt
import com.soywiz.korma.geom.RectangleInt
import kotlin.properties.Delegates

class iSprite {

    var id: SizeT by Delegates.notNull()
    lateinit var anchor: IPointInt
    var spanned: Boolean by Delegates.notNull()
    var crop: Boolean by Delegates.notNull()
    var shadow: UByte by Delegates.notNull()
    lateinit var dib: Bitmap32

    fun Init(
        _dib: Bitmap32,
        _anchor: IPointInt,
        _spanned: Boolean,
        _crop: Boolean,
        _shadow: UByte,
        rc: IRectangleInt = cInvalidRect
    ) {
        id = -1
        anchor = _anchor
        spanned = _spanned
        crop = _crop
        shadow = _shadow
        if (rc == cInvalidRect) {
            dib = _dib
        } else {
            dib = _dib.slice(RectangleInt(rc)).extract()
        }
    }
}

private class iSpriteXmlReader {

    lateinit var m_GroupName: String
    val m_SpriteMgr: iSpriteMgr

    constructor(spriteMgr: iSpriteMgr) {
        m_SpriteMgr = spriteMgr
    }

    suspend fun parse(vfsFile: VfsFile): Boolean {
        fun String.parsePoint(): PointInt {
            if (isEmpty()) {
                return PointInt(0, 0)
            }
            val coords = split(',').map { it.trim().toInt() }
            return PointInt(coords[0], coords[1])
        }

        Xml.Stream.parse(vfsFile.readString()).forEach {
            when {
                it is Xml.Stream.Element.OpenTag && it.name == "Group" -> {
                    m_GroupName = it.attributes["id"]!!
                    println("loading sprite group $m_GroupName")
                }
                it is Xml.Stream.Element.OpenCloseTag && it.name == "Item" -> {
                    val shadow: UByte = it.attributes["shadow"]?.toUByte() ?: 0u
                    val seq: PointInt = it.attributes["sequence"]?.parsePoint() ?: PointInt(1, 1)
                    val spanned: Boolean = when (it.attributes["spanned"]?.toInt()) {
                        0 -> false
                        null -> true  // by default it's true
                        else -> true
                    }
                    val id = it.attributes.getValue("id")

                    if (!m_SpriteMgr.AddSprite(
                            name = "$m_GroupName.$id",
                            chunk = it.attributes.getValue("chunk"),
                            anchor = it.attributes.getOrElse("anchor") { "" }.parsePoint(),
                            spanned = spanned,
                            crop = it.attributes["crop"]?.toInt() == 1,
                            shadow = shadow,
                            seqX = seq.x,
                            seqY = seq.y,
                            constName = it.attributes["constId"] ?: "",
                        )
                    ) {
                        return false
                    }
                }
            }
        }

        return true
    }
}

class iConstDef(val m_SpriteName: String, val m_ConstName: String, val m_SeqCnt: SizeT)

typealias iConstDefList = MutableList<iConstDef>

class iSpriteMgr {

    private lateinit var m_ResPath: VfsFile
    private val m_InvalidSprite: iSprite = iSprite()

    val m_SpriteHash: MutableMap<String, iSprite> = mutableMapOf()
    val m_ConstDefList: iConstDefList = mutableListOf()

    fun GetResPath(): VfsFile = m_ResPath

    suspend fun AddSprite(
        name: String,
        chunk: String,
        anchor: IPointInt,
        spanned: Boolean,
        crop: Boolean,
        shadow: UByte,
        seqX: SizeT,
        seqY: SizeT,
        constName: String
    ): Boolean {
        if (seqX == 0 || seqY == 0) {
            TODO("MessageBox(NULL,iFormat(_T(\"Unable to open sprite %s !\"),(m_ResPath + chunk).CStr()).CStr(),_T(\"Error\"),MB_OK); return false")
        }
        val file = m_ResPath[chunk]
        val tdib = if (file.exists() && file.isFile()) {
            file.readBitmap().toBMP32IfRequired()
        } else {
            logger.warn { "Can't find '${file.path}' sprite, setting to dummy" }
            Bitmap32(128, 128, Colors.VIOLET)
        }

        if (seqX == 1 && seqY == 1) {
            m_SpriteHash[name] = iSprite().apply {
                Init(tdib, anchor, spanned, crop, shadow)
            }
        } else {
            val iw = tdib.width / seqX
            val ih = tdib.height / seqY
            val rc = RectangleInt(0, 0, iw, ih)
            repeat(seqY) { yy ->
                repeat(seqX) { xx ->
                    val nname = "%s_%04d".format(name, yy * seqX + xx)
                    m_SpriteHash[nname] = iSprite().apply {
                        Init(tdib, anchor, spanned, crop, shadow, rc)
                    }
                    rc.x += iw
                }
                rc.y += ih
                rc.x = 0
            }
        }

        if (constName.isNotEmpty()) {
            m_ConstDefList.add(iConstDef(name, constName, seqX * seqY))
        }

        return true
    }

    fun GetSpritePtr(name: String): iSprite {
        return m_SpriteHash[name] ?: m_InvalidSprite
    }

    suspend fun Init(spriteSetFile: VfsFile): Boolean {
        m_ResPath = spriteSetFile.parent
        val reader = iSpriteXmlReader(this)
        if (!reader.parse(spriteSetFile)) {
            return false
        }

        // Normalize indeces
        var constCount = 0
        m_ConstDefList.forEach {
            if (it.m_SeqCnt == 1) {
                this[it.m_SpriteName].id = constCount
                ++constCount
            } else {
                repeat(it.m_SeqCnt) { zz ->
                    val nname = "%s_%04d".format(it.m_SpriteName, zz)
                    this[nname].id = constCount
                    ++constCount
                }
            }
        }

        var idCounter = constCount
        m_SpriteHash.forEach { (_, v) ->
            if (v.id == -1) {
                v.id = idCounter
                ++idCounter
            }
        }

        logger.info { "Normilized $idCounter sprites ($constCount const entries)" }

        return true
    }

    operator fun get(name: String): iSprite {
        return m_SpriteHash.getValue(name)
    }
}

private val logger = Logger("SpriteMgr")
