@file:Suppress("PackageName")

package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.ReadS32
import com.github.servb.pph.gxlib.ReadU32
import com.github.servb.pph.gxlib.Write
import com.github.servb.pph.gxlib.iKbdKey
import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.github.servb.pph.util.helpertype.getByValue
import com.soywiz.klogger.Logger
import com.soywiz.kmem.buildByteArray
import com.soywiz.korio.file.std.localCurrentDirVfs
import com.soywiz.korma.math.clamp
import kotlin.properties.Delegates

private const val CONFIG_FILE_HDR = 0xA173B7C3u

private class ConfigEntryDesc(val min: Int, val max: Int, val default: Int)

// todo: add check for size == ConfigEntryType.COUNT
private val CONFIG_ENTRY_DESC = listOf(
    ConfigEntryDesc(0, 10, 5),  // Sfx volume
    ConfigEntryDesc(0, 4, 0),  // Display gamma
    ConfigEntryDesc(0, 4, 2),  // Map scroll speed
    ConfigEntryDesc(0, 4, 2),  // Hero speed
    ConfigEntryDesc(0, 4, 2),  // Combat speed
    ConfigEntryDesc(0, 1, 0),  // Quick combat
    ConfigEntryDesc(0, 1, 1),  // End turn confitmation
    ConfigEntryDesc(0, 1, 0),  // Lefthander mode
    ConfigEntryDesc(0, 1, 0),  // Overland grid
    ConfigEntryDesc(0, 1, 1),  // Combat grid
    ConfigEntryDesc(0, 1, 1),  // Travel points mode
    ConfigEntryDesc(0, 1, 1),  // Survey map filtration
    ConfigEntryDesc(0, 1, 0),  // Survey map scale
    ConfigEntryDesc(0, 2, 2),  // New game dialog list sort order
    ConfigEntryDesc(0, -1, 0),  // New game dialog list position
)

// todo: add check for size == ButtonActionType.COUNT
private val CONFIG_DEFKEYS = listOf(
    iKbdKey.ENTER,  // Help mode
    iKbdKey.VKA,  // Hand mode
    iKbdKey.VKB,  // Survey map
    iKbdKey.VKC,  // Minimize
    iKbdKey.INVALID,  // Make screenshot
)

private const val CONFIG_FILE = "PalmHeroes.cfg"

private val logger by lazy { Logger("Settings") }

enum class ButtonActionType(override val v: Int) : UndefinedCountValueEnum, UniqueValueEnum {

    INVALID(-1),
    HELP_MODE(0),
    HAND_MODE(1),
    SURVEY_MODE(2),
    MINIMIZE_APP(3),
    MAKE_SCREENSHOT(4),
    COUNT(5),
}

enum class ConfigEntryType(override val v: Int) : CountValueEnum, UniqueValueEnum {
    SFXVOLUME(0),
    DISPGAMMA(1),
    MAPSCROLLSPEED(2),
    HEROSPEED(3),
    COMBATSPEED(4),
    QUICKCOMBAT(5),
    ENDTURNCONF(6),
    LHANDMAODE(7),
    OVRLANDGRID(8),
    COMBATGRID(9),
    TRVPTSMODE(10),
    SURVMAPFILTR(11),
    SURVMAPSCALE(12),
    NGDSORT(13),
    NGDPOS(14),
    COUNT(15),
}

// activation key, sn, and magic numbers are removed
interface IiSettings {

    fun GetEntryValue(entry: ConfigEntryType): Int
    fun ActionKey(bat: ButtonActionType): iKbdKey
    fun ForceNoSound(): Boolean
    fun FogOfWar(): Boolean
    fun ShowEnemyTurn(): Boolean
    fun MapSpriteFile(): Boolean
}

class iSettings : IiSettings {

