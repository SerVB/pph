package com.github.servb.pph.gxlib.gxlapplication

import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxlmetrics.Size
import com.github.servb.pph.gxlib.gxlviewmgr.iViewMgr
import com.github.servb.pph.util.helpertype.*

interface IGame {
    fun process(t: Float): Int
    fun onKeyDown(key: Int)
    fun onKeyUp(key: Int)
    fun onSuspend()
    fun onResume()
}

class iGXApp {

    fun init(hInst: HINSTANCE, appName: LPCWSTR, pGame: IGame, cdelay: UInt, flags: UInt): Boolean {
        @Suppress("NAME_SHADOWING") var flags = flags
        check(!m_bInited)
        m_pGame = pGame
        m_CycleDelay = cdelay
        m_Flags = flags
        val scrsiz: MutableSize
        val osiz: WHHolder
        if (HPC_JORNADA) {
            m_BaseMetrics = Size(320, 240)
            flags = flags or Inc.GXLF_DEV_LANDSCAPE.v
            scrsiz = m_BaseMetrics
            osiz = m_BaseMetrics
        } else if (OS_WINCE) {
            m_BaseMetrics = Size(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN))
            if (m_BaseMetrics.w > m_BaseMetrics.h) {
                flags = flags or Inc.GXLF_DEV_LANDSCAPE.v
            }
            scrsiz = m_BaseMetrics
            osiz = m_BaseMetrics
        } else if (OS_WIN32) {
            m_BaseMetrics = if (m_Flags and Inc.GXLF_REALVGA.v != 0) Size(480, 640) else Size(240, 320)
            scrsiz = m_BaseMetrics
            if (m_Flags and Inc.GXLF_LANDSCAPE.v != 0) {
                scrsiz.w = m_BaseMetrics.h
                scrsiz.h = m_BaseMetrics.w
            }
            osiz = scrsiz
            if (m_Flags and Inc.GXLF_DOUBLESIZE.v != 0) {
                osiz.w *= 2
                osiz.h *= 2
            }
        }

        var bOk = false
        do {
            if (!window.Init(hInst, appName, osiz, this)) break
            if (!input.Init(osiz, flags)) break
            if (!m_Timer.Init()) break
            if (!display.Init(window, scrsiz, flags)) break
            if ((m_Flags and Inc.GXLF_ENABLESOUND.v != 0) && !m_SoundPlayer.Init()) break
            window.SetOwner(this)
            m_bInited = true
        } while (false)

        if (!m_bInited) {
            m_SoundPlayer.Close()
            display.Destroy()
            input.Destroy()
            window.Destroy()
        }

        if (OS_WINCE) {
            m_coreDLL = LoadLibrary(L"COREDLL.DLL")
            m_sysidletimer = (VOID_STAR) GetProcAddress (m_coreDLL, L"SystemIdleTimerReset")
            m_aygDLL = LoadLibrary(L"AYGSHELL.DLL")
            m_shfullscreen = 0
            if (m_aygDLL != 0) {
                m_shfullscreen = (VOID_STAR) GetProcAddress (m_aygDLL, L"SHFullScreen")
            }
        }

