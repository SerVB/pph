package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.*
import com.github.servb.pph.pheroes.common.TextResId
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.asPoint
import com.github.servb.pph.util.invoke
import com.soywiz.klock.DateTime
import com.soywiz.kmem.buildByteArray
import com.soywiz.korio.file.std.localCurrentDirVfs
import com.soywiz.korma.geom.*

class iHighScore {

    class iEntry(val land: String, val date: UInt, val days: UInt, val score: UInt)

    private val m_entries: MutableList<iEntry> = mutableListOf()

    fun Reset() {
        m_entries.clear()
    }

    suspend fun Load(fname: String) {
        val file = localCurrentDirVfs[fname]
        if (file.exists()) {
            val stream = file.openInputStream()
            val cnt = stream.ReadS32()
            repeat(cnt) {
                val entry = iEntry(
                    stream.ReadString(),
                    stream.ReadU32(),
                    stream.ReadU32(),
                    stream.ReadU32(),
                )
                m_entries.add(entry)
            }
        }
        if (m_entries.size < 10) {  // todo: remove these debug entries
            val tsOfImplementation = DateTime(2021, 1, 14)
            m_entries.add(iEntry("your ad here :)", tsOfImplementation.toTimestamp(), 12u, 1024u))
        }
    }

    suspend fun Save(fname: String) {
        val data = buildByteArray {
            Write(m_entries.size)
            m_entries.forEach {
                Write(it.land)
                Write(it.date)
                Write(it.days)
                Write(it.score)
            }
        }
        localCurrentDirVfs[fname].write(data)
    }

    fun AddEntry(entry: iEntry): Int {
        var idx = 0
        while (idx < m_entries.size) {
            if (entry.score > m_entries[idx].score) {
                m_entries.add(idx, entry)
                if (m_entries.size > 10) {
                    m_entries.removeAt(10)
                }
                return idx
            }

            ++idx
        }
        if (m_entries.size < 10) {
            m_entries.add(entry)
            return m_entries.lastIndex
        }
        return -1
    }

    fun Count(): SizeT = m_entries.size

    fun Entry(idx: SizeT): iEntry = m_entries[idx]
}

class iDlg_HallOfFame : iBaseGameDlg {

    companion object {

        suspend fun construct(pViewMgr: iViewMgr, fname: String): iDlg_HallOfFame {
            val highScore = iHighScore()
            highScore.Load(fname)
            return iDlg_HallOfFame(pViewMgr, fname, highScore)
        }

        suspend fun construct(pViewMgr: iViewMgr, fname: String, entry: iHighScore.iEntry): iDlg_HallOfFame {
            val highScore = iHighScore()
            highScore.Load(fname)
            val curScoreId = highScore.AddEntry(entry)
            highScore.Save(fname)
            return iDlg_HallOfFame(pViewMgr, fname, highScore, curScoreId)
        }
    }

    private val m_curScore: SizeT
    private val m_fname: String
    private val m_hScore: iHighScore

    private constructor(pViewMgr: iViewMgr, fname: String, highScore: iHighScore) : super(pViewMgr, PlayerId.NEUTRAL) {
        m_fname = fname
        m_curScore = -1
        m_hScore = highScore
    }

    private constructor(pViewMgr: iViewMgr, fname: String, highScore: iHighScore, curScoreId: SizeT) : super(
        pViewMgr,
        PlayerId.NEUTRAL
    ) {
        m_fname = fname
        m_curScore = curScoreId
        m_hScore = highScore
    }

