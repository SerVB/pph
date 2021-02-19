package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.ReadS8
import com.github.servb.pph.gxlib.ReadU16
import com.github.servb.pph.gxlib.ReadU32
import com.github.servb.pph.gxlib.ReadU8
import com.github.servb.pph.pheroes.common.DeserializePoint
import com.github.servb.pph.pheroes.common.DeserializeString
import com.github.servb.pph.pheroes.common.common.*
import com.github.servb.pph.util.ISizeInt
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.getByValue
import com.soywiz.klogger.Logger
import com.soywiz.korio.stream.AsyncInputStream
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.ISizeInt
import kotlin.properties.Delegates

class iMapInfo {

    enum class GameMode(override val v: Int) : UniqueValueEnum {
        UNDEFINED(0),
        SPLAYER(1),
        HOTSEAT(2),
    }

    class iPlayerInfo {

        lateinit var m_Minerals: MineralSetC
        var m_Id: PlayerId
        var m_TypeMask: PlayerTypeMask

        var m_Type: PlayerType

        //        var m_Nation: CastleType
        var m_curHeroId: UShort by Delegates.notNull()
        var m_curCastleIdx: UShort by Delegates.notNull()
        var m_keys: UByte by Delegates.notNull()

        constructor(pid: PlayerId, ptypemask: PlayerTypeMask, ptype: PlayerType/*, ntype: CastleType*/) {
            m_Id = pid
            m_TypeMask = ptypemask
            m_Type = ptype
//            m_Nation = ntype
        }
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
        m_Size = getByValue(mapSiz.toInt())

        m_Name = DeserializeString(pFile)
        m_Description = DeserializeString(pFile)

        m_Version = DeserializeString(pFile)
        m_Author = DeserializeString(pFile)

        m_curDay = pFile.ReadU32()  // Current date (1 is default value for new game)

        val gameMode = pFile.ReadU16()  // Game mode (GM_UNDEFINED for new map)
        m_gameMode = getByValue(gameMode.toInt())

        val gameDifLvl = pFile.ReadS8()  // Difficulty level (DFC_UNDEFINED for new game)
        m_Difficulty = getByValue(gameDifLvl.toInt())

        m_Players = emptyList()  // todo
        m_curPlayerId = PlayerId.NEUTRAL  // todo
        m_metrics = ISizeInt(42, 42)  // todo

        return true
    }

    suspend fun ReadMapInfoHmm(fileBuff: AsyncInputStream): Boolean {
        m_Difficulty = DifficultyLevel.UNDEFINED
        m_gameMode = GameMode.UNDEFINED
        m_curDay = 1u
        m_curPlayerId = PlayerId.NEUTRAL

        val fileVersion = fileBuff.ReadU32()
        val mapSize = fileBuff.ReadU8()
        m_Size = getByValue(mapSize.toInt())

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

        if (fileVersion > 0x12u) {
            val timeEventCount = fileBuff.ReadU16().toInt()

            if (timeEventCount != 0) {
                logger.warn { "no support for time events in HMM files yet..." }  // todo
                return false
            }
        }

        if (fileVersion >= 0x18u) {
            DeserializePoint(fileBuff)  // todo: m_posUltimateArt
            fileBuff.ReadU32()  // todo: m_radUltimateArt
        }

        class iMainCtlDesc(val pid: PlayerId, val pos: IPointInt, val bCreateHero: UByte)

        val players = mutableListOf<iPlayerInfo>()
        val mainCtls = mutableListOf<iMainCtlDesc>()
        val playerCount = fileBuff.ReadU16().toInt()
        repeat(playerCount) {
            val playerId = fileBuff.ReadS8()
            val playerTypeMask = fileBuff.ReadS8()
            if (fileVersion >= 0x12u) {
                val hasMainCtl = fileBuff.ReadU8() != 0u.toUByte()
                if (hasMainCtl) {
                    val mainCtl = iMainCtlDesc(
                        pid = getByValue(playerId.toInt()),
                        pos = DeserializePoint(fileBuff),
                        bCreateHero = fileBuff.ReadU8(),
                    )
                    mainCtls.add(mainCtl)
                }
            }
            val player = iPlayerInfo(
                pid = getByValue(playerId.toInt()),
                ptypemask = getByValue(playerTypeMask.toInt()),
                ptype = PlayerType.UNDEFINED,  // UNDEFINED because new game
//                ntype = CastleType.CITADEL,  // todo: depends on the main hero
            )
            players.add(player)
        }

        m_Players = players
        m_metrics = ISizeInt(42, 42)  // todo
        // todo: read other info

        return true
    }

    fun ReorderPlayers() {
        // todo
    }

    fun TotalPlayers(): SizeT = m_Players.size
    fun HumanPlayers(): SizeT =
        m_Players.count { it.m_TypeMask in setOf(PlayerTypeMask.HUMAN_ONLY, PlayerTypeMask.HUMAN_OR_COMPUTER) }

    fun Supported(): Boolean {
        return true  // todo
    }

    private companion object {

        private val logger = Logger<iMapInfo>()
    }
}

// todo
