package com.github.servb.pph.gxlib

import com.soywiz.korev.Key
import com.soywiz.korev.MouseButton
import com.soywiz.korge.input.MouseEvents
import com.soywiz.korge.input.keys
import com.soywiz.korge.input.mouse
import com.soywiz.korge.view.Stage
import com.soywiz.korgw.GameWindow
import com.soywiz.korma.geom.ISizeInt
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.setTo

// todo: not needed until screen suspension is implemented
//interface iDispMsgHnd {
//    fun msg_Suspend()
//    fun msg_Resume()
//    fun msg_OnPaint(hdc: HDC)
//}

interface iInputMsgHnd {

    fun msg_OnKeyDown(key: Key)
    fun msg_OnKeyUp(key: Key)
    fun msg_OnMouseMove(px: Int, py: Int)
    fun msg_OnMouseDown(px: Int, py: Int)
    fun msg_OnMouseUp(px: Int, py: Int)
}

class iWindow {

    private val m_Size: SizeInt = SizeInt()
    private var m_bTrack: Boolean
    private var m_bActive: Boolean
    private lateinit var m_pOwner: iGXApp
    private lateinit var window: GameWindow

    constructor() {
        m_bActive = false
        m_bTrack = false
    }

    private sealed class WindowMessage {

        object WM_KILLFOCUS : WindowMessage()  // todo
        object WM_SETFOCUS : WindowMessage()  // todo
        class WM_LBUTTONDOWN(val x: Int, val y: Int) : WindowMessage()
        class WM_LBUTTONUP(val x: Int, val y: Int) : WindowMessage()
        object WM_RBUTTONDOWN : WindowMessage()
        object WM_RBUTTONUP : WindowMessage()
        class WM_MOUSEMOVE(val x: Int, val y: Int, val lmbIsPressed: Boolean) : WindowMessage()
        class WM_KEYDOWN(val key: Key) : WindowMessage()  // todo
        class WM_KEYUP(val key: Key) : WindowMessage()  // todo
    }

    private fun Stage.WndProc() {
        mouse {
            down {
                when (it.button) {
                    MouseButton.LEFT -> OnMessage(WindowMessage.WM_LBUTTONDOWN(it.x, it.y))
                    MouseButton.RIGHT -> OnMessage(WindowMessage.WM_RBUTTONDOWN)
                }
            }
            up {
                when (it.button) {
                    MouseButton.LEFT -> OnMessage(WindowMessage.WM_LBUTTONUP(it.x, it.y))
                    MouseButton.RIGHT -> OnMessage(WindowMessage.WM_RBUTTONUP)
                }
            }
            move {
                OnMessage(WindowMessage.WM_MOUSEMOVE(it.x, it.y, it.button == MouseButton.LEFT))
            }
        }
        keys {
            down {
                OnMessage(WindowMessage.WM_KEYDOWN(it.key))
            }
            up {
                OnMessage(WindowMessage.WM_KEYUP(it.key))
            }
        }
    }

    fun Init(siz: ISizeInt, pApp: iGXApp, stage: Stage): Boolean {
        m_pOwner = pApp
        // todo: check for multiple instanses (existed in sources), or can uncomment mutex in main function
        stage.WndProc()
        window = stage.gameWindow

        m_Size.setTo(siz)
        // todo: do we need switch to full screen?

        m_bActive = true
        return true
    }

    fun Destroy() {
        window.close()
    }

    private fun OnMessage(uMsg: WindowMessage) {
        when (uMsg) {
            is WindowMessage.WM_KILLFOCUS -> m_pOwner.Suspend()
            is WindowMessage.WM_SETFOCUS -> m_pOwner.Resume()
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
            is WindowMessage.WM_LBUTTONDOWN -> {
                if (m_bTrack) {
                    return
                }
                m_pOwner.Input().msg_OnMouseDown(uMsg.x, uMsg.y)
                m_bTrack = true
            }
            is WindowMessage.WM_LBUTTONUP -> {
                if (!m_bTrack) {
                    return
                }
                m_pOwner.Input().msg_OnMouseUp(uMsg.x, uMsg.y)
                m_bTrack = false
            }
            is WindowMessage.WM_RBUTTONDOWN -> m_pOwner.Input().msg_OnKeyDown(Key.CLEAR)
            is WindowMessage.WM_RBUTTONUP -> m_pOwner.Input().msg_OnKeyUp(Key.CLEAR)
            is WindowMessage.WM_MOUSEMOVE -> if (uMsg.lmbIsPressed) {  // seems like check for drag is happening
                m_pOwner.Input().msg_OnMouseMove(uMsg.x, uMsg.y)
            }
            is WindowMessage.WM_KEYDOWN -> //if (keyWasUnpressedBefore) {  // todo: why this check is needed in sources?
                m_pOwner.Input().msg_OnKeyDown(uMsg.key)
            //}
            is WindowMessage.WM_KEYUP -> m_pOwner.Input().msg_OnKeyUp(uMsg.key)
        }
    }

    fun SetOwner(pApp: iGXApp) {
        m_pOwner = pApp
    }

    fun SetSize(siz: ISizeInt) {
        m_Size.setTo(siz)
        // todo: make it work not just dummy
    }

    fun GetSize(): SizeInt = SizeInt(m_Size)  // const method

    private companion object {

        private val MouseEvents.x: Int get() = currentPosLocal.x.toInt()
        private val MouseEvents.y: Int get() = currentPosLocal.y.toInt()
    }
}
