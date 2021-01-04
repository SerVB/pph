package com.github.servb.pph.gxlib

import com.github.servb.pph.util.isEmpty
import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.Stage
import com.soywiz.korio.async.delay
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.setTo
import kotlin.properties.Delegates

interface IGame {

    suspend fun Process(t: Double): Int
    fun OnKeyDown(key: iKbdKey)
    fun OnKeyUp(key: iKbdKey)
    fun OnSuspend()
    fun OnResume()
}

class iGXApp {

    private val m_BaseMetrics: SizeInt = SizeInt()
    private var m_Flags: UInt by Delegates.notNull()
    private var m_CycleDelay: UInt by Delegates.notNull()
    private var m_processTimer: UInt by Delegates.notNull()
    private var m_bExit: Boolean

    private var m_pGame: IGame?
    private val m_Window: iWindow = iWindow()
    private val m_Input: iInput = iInput()
    private val m_Timer: iTimer = iTimer()
    private val m_Display: iDisplay = iDisplay()

    private val m_viewMgr: iViewMgr

    //    private lateinit var m_SoundPlayer: iSoundPlayer  // todo: all related to sound player is deleted now
    private var m_bInited: Boolean
    private var m_bSuspended: Boolean

//    private var m_aygDLL: HINSTANCE  // todo: are these fields really needed?
//    private var m_coreDLL: HINSTANCE
//
//    private var m_shfullscreen: VOID_STAR
//    private var m_sysidletimer: VOID_STAR

    constructor() {
        m_viewMgr = iViewMgr(this)
        m_pGame = null
        m_bInited = false
        m_bSuspended = false
        m_bExit = false
        m_CycleDelay = 30u
        m_processTimer = 0u
    }

    fun Init(pGame: IGame, cdelay: UInt, flags: UInt, stage: Stage): Boolean {
        var flags = flags
        check(!m_bInited)
        m_pGame = pGame
        m_CycleDelay = cdelay
        m_Flags = flags
        // todo: currently used metrics are from HPC_JORNADA, need to research other variants (see sources)
        m_BaseMetrics.setTo(320, 240)
        flags = flags or GXLF_DEV_LANDSCAPE
        val scrsiz = m_BaseMetrics
        val osiz = m_BaseMetrics

        do {
            if (!m_Window.Init(osiz, this, stage)) break
            if (!m_Input.Init(osiz, flags)) break
            if (!m_Timer.Init()) break
            if (!m_Display.Init(scrsiz, flags, stage)) break
            m_Window.SetOwner(this)
            m_bInited = true
        } while (false)

        if (!m_bInited) {
            m_Display.Destroy()
            m_Input.Destroy()
            m_Window.Destroy()
        }

        return m_bInited
    }

    fun Destroy() {
        check(m_bInited) { "Can't destroy not inited app" }

        m_Display.Destroy()
        m_Input.Destroy()
        m_Window.Destroy()
        m_pGame = null
        m_bInited = false
    }

    /**
     * in sources, it's a static variable inside the function, so let's workaround it like this.
     * see place of usage.
     */
    private val m_LastUpdate by lazy { m_Timer.GetCurTime() }

    suspend fun Cycle(): Boolean {
        while (m_Input.EntryCount() != 0) {
            m_viewMgr.ProcessMessage(m_Input.Get())
        }

        m_LastUpdate * m_LastUpdate  // seems to be initialization of m_Timer, more info in sources

        val step: UInt = m_Timer.GetStep()

        if (!m_bSuspended) {
            m_pGame!!.Process(step.toInt() / 1000.0)
            m_viewMgr.Process(step)
            if (step + m_processTimer > m_CycleDelay) {
                m_processTimer = 0u
                val rc = RectangleInt()
                m_viewMgr.Compose(rc)
                if (!rc.isEmpty) {
                    m_Display.DoPaint(rc)
                }
            } else {
                m_processTimer += step
            }
        }
        delay(1.milliseconds)

        return true
    }

    suspend fun Run() {
        while (!m_bExit && Cycle()) {
            // cycling, no body needed
        }
    }

    fun Exit(code: UInt) {
        m_bExit = true
    }

    fun Minimize() {
//        if (OS_WINCE) {
//            ShowWindow(m_Window.GetHWND(), SW_MINIMIZE)
//
//            if (!HPC_JORNADA) {
//                ShellFullscreen(m_Window.GetHWND(), SHFS_SHOWTASKBAR or SHFS_SHOWSIPBUTTON or SHFS_SHOWSTARTICON)
//            }
//        }
    }
    fun Suspend() {
        if (!m_bSuspended) {
//            if (OS_WINCE) {
//                m_pGame?.OnSuspend() ?: check(false)
//                m_Display.msg_Suspend()
//            }
            m_bSuspended = true
        }
    }
    fun Resume() {
        println("+ iGXApp::Resume() : ")
        if (m_bSuspended) {
//            if (OS_WINCE) {
//                // Clear key-press states
//                m_Input.ClearKeyState()
//                if (!HPC_JORNADA) {
//                    //::SHFullScreen( window.GetHWND(), SHFS_HIDETASKBAR | SHFS_HIDESIPBUTTON | SHFS_HIDESTARTICON )
//                    ShellFullscreen(
//                        m_Window.GetHWND(),
//                        SHFS_HIDETASKBAR or SHFS_HIDESIPBUTTON or SHFS_HIDESTARTICON
//                    )
//                }
//                if (m_SoundPlayer.Inited()) {
//                    m_SoundPlayer.Close()
//                    m_SoundPlayer.Init()
//                }
//                m_Display.msg_Resume()
//                m_pGame?.OnResume() ?: check(false)
//            }
            m_bSuspended = false
        }
    }

    fun KeyDown(key: iKbdKey) {
        val game = checkNotNull(m_pGame)
        game.OnKeyDown(key)
    }

    fun KeyUp(key: iKbdKey) {
        val game = checkNotNull(m_pGame)
        game.OnKeyUp(key)
    }

    fun Window(): iWindow = m_Window
    fun ViewMgr(): iViewMgr = m_viewMgr
    fun Input(): iInput = m_Input
    fun Timer(): iTimer = m_Timer
    fun Display(): iDisplay = m_Display
    fun Surface(): iDib = m_Display.GetSurface()
    fun IsActive(): Boolean = m_bInited && !m_bSuspended

    fun SetOrientation(bLandscape: Boolean, bLeftHander: Boolean) {
        if (bLandscape && (m_Flags and GXLF_LANDSCAPE) == 0u) {
            m_Flags = m_Flags or GXLF_LANDSCAPE
        } else if (!bLandscape && (m_Flags and GXLF_LANDSCAPE) != 0u) {
            m_Flags = m_Flags xor GXLF_LANDSCAPE
        }

        // todo: need to swap coords?
//        if (OS_WIN32) {
//            val csiz = m_Window.GetSize()
//            csiz.w = csiz.h.also { csiz.h = csiz.w }
//            m_Window.SetSize(csiz)
//        }

        m_Input.SetOrientation(bLandscape, bLeftHander)
        m_Display.SetOrientation(bLandscape, bLeftHander)
    }

    fun ToggleOrientation() {
        SetOrientation(!IsLandscape(), (m_Flags and GXLF_LHANDER) != 0u)
    }

    fun IsLandscape(): Boolean = (m_Flags and GXLF_LANDSCAPE) != 0u
}
