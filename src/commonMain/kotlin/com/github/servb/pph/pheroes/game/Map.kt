package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.ReadS8
import com.github.servb.pph.gxlib.ReadU16
import com.github.servb.pph.gxlib.ReadU32
import com.github.servb.pph.gxlib.ReadU8
import com.github.servb.pph.pheroes.common.DeserializeString
import com.github.servb.pph.pheroes.common.common.DifficultyLevel
import com.github.servb.pph.pheroes.common.common.GMAP_FILE_VERSION
import com.github.servb.pph.pheroes.common.common.MapSize
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.ISizeInt
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.soywiz.klogger.Logger
import com.soywiz.korio.stream.AsyncInputStream
import com.soywiz.korma.geom.ISizeInt
import kotlin.properties.Delegates

class iMapInfo {

    enum class GameMode(override val v: Int) : UniqueValueEnum {
        UNDEFINED(0),
        SPLAYER(1),
        HOTSEAT(2),
    }

    class iPlayerInfo {

        val todo: Any = TODO()
    }

    var m_bNewGame: Boolean by Delegates.notNull()
    var m_saveTime: UInt by Delegates.notNull()
    lateinit var m_gameMode: GameMode
    lateinit var m_FileName: String
    lateinit var m_Difficulty: DifficultyLevel
    lateinit var m_Players: List<iPlayerInfo>
    lateinit var m_Size: MapSize
    lateinit var m_Name: String
    lateinit var m_Description: String

    lateinit var m_Version: String
    lateinit var m_Author: String

    var m_curDay: UInt by Delegates.notNull()
    lateinit var m_curPlayerId: PlayerId
    var m_rseed: UInt by Delegates.notNull()
    lateinit var m_metrics: ISizeInt

    suspend fun ReadMapInfoPhm(pFile: AsyncInputStream): Boolean {
        // todo: this is ported from SiGMan's game sources but the format is changed in other game versions
        //       so we should support them too (probably parse all variants and use exception-based logic)

        val ver = pFile.ReadU16()
        if (ver != GMAP_FILE_VERSION) {
            logger.warn { "Map $m_FileName has bad version $ver, skipping" }
            return false
        }

        m_saveTime = pFile.ReadU32()

        m_rseed = pFile.ReadU32()

        val mapSiz = pFile.ReadU8()
        m_Size = MapSize.values().single { it.v == mapSiz.toInt() }

        m_Name = DeserializeString(pFile)
        m_Description = DeserializeString(pFile)

        m_Version = DeserializeString(pFile)
        m_Author = DeserializeString(pFile)

        m_curDay = pFile.ReadU32()  // Current date (1 is default value for new game)

        val gameMode = pFile.ReadU16()  // Game mode (GM_UNDEFINED for new map)
        m_gameMode = GameMode.values().single { it.v == gameMode.toInt() }

        val gameDifLvl = pFile.ReadS8()  // Difficulty level (DFC_UNDEFINED for new game)
        m_Difficulty = DifficultyLevel.values().single { it.v == gameDifLvl.toInt() }

        m_Players = emptyList()  // todo
        m_curPlayerId = PlayerId.NEUTRAL  // todo
        m_metrics = ISizeInt(42, 42)  // todo

        return true
    }

    suspend fun ReadMapInfoHmm(fileBuff: AsyncInputStream): Boolean {
        m_Difficulty = DifficultyLevel.UNDEFINED
        m_gameMode = GameMode.UNDEFINED
        m_curDay = 1u

        val fileVersion = fileBuff.ReadU32()
        val mapSize = fileBuff.ReadU8()
        m_Size = MapSize.values().single { it.v == mapSize.toInt() }

        if (fileVersion > 0x18u) {
            logger.warn { "no support for multilanguage HMM files yet..." }  // todo
            return false
        }

        if (fileVersion <= 0x18u) {
            m_Name = DeserializeString(fileBuff)
            m_Description = DeserializeString(fileBuff)
        }

        if (fileVersion >= 0x15u) {
            m_Version = DeserializeString(fileBuff)
            m_Author = DeserializeString(fileBuff)
        }

        m_Players = emptyList()  // todo
        m_curPlayerId = PlayerId.NEUTRAL  // todo
        m_metrics = ISizeInt(42, 42)  // todo
        // todo: read other info

        return true
    }

    fun ReorderPlayers() {
        // todo
    }

    fun TotalPlayers(): SizeT = m_Players.size
    fun HumanPlayers(): SizeT = 0  // todo

    fun Supported(): Boolean {
        return false  // todo
    }

    private companion object {

        private val logger = Logger<iMapInfo>()
    }
}

// todo
