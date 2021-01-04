package com.github.servb.pph.gxlib

import com.github.servb.pph.util.Mutable
import com.github.servb.pph.util.SizeInt
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.soywiz.kds.Deque
import com.soywiz.korev.Key
import com.soywiz.korma.geom.ISizeInt
import com.soywiz.korma.geom.SizeInt
import kotlin.properties.Delegates

enum class iKbdKey(override val v: Int) : UniqueValueEnum, UndefinedCountValueEnum {
    INVALID(-1),
    ENTER(0),
    UP(1),
    DOWN(2),
    LEFT(3),
    RIGHT(4),
    VKA(5),
    VKB(6),
    VKC(7),
    VKD(8),
    VKE(9),
    VKF(10),
    REC(11),
    TALK(12),
    END(13),
    BACK(14),
    HOME(15),
    SOFT1(16),
    SOFT2(17),
    COUNT(18),
}

class iInput : iInputMsgHnd {

    sealed class iEntry {

        object Unknown : iEntry()
        class MouseMove(val px: Int, val py: Int) : iEntry()
        class MouseDown(val px: Int, val py: Int) : iEntry()
        class MouseUp(val px: Int, val py: Int) : iEntry()
        class KeyDown(val key: iKbdKey) : iEntry()
        class KeyUp(val key: iKbdKey) : iEntry()
    }

    private val m_KeysState = BooleanArray(iKbdKey.COUNT.v)
    private var m_Flags: UInt by Delegates.notNull()
    private var m_mo: Int = 0
    private var m_dimScreen: SizeInt by Delegates.notNull()
    private val m_Entries: Deque<iEntry> = Deque()
    private var m_bInited: Boolean

    constructor() {
        m_bInited = false
    }

    fun Init(src_siz: ISizeInt, flags: UInt): Boolean {
        check(!m_bInited)
        m_dimScreen = SizeInt(src_siz)
        m_Flags = flags

        m_bInited = true
        return true
    }

    fun Destroy() {
        check(m_bInited)
        m_Entries.clear()

        m_bInited = false
    }

    fun SetOrientation(bLandscape: Boolean, bLeftHander: Boolean) {
        if (bLandscape && (m_Flags and GXLF_LANDSCAPE) == 0u) {
            m_Flags = m_Flags or GXLF_LANDSCAPE
        } else if (!bLandscape && (m_Flags and GXLF_LANDSCAPE) != 0u) {
            m_Flags = m_Flags xor GXLF_LANDSCAPE
        }

        if (bLeftHander && (m_Flags and GXLF_LHANDER) == 0u) {
            m_Flags = m_Flags or GXLF_LHANDER
        } else if (!bLeftHander && (m_Flags and GXLF_LHANDER) != 0u) {
            m_Flags = m_Flags xor GXLF_LHANDER
        }

        if (m_Flags and GXLF_LANDSCAPE != 0u) {
            if ((m_Flags and GXLF_DEV_LANDSCAPE) != 0u || (m_Flags and GXLF_LHANDER) != 0u) {
                m_mo = m_dimScreen.width - 1
            } else {
                m_mo = m_dimScreen.height - 1
            }
        } else {
            m_mo = 0
        }
    }

    fun EntryCount(): SizeT = m_Entries.size  // const method, but iInput isn't passed as const anywhere
    fun Get(): iEntry = m_Entries.removeFirst()
    fun Reset(): Unit = m_Entries.clear()

    fun KeyCode2Key(key_code: Key): iKbdKey {
        return when (key_code) {
            Key.RETURN -> iKbdKey.ENTER
            Key.UP -> iKbdKey.UP
            Key.DOWN -> iKbdKey.DOWN
            Key.LEFT -> iKbdKey.LEFT
            Key.RIGHT -> iKbdKey.RIGHT
            Key.Q -> iKbdKey.VKA
            Key.W -> iKbdKey.VKB
            Key.E -> iKbdKey.VKC

            // added:
            Key.VOLUME_UP -> iKbdKey.VKD
            Key.VOLUME_DOWN -> iKbdKey.VKE

            else -> iKbdKey.INVALID
        }
    }

    fun CvtScrCoor(px: Mutable<Int>, py: Mutable<Int>) {
        var npx = px.element
        var npy = py.element

        // todo: do we need it?
//        #elif defined OS_WINCE
//        if (m_Flags & GXLF_LANDSCAPE) {
//            if (m_Flags & GXLF_LHANDER) {
//            npx = py;
//            npy = m_mo - px;
//        } else {
//            npx = m_mo-py;
//            npy = px;
//        }
//        }
//        #endif //OS_WINCE

        if ((m_Flags and GXLF_DOUBLESIZE) != 0u) {
            npx /= 2
            npy /= 2
        }

        px.element = npx
        py.element = npy
    }

    override fun msg_OnKeyDown(key: Key) {
        val kkey = KeyCode2Key(key)
        if (kkey != iKbdKey.INVALID && !m_KeysState[kkey.v]) {
            m_KeysState[kkey.v] = true
            m_Entries.addLast(iEntry.KeyDown(kkey))
        }
    }

    override fun msg_OnKeyUp(key: Key) {
        val kkey = KeyCode2Key(key)
        if (kkey != iKbdKey.INVALID && m_KeysState[kkey.v]) {
            m_KeysState[kkey.v] = false
            m_Entries.addLast(iEntry.KeyUp(kkey))
        }
    }

    override fun msg_OnMouseMove(px: Int, py: Int) {
        val mpx = Mutable(px)
        val mpy = Mutable(py)
        CvtScrCoor(mpx, mpy)
        m_Entries.addLast(iEntry.MouseMove(mpx.element, mpy.element))
    }

    override fun msg_OnMouseDown(px: Int, py: Int) {
        val mpx = Mutable(px)
        val mpy = Mutable(py)
        CvtScrCoor(mpx, mpy)
        m_Entries.addLast(iEntry.MouseDown(mpx.element, mpy.element))
    }

    override fun msg_OnMouseUp(px: Int, py: Int) {
        val mpx = Mutable(px)
        val mpy = Mutable(py)
        CvtScrCoor(mpx, mpy)
        m_Entries.addLast(iEntry.MouseUp(mpx.element, mpy.element))
    }

    fun ClearKeyState() {
        m_KeysState.fill(false)
    }
}
