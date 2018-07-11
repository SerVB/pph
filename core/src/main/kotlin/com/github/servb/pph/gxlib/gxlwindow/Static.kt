package com.github.servb.pph.gxlib.gxlwindow

import com.github.servb.pph.gxlib.gxlapplication.iGXApp
import com.github.servb.pph.gxlib.gxlmetrics.Point
import com.github.servb.pph.gxlib.gxlmetrics.Rect
import com.github.servb.pph.gxlib.gxlmetrics.Size
import com.github.servb.pph.util.helpertype.*
import unsigned.Uint
import unsigned.ui

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
        check(m_hWnd == null)
        check(owner != 0)
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
            val wrc = Rect(100, 100, 100.ui + m_Size.w, 100.ui + m_Size.h)
            val dwStyle = WS_OVERLAPPED or WS_CAPTION or WS_SYSMENU
            AdjustWindowRect(wrc, dwStyle, false)
            m_hWnd = CreateWindow(wndClassName, wndName, dwStyle,
                    wrc.left, wrc.top, wrc.right - wrc.left, wrc.bottom - wrc.top, 0L, 0, hInst, 0)
        }
        if (m_hWnd == null) {
            return false
        }
        SetWindowLong(m_hWnd, GWL_USERDATA, this)
        m_bActive = true
        if (OS_WIN32) {
            ShowWindow(m_hWnd, SW_SHOW)
        }
        return true;
    }
    fun Destroy() {
        check(m_hWnd != null)
        DestroyWindow(m_hWnd)
        m_hWnd = null
    }

    fun OnMessage(hWnd: HWND, uMsg: Uint, wParam: WPARAM, lParam: LPARAM): LRESULT {
        val pt = Point()
        when (uMsg) {
            WM_KILLFOCUS -> {
                m_pOwner?.Suspend()
                return 0
            }
            WM_SETFOCUS -> {
                m_pOwner?.Resume()
                return 0
            }
            /*
            case WM_ACTIVATE:
                if (m_pOwner) {
                    if (LOWORD(wParam) == WA_ACTIVE || LOWORD(wParam) == WA_CLICKACTIVE) {
                        OutputDebugString(L"+ WM_ACTIVATE 1\n");
                         m_pOwner->Resume();
                    } else if (LOWORD(wParam) == WA_INACTIVE) {
                        OutputDebugString(L"- WM_ACTIVATE 0\n");
                        m_pOwner->Suspend();
                    } else {
                        check(0 == "Invalid wParam in WM_ACTIVATE");
                    }

                }
                return 0;
            */
            WM_ERASEBKGND -> {
                return 0
            }
            WM_PAINT -> {
                m_pOwner?.IsActive?.let {
                    if (it) {
                        val ps = PAINTSTRUCT()
                        val hdc = BeginPaint(hWnd, ps)
                        m_pOwner?.Display?.msg_OnPaint(hdc)
                        EndPaint(m_hWnd, ps)
                    }
                }
                return 0
            }
            WM_LBUTTONDOWN -> {
                if (m_bTrack) {
                    return 0
                }
                SetCapture(hWnd)
                if (m_pOwner != null) {
                    pt.x = LOWORD(lParam)
                    pt.y = HIWORD(lParam)
                    //ClientToScreen(m_hWnd, &pt)
                    m_pOwner.Input().msg_OnMouseDown(pt.x, pt.y)
                }
                m_bTrack = true
                return 0
            }
            WM_LBUTTONUP -> {
                if (!m_bTrack) {
                    return 0
                }
                ReleaseCapture()
                if (m_pOwner != null) {
                    pt.x = LOWORD(lParam)
                    pt.y = HIWORD(lParam)
                    //ClientToScreen(m_hWnd, &pt)
                    m_pOwner.Input().msg_OnMouseUp(pt.x, pt.y)
                }
                m_bTrack = false
                return 0
            }
            WM_RBUTTONDOWN -> {
                if (m_pOwner != null) m_pOwner.Input().msg_OnKeyDown(VK_CLEAR)
                return 0
            }
            WM_RBUTTONUP -> {
                if (m_pOwner != null) m_pOwner.Input().msg_OnKeyUp(VK_CLEAR)
                return 0
            }
            WM_MOUSEMOVE -> {
                if (m_pOwner != null && (wParam and MK_LBUTTON)) {
                    pt.x = LOWORD(lParam)
                    pt.y = HIWORD(lParam)
                    //ClientToScreen(m_hWnd, &pt)
                    m_pOwner.Input().msg_OnMouseMove(pt.x, pt.y)
                }
                return 0
            }
            WM_KEYDOWN -> {
                if (m_pOwner != null && (lParam and 0x40000000) == 0) {
                    m_pOwner.Input().msg_OnKeyDown(wParam)
                }
                return 0
            }
            WM_KEYUP -> {
                if (m_pOwner != null) m_pOwner.Input().msg_OnKeyUp(wParam)
                return 0
            }
            WM_CLOSE -> {
                PostQuitMessage(0)
                return 0
            }
        }

        return DefWindowProc(hWnd, uMsg, wParam, lParam)
    }

    fun SetOwner(pApp: iGXApp) {
        m_pOwner = pApp
    }

    fun SetSize(siz: IConstSize) {
        m_Size = Size(siz)
        if (OS_WIN32) {
            val wrc = Rect(100, 100, 100.ui + siz.w, 100.ui + siz.h)
            AdjustWindowRect(wrc, GetWindowLong(m_hWnd, GWL_STYLE), false)
            SetWindowPos(m_hWnd, 0, 0, 0, wrc.right - wrc.left, wrc.bottom - wrc.top,
                    SWP_NOACTIVATE or SWP_NOOWNERZORDER or SWP_NOZORDER or SWP_NOMOVE)
        }
    }

    fun GetSize() = Size(m_Size)

    fun GetHWND() = m_hWnd

    private lateinit var m_Size: Size
    private val m_bTrack: Boolean
    private var m_bActive: Boolean
    private val m_hWnd: HWND?
    private var m_pOwner: iGXApp?
}