    override fun OnCreateDlg() {
        val clRect = ClientRect()

        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x + clRect.width / 2 - 55, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_OK,
                DLG_RETCODE.OK.v.toUInt()
            )
        )
        AddChild(
            iTextButton(
                m_pMgr,
                this,
                IRectangleInt(clRect.x + clRect.width / 2 + 5, clRect.y2 - DEF_BTN_HEIGHT, 50, DEF_BTN_HEIGHT),
                TextResId.TRID_RESET,
                1000u
            )
        )
    }

    override fun DoCompose(clRect: IRectangleInt) {
        val rc = RectangleInt(clRect)

        // title
        gTextComposer.TextOut(
            dlgfc_hdr,
            gApp.Surface(),
            rc.position,
            gTextMgr[TextResId.TRID_MENU_HIGHSCORE],
            IRectangleInt(rc.x, rc.y, rc.width, 15),
            Alignment.AlignCenter
        )
        rc.y += 17

        // header
        gTextComposer.TextOut(
            dlgfc_topic,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_HOF_RANK],
            IRectangleInt(rc.x, rc.y, 30, 15),
            Alignment.AlignTop
        )
        gTextComposer.TextOut(
            dlgfc_topic,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_HOF_LAND],
            IRectangleInt(rc.x + 30, rc.y, 120, 15),
            Alignment.AlignTop
        )
        gTextComposer.TextOut(
            dlgfc_topic,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_HOF_DATE],
            IRectangleInt(rc.x + 30 + 120, rc.y, 50, 15),
            Alignment.AlignTop
        )
        gTextComposer.TextOut(
            dlgfc_topic,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_HOF_DAYS],
            IRectangleInt(rc.x + 30 + 120 + 50, rc.y, 40, 15),
            Alignment.AlignTop
        )
        gTextComposer.TextOut(
            dlgfc_topic,
            gApp.Surface(),
            rc.asPoint(),
            gTextMgr[TextResId.TRID_HOF_SCORE],
            IRectangleInt(rc.x + 30 + 120 + 50 + 40, rc.y, 40, 15),
            Alignment.AlignTop
        )
        rc.y += 15

        // entries
        var eid = 0
        val fc = iTextComposer.FontConfig(iTextComposer.FontSize.MEDIUM, IiDibFont.ComposeProps(RGB16(192, 192, 192)))
        while (eid < m_hScore.Count() && eid < 10) {
            if (eid == m_curScore) {
                fc.cmpProps.faceColor = RGB16(255, 192, 128)
            } else {
                fc.cmpProps.faceColor = RGB16(192, 192, 192)
            }
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                "${eid + 1}.",
                IRectangleInt(rc.x, rc.y, 30, 15),
                Alignment.AlignTop
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                m_hScore.Entry(eid).land,
                IRectangleInt(rc.x + 30, rc.y, 120, 15),
                Alignment.AlignTopLeft
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                FormatDate(m_hScore.Entry(eid).date, false),
                IRectangleInt(rc.x + 30 + 120, rc.y, 50, 15),
                Alignment.AlignTop
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                m_hScore.Entry(eid).days.toString(),
                IRectangleInt(rc.x + 30 + 120 + 50, rc.y, 40, 15),
                Alignment.AlignTop
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                m_hScore.Entry(eid).score.toString(),
                IRectangleInt(rc.x + 30 + 120 + 50 + 40, rc.y, 40, 15),
                Alignment.AlignTop
            )
            rc.y += 14

            ++eid
        }

        fc.cmpProps.faceColor = RGB16(192, 192, 192)
        while (eid < 10) {
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                "${eid + 1}.",
                IRectangleInt(rc.x, rc.y, 30, 15),
                Alignment.AlignTop
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                "-",
                IRectangleInt(rc.x + 30, rc.y, 120, 15),
                Alignment.AlignTopLeft
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                "-",
                IRectangleInt(rc.x + 30 + 120, rc.y, 50, 15),
                Alignment.AlignTop
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                "-",
                IRectangleInt(rc.x + 30 + 120 + 50, rc.y, 40, 15),
                Alignment.AlignTop
            )
            gTextComposer.TextOut(
                fc,
                gApp.Surface(),
                rc.asPoint(),
                "-",
                IRectangleInt(rc.x + 30 + 120 + 50 + 40, rc.y, 40, 15),
                Alignment.AlignTop
            )
            rc.y += 14

            ++eid
        }
    }

    override fun ClientSize(): SizeInt {
        return SizeInt(280, 180 + DEF_BTN_HEIGHT)
    }

    override suspend fun iCMDH_ControlCommand(pView: iView, cmd: CTRL_CMD_ID, param: Int) {
        val uid = pView.GetUID()
        when (uid) {
            DLG_RETCODE.OK.v.toUInt() -> EndDialog(DLG_RETCODE.OK.v)
            1000u -> {
                val qdlg = iQuestDlg(gApp.ViewMgr(), "", gTextMgr[TextResId.TRID_MSG_RESET_HIGHSCORE], PlayerId.RED)
                if (qdlg.DoModal() == DLG_RETCODE.YES.v) {
                    m_hScore.Reset()
                    m_hScore.Save(m_fname)
                }
            }
        }
    }
}
