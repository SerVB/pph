package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.common.SurfaceType
import com.soywiz.korma.geom.IPointInt

class iInteractBattle : iBattleWrapper {

    private var m_pBattleView: iBattleView?
    private lateinit var m_surfType: SurfaceType

    constructor() : super(false) {
        m_pBattleView = null
    }

    override suspend fun OnBeginBattle() {
        gGame.ShowView(iChildGameView.CHILD_VIEW.BATTLE)
        m_pBattleView = gGame.View(iChildGameView.CHILD_VIEW.BATTLE) as iBattleView
        checkNotNull(m_pBattleView)
        // todo:
//        val dpos = m_engine.GetBattleInfo().Defender().Pos()
//        iCell c = gGame.Map().GetAt(dpos.x,dpos.y);
//	      sint32 upSurf = iMAX(iMAX(iMAX(c.SurfNode(0),c.SurfNode(1)),c.SurfNode(2)),c.SurfNode(3));
        m_surfType = SurfaceType.GRASS
        m_pBattleView!!.BeginBattle(this, m_surfType)
    }

    override fun OnStart() {
        m_pBattleView!!.Start()
    }

    override suspend fun OnEndBattle() {
        gGame.HideView(iChildGameView.CHILD_VIEW.BATTLE)
        m_pBattleView = null
    }

    override fun AddLogEvent(msg: String) {
        m_pBattleView?.AddLogEvent(msg)
    }

    override fun AddCellEvent(msg: String, pos: IPointInt) {
        m_pBattleView?.AddCellEvent(msg, pos)
    }
}