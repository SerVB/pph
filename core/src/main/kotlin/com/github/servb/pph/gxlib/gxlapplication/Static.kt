package com.github.servb.pph.gxlib.gxlapplication

import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxlmetrics.Size
import com.github.servb.pph.util.helpertype.*

interface IGame {
    fun Process(t: fix32): Int
    fun OnKeyDown(key: Int)
    fun OnKeyUp(key: Int)
    fun OnSuspend()
    fun OnResume()
}

class iGXApp {

    fun Init(hInst: HINSTANCE, appName: LPCWSTR, pGame: IGame, cdelay: UInt, flags: UInt): Boolean {
        var flags = flags
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
            if (!m_Window.Init(hInst, appName, osiz, this)) break
            if (!m_Input.Init(osiz, flags)) break
            if (!m_Timer.Init()) break
            if (!m_Display.Init(m_Window, scrsiz, flags)) break
            if ((m_Flags and Inc.GXLF_ENABLESOUND.v != 0) && !m_SoundPlayer.Init()) break
            m_Window.SetOwner(this)
            m_bInited = true
        } while (false)

        if (!m_bInited) {
            m_SoundPlayer.Close()
            m_Display.Destroy()
            m_Input.Destroy()
            m_Window.Destroy()
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
        FreeLibrary( m_aygDLL )

        m_Display.Destroy()
        m_Input.Destroy()
        m_Window.Destroy()
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

        while (m_Input.EntryCount()) {
            m_viewMgr.ProcessMessage(m_Input.Get())
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
            m_viewMgr.Process(step)
            if (step + m_processTimer > m_CycleDelay) {
                m_processTimer = 0
                val rc = Rect()
                m_viewMgr.Compose(rc)
                if (!rc.IsEmpty()) {
                    m_Display.DoPaint(rc)
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
            ShowWindow(m_Window.GetHWND(), SW_MINIMIZE)

            if (!HPC_JORNADA) {
                ShellFullscreen(m_Window.GetHWND(), SHFS_SHOWTASKBAR or SHFS_SHOWSIPBUTTON or SHFS_SHOWSTARTICON)
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
                m_Display.msg_Suspend()
            }
            m_bSuspended = true
        }
    }
    fun Resume() {
        println("+ iGXApp::Resume() : ")
        if (m_bSuspended) {
            if (OS_WINCE) {
                // Clear key-press states
                m_Input.ClearKeyState()
                if (!HPC_JORNADA) {
                    //::SHFullScreen( m_Window.GetHWND(), SHFS_HIDETASKBAR | SHFS_HIDESIPBUTTON | SHFS_HIDESTARTICON )
                    ShellFullscreen(m_Window.GetHWND(),
                            SHFS_HIDETASKBAR or SHFS_HIDESIPBUTTON or SHFS_HIDESTARTICON)
                }
                if (m_SoundPlayer.Inited()) {
                    m_SoundPlayer.Close()
                    m_SoundPlayer.Init()
                }
                m_Display.msg_Resume()
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

    var Window: iWindow  // TODO: Join w/ m_Window?
        get() = m_Window
        set(value) {
            m_Window = value
        }

    var ViewMgr: iViewMgr  // TODO: Join w/ m_viewMgr?
        get() = m_viewMgr
        set(value) {
            m_viewMgr = value
        }

    var Input: iInput  // TODO: Join w/ m_Input?
        get() = m_Input
        set(value) {
            m_Input = value
        }

    var Timer: iTimer  // TODO: Join w/ m_Timer?
        get() = m_Timer
        set(value) {
            m_Timer = value
        }

    var Display: iDisplay  // TODO: Join w/ m_Display?
        get() = m_Display
        set(value) {
            m_Display = value
        }

    var Surface: iDib  // TODO: Join w/ m_Display.GetSurface?
        get() = m_Display.GetSurface
        set(value) {
            m_Display.GetSurface = value
        }

    var SndPlayer: iSoundPlayer  // TODO: Join w/ m_SoundPlayer?
        get() = m_SoundPlayer
        set(value) {
            m_SoundPlayer = value
        }

    val IsActive get() = m_bInited && !m_bSuspended

    fun SetOrientation(bLandscape: Boolean, bLeftHander: Boolean) {
        if (bLandscape && m_Flags and GXLF_LANDSCAPE == 0) {
            m_Flags = m_Flags or GXLF_LANDSCAPE
        } else if (!bLandscape && m_Flags and GXLF_LANDSCAPE != 0) {
            m_Flags = m_Flags xor GXLF_LANDSCAPE
        }

        if (OS_WIN32) {
            val csiz = m_Window.GetSize()
            csiz.w = csiz.h.also { csiz.h = csiz.w }
            m_Window.SetSize(csiz)
        }

        m_Input.SetOrientation(bLandscape, bLeftHander)
        m_Display.SetOrientation(bLandscape, bLeftHander)
    }

    fun ToggleOrientation() {
        SetOrientation(!IsLandscape, m_Flags and GXLF_LHANDER != 0)
    }

    val IsLandscape get() = m_Flags and GXLF_LANDSCAPE != 0

    fun ShellFullscreen(p1: HWND, p2: DWORD): Boolean {
        /*if (m_shfullscreen != 0) {  // TODO: winapi?
            return ((BOOL (*)(HWND, DWORD))m_shfullscreen)( p1, p2 )
        }*/
        return false
    }

    protected var m_BaseMetrics: Size
    protected var m_Flags: UInt
    protected var m_CycleDelay: UInt
    protected var m_processTimer: UInt
    protected var m_bExit: Boolean

    protected var m_pGame: IGame?
    protected var m_Window: iWindow
    protected var m_Input: iInput
    protected var m_Timer: iTimer
    protected var m_Display: iDisplay

    protected var m_viewMgr: iViewMgr
    protected var m_SoundPlayer: iSoundPlayer
    protected var m_bInited: Boolean
    protected var m_bSuspended: Boolean

    protected var m_aygDLL: HINSTANCE
    protected var m_coreDLL: HINSTANCE

    protected var m_shfullscreen: VOID_STAR
    protected var m_sysidletimer: VOID_STAR

    init {
        m_viewMgr = this
        m_pGame = null
        m_bInited = false
        m_bSuspended = false
        m_bExit = false
        m_CycleDelay = 30
        m_processTimer = 0
    }
}
