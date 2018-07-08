package com.github.servb.pph.gxlib.gxlwindow

import com.github.servb.pph.gxlib.gxlapplication.iGXApp
import com.github.servb.pph.gxlib.gxlmetrics.IConstSize
import com.github.servb.pph.gxlib.gxlmetrics.Size
import com.github.servb.pph.util.helpertype.*
import com.github.servb.pph.util.staticfunction.Tracer

interface iDispMsgHnd {
    fun msg_Suspend()
    fun msg_Resume()
    fun msg_OnPaint(hdc: HDC)
}

interface iInputMsgHnd  {
    fun msg_OnKeyDown(key_code: Short)
    fun msg_OnKeyUp(key_code: Short)
    fun msg_OnMouseMove(px: Short, py: Short)
    fun msg_OnMouseDown(px: Short, py: Short)
    fun msg_OnMouseUp(px: Short, py: Short)
}

const val wndClassName = "GXLMWND"

class iWindow {
    constructor() {
        m_hWnd = null
        m_pOwner = null
        m_bActive = false
        m_bTrack = false
    }

    fun Init(hInst: HINSTANCE, wndName: LPCWSTR, siz: IConstSize, pApp: iGXApp): Boolean {
        Tracer.check(m_hWnd == null)
        Tracer.check(owner != 0)
        m_pOwner = owner
        // SiGMan: this one is proper (for PPC) app single instance check
        // first check if such window already exists (application is running)
        // NB:: Somehow in the windows application does not have any caption and search fails
        // 2ROBERT: WHY THERE IS NO WINDOW TITLE DISPLAYED???
        val hOtherWnd: HWND? = FindWindow(wndClassName, null)
        // this parameter belongs to config actually
        val allowMultipleInstances = false
        if (hOtherWnd != null && !allowMultipleInstances) {
            SetForegroundWindow(hOtherWnd)
            return false
        }
        // Create Window
        val wndClass = WNDCLASS(CS_HREDRAW or CS_VREDRAW, WndProc, 4, 4, hInst,
                null, LoadCursor(null, IDC_ARROW), (HBRUSH)GetStockObject(NULL_BRUSH),
                null, wndClassName)
        RegisterClass(wndClass)
        m_Size = Size(siz)
        if (OS_WINCE) {
            m_hWnd = CreateWindow(wndClassName, wndName,
                    WS_VISIBLE, 0, 0, m_Size.w, m_Size.h, 0L, 0, hInst, 0)
            if (!HPC_JORNADA) {
                //::SHFullScreen( m_hWnd, SHFS_HIDETASKBAR | SHFS_HIDESIPBUTTON | SHFS_HIDESTARTICON );
                owner.ShellFullscreen(m_hWnd,
                        SHFS_HIDETASKBAR or SHFS_HIDESIPBUTTON or SHFS_HIDESTARTICON);
            }
        }
        if (OS_WIN32) {
            RECT wrc ={ 100, 100, 100+m_Size.w, 100+m_Size.h };
            DWORD dwStyle = WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU;
            AdjustWindowRect(& wrc, dwStyle, FALSE);
            m_hWnd = ::CreateWindow(wndClassName, wndName, dwStyle, wrc.left, wrc.top, wrc.right - wrc.left, wrc.bottom - wrc.top, 0L, 0, hInst, 0);
        }
        if (!m_hWnd) return false;
        SetWindowLong(m_hWnd,GWL_USERDATA,(LONG)this);
        m_bActive = true;
        #ifdef OS_WIN32
                ShowWindow(m_hWnd, SW_SHOW);
        #endif
        return true;
    }
    fun Destroy()

    fun OnMessage(hWnd: HWND, uMsg: Uint, wParam: WPARAM, lParam: LPARAM): LRESULT
    fun SetOwner(pApp: iGXApp)
    fun SetSize(siz: IConstSize)

    fun GetSize() = Size(m_Size)

    fun GetHWND() = m_hWnd

    private lateinit var m_Size: Size
    private val m_bTrack: Boolean
    private val m_bActive: Boolean
    private val m_hWnd: HWND?
    private var m_pOwner: iGXApp?
}
