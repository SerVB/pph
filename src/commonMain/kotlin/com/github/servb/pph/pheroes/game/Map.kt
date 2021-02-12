package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.common.DifficultyLevel
import com.github.servb.pph.pheroes.common.common.MapSize
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.helpertype.UniqueValueEnum
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
    var m_saveTime: SizeT by Delegates.notNull()
    lateinit var m_gameMode: GameMode
    lateinit var m_FileName: String
    lateinit var m_Difficulty: DifficultyLevel
    lateinit var m_Players: List<iPlayerInfo>
    lateinit var m_Size: MapSize
    lateinit var m_Name: String
    lateinit var m_Description: String

    lateinit var m_Version: String
    lateinit var m_Author: String

    val m_curDay: SizeT by Delegates.notNull()
    lateinit var m_curPlayerId: PlayerId
    val m_rseed: UInt by Delegates.notNull()
    lateinit var m_metrics: ISizeInt

    fun ReadMapInfo(fileName: String): Boolean {
        TODO()
    }

    fun ReorderPlayers() {
        TODO()
    }

    fun TotalPlayers(): SizeT = m_Players.size
    fun HumanPlayers(): SizeT = TODO()

    fun Supported(): Boolean {
        TODO()
    }
}

// todo