        return m_bInited
    }
    fun Destroy() {
        check(m_bInited) { "Can't destroy not inited app" }
        FreeLibrary(m_coreDLL)
        FreeLibrary(m_aygDLL)

        display.Destroy()
        input.Destroy()
        window.Destroy()
        m_pGame = null
        m_bInited = false
    }

    fun Cycle(): Boolean {
        /*val msg: MSG  // TODO: Only for winapi?
        if (PeekMessage( msg, null, 0, 0, PM_NOREMOVE)) {
            while (PeekMessage( &msg, NULL, 0U, 0U, PM_REMOVE)) {
            if ( msg.message == WM_QUIT ) return false;
            DispatchMessage( &msg );
        }
        }*/

        while (input.EntryCount()) {
            viewMgr.ProcessMessage(input.Get())
        }

        val m_LastUpdate: UInt = m_Timer.GetCurTime()  // TODO: static?
        val step: UInt = m_Timer.GetStep()

        /*if (OS_WINCE) { // TODO: winapi?
            val pwrTimeout = 0u  // TODO: static?
            pwrTimeout += step
            if (pwrTimeout >= 10000) {
                pwrTimeout = 0
                //SystemIdleTimerReset();
                ((void(WINAPI *)(void)) m_sysidletimer)();
            }
        }*/

        if (!m_bSuspended) {
            m_pGame?.Process(fix32(step.i) / 1000L) ?: check(false)
            viewMgr.Process(step)
            if (step + m_processTimer > m_CycleDelay) {
                m_processTimer = 0
                val rc = Rect()
                viewMgr.Compose(rc)
                if (!rc.IsEmpty()) {
                    display.DoPaint(rc)
                }
            } else {
                m_processTimer += step
            }
        }
        Thread.sleep(1)

        return true
    }

    fun Run(): Int {
        while (!m_bExit && Cycle()) {}
        return 0
    }

    fun Exit(code: UInt) {
        m_bExit = true
    }

    fun Minimize() {
        if (OS_WINCE) {
            ShowWindow(window.GetHWND(), SW_MINIMIZE)

            if (!HPC_JORNADA) {
                ShellFullscreen(window.GetHWND(), SHFS_SHOWTASKBAR or SHFS_SHOWSIPBUTTON or SHFS_SHOWSTARTICON)
            }
        }

        // TO Bring back discussion about proper way to minimize application
        // I should admit what there is definitely more than one way to do so:
        //
        // First is SHNavigateBack() API which is used to switch back to the
        // previously running application
        // Notably its not available on pure CE devices
        //
        // The other is implemented by bringing the 'Today' screen back foreground:
        // HWND hwnd = FindWindow(TEXT("DesktopExplorerWindow"), NULL);
        // if((NULL != hwnd) && (TRUE == IsWindow(hwnd)))
        // {
        //   ShowWindow(hwnd, SW_SHOWNA);
        //   SetForegroundWindow((HWND)(((DWORD)((HWND)hwnd)) | 0x01));
        // }
        //
        // >> PPC2003\Samples\Win32\GapiApp\GapiApp.cpp
    }
    fun Suspend() {
        if (!m_bSuspended) {
            if (OS_WINCE) {
                m_pGame?.OnSuspend() ?: check(false)
                display.msg_Suspend()
            }
            m_bSuspended = true
        }
    }
    fun Resume() {
        println("+ iGXApp::Resume() : ")
        if (m_bSuspended) {
            if (OS_WINCE) {
                // Clear key-press states
                input.ClearKeyState()
                if (!HPC_JORNADA) {
                    //::SHFullScreen( window.GetHWND(), SHFS_HIDETASKBAR | SHFS_HIDESIPBUTTON | SHFS_HIDESTARTICON )
                    ShellFullscreen(
                        window.GetHWND(),
                        SHFS_HIDETASKBAR or SHFS_HIDESIPBUTTON or SHFS_HIDESTARTICON
                    )
                }
                if (m_SoundPlayer.Inited()) {
                    m_SoundPlayer.Close()
                    m_SoundPlayer.Init()
                }
                display.msg_Resume()
                m_pGame?.OnResume() ?: check(false)
            }
            m_bSuspended = false
        }
    }

    fun KeyDown(key: Int) {
        check(m_pGame)
        m_pGame.OnKeyDown(key)
    }

    fun KeyUp(key: Int) {
        check(m_pGame)
        m_pGame.OnKeyUp(key)
    }

    val IsActive get() = m_bInited && !m_bSuspended

    fun SetOrientation(bLandscape: Boolean, bLeftHander: Boolean) {
        if (bLandscape && m_Flags and GXLF_LANDSCAPE == 0) {
            m_Flags = m_Flags or GXLF_LANDSCAPE
        } else if (!bLandscape && m_Flags and GXLF_LANDSCAPE != 0) {
            m_Flags = m_Flags xor GXLF_LANDSCAPE
        }

        if (OS_WIN32) {
            val csiz = window.GetSize()
            csiz.w = csiz.h.also { csiz.h = csiz.w }
            window.SetSize(csiz)
        }

        input.SetOrientation(bLandscape, bLeftHander)
        display.SetOrientation(bLandscape, bLeftHander)
    }

    fun ToggleOrientation() {
        SetOrientation(!IsLandscape, (m_Flags and GXLF_LHANDER) != 0)
    }

    val IsLandscape get() = (m_Flags and GXLF_LANDSCAPE) != 0

    fun ShellFullscreen(p1: HWND, p2: DWORD): Boolean {
        /*if (m_shfullscreen != 0) {  // TODO: winapi?
            return ((BOOL (*)(HWND, DWORD))m_shfullscreen)( p1, p2 )
        }*/
        return false
    }

    protected lateinit var m_BaseMetrics: Size
    protected var m_Flags: UInt = (-42).toUInt()
    protected var m_CycleDelay: UInt = (-42).toUInt()
    protected var m_processTimer: UInt = (-42).toUInt()
    protected var m_bExit: Boolean

    protected var m_pGame: IGame?
    lateinit var window: iWindow
    lateinit var input: iInput
    lateinit var timer: iTimer
    lateinit var display: iDisplay

    lateinit var viewMgr: iViewMgr
    lateinit var soundPlayer: iSoundPlayer
    protected var m_bInited: Boolean
    protected var m_bSuspended: Boolean

    protected var m_aygDLL: HINSTANCE
    protected var m_coreDLL: HINSTANCE

    protected var m_shfullscreen: VOID_STAR
    protected var m_sysidletimer: VOID_STAR

    init {
        m_pGame = null
        m_bInited = false
        m_bSuspended = false
        m_bExit = false
        m_CycleDelay = 30u
        m_processTimer = 0
    }
}
