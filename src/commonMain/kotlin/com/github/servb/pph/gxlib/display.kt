package com.github.servb.pph.gxlib

import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.image
import com.soywiz.korge.view.xy
import com.soywiz.korma.geom.IRectangleInt
import com.soywiz.korma.geom.ISizeInt
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.setTo
import kotlin.properties.Delegates

// ported implementation from gxl.display.w32.cpp
class iDisplay /*: iDispMsgHnd */ {

    private val m_BackBuff: iDib = iDib()
    private var m_gamma: Double
    private val m_Siz: SizeInt = SizeInt()
    private var m_Flags: UInt by Delegates.notNull()
    private var m_bInited: Boolean

    private fun RebuildGammaTables() {
        // todo
    }

    constructor() {
        m_gamma = 1.0
        m_bInited = false
    }

    fun `$destruct`() {
        check(!m_bInited)
        Destroy()
    }

    fun Init(siz: ISizeInt, flags: UInt, stage: Stage): Boolean {
        check(!m_bInited)

        m_Flags = flags
        m_Siz.setTo(siz)
        m_BackBuff.Init(m_Siz, IiDib.Type.RGB)
        // todo: seems like there is one more layer of abstraction.
        //       for now we use iDib.backingBitmap but in the future can remove this property and add one more layer
        //       and support DOUBLESIZE rendering
//        if (flags & GXLF_DOUBLESIZE) m_memDC.InitDC(iSize(siz.w*2,siz.h*2));
//        else m_memDC.InitDC(siz);

        stage
            .image(m_BackBuff.backingBitmap)
            .xy(0, 0)
            .apply { smoothing = false }

        m_bInited = true

        return true
    }

    fun SetGamma(gamma: Double) {
        m_gamma = gamma
        RebuildGammaTables()
    }

    fun SetOrientation(bLandscape: Boolean, bLefthandler: Boolean) {
        // todo
    }

    fun Destroy() {
        check(m_bInited)
        m_bInited = false
    }

//    void msg_OnPaint(HDC hdc);
//    void msg_Suspend();
//    void msg_Resume();

    fun DoPaint(rc: IRectangleInt) {
        // skipped one more layer of abstraction for now (see comment in Init)

        ++m_BackBuff.backingBitmap.contentVersion
    }

    fun GetSurface(): iDib = m_BackBuff
    fun SurfMetrics(): SizeInt =
        m_BackBuff.GetSize()  // the method is marked as const, but there is no const iDisplay used in sources
}