    private val m_cfgEntries = IntArray(ConfigEntryType.COUNT.v)
    private val m_actionKeys = IntArray(ButtonActionType.COUNT.v)
    private var m_bFogOfWar: Boolean by Delegates.notNull()
    private var m_bShowEnemyTurn: Boolean by Delegates.notNull()
    private var m_bMapSpriteFile: Boolean by Delegates.notNull()
    private var m_bNoSound: Boolean by Delegates.notNull()

    suspend fun `$destruct`() {
        Save()
    }

    suspend fun Init(cmdLine: String): Boolean {
        // Reset config entries
        m_bMapSpriteFile = false
        m_bShowEnemyTurn = false
        m_bFogOfWar = true
        m_bNoSound = false

        m_cfgEntries.indices.forEach { nn ->
            m_cfgEntries[nn] = CONFIG_ENTRY_DESC[nn].default
        }
        m_actionKeys.indices.forEach { nn ->
            m_actionKeys[nn] = CONFIG_DEFKEYS[nn].v
        }

        if ("--show_enemy_turn" in cmdLine) {
            m_bShowEnemyTurn = true
        }
//        if ("--disable_fow" in cmdLine) {  //disabled as of 1.04b (Hedin)
//            m_bFogOfWar = false
//        }
        if ("--no_sound" in cmdLine) {
            m_bNoSound = true
        }
        if ("--map_sprite_file" in cmdLine) {
            m_bMapSpriteFile = true
        }

        val pConfigFile = localCurrentDirVfs[CONFIG_FILE]
        if (!pConfigFile.exists()) {
            logger.info { "Config file (${pConfigFile.absolutePath}) not found, using defaults" }
            return true
        }
        val stream = pConfigFile.openInputStream()
        val hdr = stream.ReadU32()
        if (hdr != CONFIG_FILE_HDR) {
            logger.info { "Config file (${pConfigFile.absolutePath}) has a bad header ($hdr), using defaults" }
            return true
        }
        m_cfgEntries.indices.forEach { nn ->
            m_cfgEntries[nn] = stream.ReadS32()
        }
        m_actionKeys.indices.forEach { nn ->
            m_actionKeys[nn] = stream.ReadS32()
        }
        return true
    }

    suspend fun Save() {
        val data = buildByteArray {
            Write(CONFIG_FILE_HDR)
            m_cfgEntries.indices.forEach { nn ->
                Write(m_cfgEntries[nn])
            }
            m_actionKeys.indices.forEach { nn ->
                Write(m_actionKeys[nn])
            }
        }

        localCurrentDirVfs[CONFIG_FILE].write(data)
    }

    override fun GetEntryValue(entry: ConfigEntryType): Int = m_cfgEntries[entry.v]

    fun SetEntryValue(entry: ConfigEntryType, value: Int) {
        if (CONFIG_ENTRY_DESC[entry.v].max == -1) {
            m_cfgEntries[entry.v] = maxOf(CONFIG_ENTRY_DESC[entry.v].min, value)
        } else {
            m_cfgEntries[entry.v] = value.clamp(CONFIG_ENTRY_DESC[entry.v].min, CONFIG_ENTRY_DESC[entry.v].max)
        }
    }

    override fun ActionKey(bat: ButtonActionType): iKbdKey = getByValue(m_actionKeys[bat.v])

    fun AssignActionKey(bat: ButtonActionType, key: iKbdKey) {
        m_actionKeys[bat.v] = key.v
    }

    fun GetActionByKey(key: iKbdKey): ButtonActionType {
        val idx = m_actionKeys.indexOf(key.v)

        return if (idx == -1) {
            ButtonActionType.INVALID
        } else {
            getByValue(idx)
        }
    }

    override fun ForceNoSound(): Boolean = m_bNoSound
    override fun FogOfWar(): Boolean = m_bFogOfWar
    override fun ShowEnemyTurn(): Boolean = m_bShowEnemyTurn
    override fun MapSpriteFile(): Boolean = m_bMapSpriteFile
}
