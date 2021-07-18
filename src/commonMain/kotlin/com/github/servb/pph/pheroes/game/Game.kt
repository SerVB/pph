package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.IGame
import com.github.servb.pph.gxlib.IViewCmdHandler
import com.github.servb.pph.gxlib.iKbdKey
import com.github.servb.pph.gxlib.iTopmostView
import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.common.PlayerType
import com.github.servb.pph.util.helpertype.UndefinedCountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum
import com.soywiz.korma.geom.IPointInt

class iBlackBackView : iTopmostView {

    constructor() : super(gApp.ViewMgr())

    override fun OnCompose() {
        val rect = GetScrRect()
//        gGfxMgr.BlitTile(PDGG_BKTILE, gApp.Surface(), rect)  // todo
        gApp.Surface().Darken50Rect(rect)
    }
}

/** Generic GameView */
abstract class iChildGameView : iTopmostView, IViewCmdHandler {

    enum class CHILD_VIEW(override val v: Int) : UniqueValueEnum, UndefinedCountValueEnum {

        UNDEFINED(-1),
        MENU(0),
        OVERLAND(1),
        BATTLE(2),
        CASTLE(3),
        HERO(4),
        MEET(5),
        MINIMAP(6),
        COUNT(7),
    }

    private val m_parentView: CHILD_VIEW
    private val m_bEternal: Boolean

    constructor(bEternal: Boolean, parentView: CHILD_VIEW) : super(gApp.ViewMgr()) {
        m_bEternal = bEternal
        m_parentView = parentView
    }

    override fun `$destruct`() {}

    fun Eternal(): Boolean = m_bEternal
    fun ParentView(): CHILD_VIEW = m_parentView
    open suspend fun Process(t: Double): Boolean = true
    open fun OnActivate(bActivate: Boolean) {}
}

interface IMsgComposer {

    fun AddMsg(text: String)
}

class Game : IGame {

    private var m_bGoToMainMenu: Boolean

    //    private val m_itemMgr: iItemMgr = iItemMgr()  // todo
//    private val m_Map: iGameWorld = iGameWorld()  // todo
//    private val m_soundMap: iSoundMap = iSoundMap()  // todo
    private var m_pBattle: iBattleWrapper?
    private var m_bInited: Boolean
    private var m_bStarted: Boolean

    private var m_hmChannel: Int
    private var m_hmSound: UShort

    //    private var m_pMainView: iMainView?  // todo
    private val m_pChildView: Array<iChildGameView?>  // size == CHILD_VIEW.COUNT
    private var m_tActView: iChildGameView.CHILD_VIEW

    constructor() {
        m_bInited = false
        m_bStarted = false
        m_tActView = iChildGameView.CHILD_VIEW.UNDEFINED
//        m_pMainView = null  // todo
        m_pBattle = null
        m_bGoToMainMenu = false
        m_hmChannel = -1
        m_hmSound = 0xFFFFu
        m_pChildView = arrayOfNulls(iChildGameView.CHILD_VIEW.COUNT.v)
    }

    fun `$destruct`() {
        Cleanup()
    }

    suspend fun Init(): Boolean {
        check(!m_bInited)

//        if (!LoadResouses(m_itemMgr)) return false  // todo

        // todo: port this
//        if (iFile::Exists(gSavePath + _T("lastses.phs"))) {
//            iFilePtr pSaveFile = OpenWin32File(gSavePath + _T("lastses.phs"));
//            check(pSaveFile);
//            if (pSaveFile) {
//                uint32 fourcc; pSaveFile->Read(&fourcc,sizeof(fourcc));
//                iMapInfo mapInfo;
//                mapInfo.m_FileName = gSavePath + _T("lastses.phs");
//                mapInfo.m_bNewGame = false;
//                mapInfo.ReadMapInfo(pSaveFile.get());
//                mapInfo.ReorderPlayers();
//                if (StartNewGame(mapInfo, false)) {
//                    pSaveFile.reset();
//                    iFile::Delete(gSavePath + _T("lastses.phs"));
//                    return m_bInited = true;
//                }
//                pSaveFile.reset();
//                iFile::Delete(gSavePath + _T("lastses.phs"));
//            }
//        }

        ShowView(iChildGameView.CHILD_VIEW.MENU)
//        (m_pChildView[iChildGameView.CHILD_VIEW.MENU.v] as iMenuView).Start()  // todo

        m_bInited = true
        return true
    }

    fun Cleanup() {
        m_pChildView.forEach { it?.`$destruct`() }
        m_pChildView.fill(null)
        m_tActView = iChildGameView.CHILD_VIEW.UNDEFINED
        if (m_bInited) {
//            m_itemMgr.Cleanup()  // todo
            m_bInited = false
        }
    }

    fun Quit() {
        gApp.Exit(0u)
    }

    fun MainMenu() {
        m_bGoToMainMenu = true
    }

//    fun StartNewGame(mapInfo: IiMapInfo, bNewGame: Boolean)  // todo

    suspend fun ExitGame(bChangeView: Boolean) {
//        m_soundMap.Cleanup()  // todo
//        m_Map.CleanupGameWorld()  // todo
//        m_itemMgr.OnGameEnd()  // todo
        if (bChangeView) {
            ShowView(iChildGameView.CHILD_VIEW.MENU)
//            (m_pChildView[iChildGameView.CHILD_VIEW.MENU.v] as iMenuView).Start()  // todo
        }
    }

    fun Inited(): Boolean = m_bInited
    fun Started(): Boolean = m_bStarted
//    fun Map(): iGameWorld = m_Map  // todo
//    fun ItemMgr(): iItemMgr = m_itemMgr  // todo
//    fun SoundMap(): iSoundMap = m_soundMap  // todo

    // todo: get rid of suspend because it makes much of code suspend
    suspend fun ShowView(cv: iChildGameView.CHILD_VIEW) {
        check(cv != iChildGameView.CHILD_VIEW.UNDEFINED)
        if (cv == m_tActView) {
            return
        }

        // Delete/Hide old active view
        if (m_tActView != iChildGameView.CHILD_VIEW.UNDEFINED) {
            m_pChildView[m_tActView.v]!!.OnActivate(false)
            m_pChildView[m_tActView.v]!!.SetVisible(false)
            gApp.ViewMgr().SetCurView(null)
            if (cv == iChildGameView.CHILD_VIEW.HERO && m_tActView != iChildGameView.CHILD_VIEW.OVERLAND) {
                // do not delete parent view
            } else {
                if (m_tActView == iChildGameView.CHILD_VIEW.MINIMAP) {
//                    m_pMainView.Composer().CenterView((m_pChildView[m_tActView.v] as iMinimapView).GetCenterCell())  // todo
                }
                if (!m_pChildView[m_tActView.v]!!.Eternal() || cv == iChildGameView.CHILD_VIEW.MENU) {
                    m_pChildView[m_tActView.v]!!.`$destruct`()
                    m_pChildView[m_tActView.v] = null
                    if (m_tActView == iChildGameView.CHILD_VIEW.OVERLAND) {
//                        m_pMainView = null  // todo
                    }
                }
            }
        }

        // Create/Show new active view
        when (cv) {
            iChildGameView.CHILD_VIEW.MENU -> {
                check(m_pChildView[cv.v] == null)
                m_pChildView[cv.v] = iMenuView.construct()
            }
            iChildGameView.CHILD_VIEW.OVERLAND -> TODO()
            iChildGameView.CHILD_VIEW.BATTLE -> {
                check(m_pChildView[cv.v] == null)
                m_pChildView[cv.v] = iBattleView()
            }
            iChildGameView.CHILD_VIEW.CASTLE -> TODO()
            iChildGameView.CHILD_VIEW.HERO -> TODO()
            iChildGameView.CHILD_VIEW.MEET -> TODO()
            iChildGameView.CHILD_VIEW.MINIMAP -> TODO()
        }

        m_pChildView[cv.v]!!.OnActivate(true)
        gApp.ViewMgr().SetCurView(m_pChildView[cv.v])
        m_tActView = cv
    }

    suspend fun HideView(cv: iChildGameView.CHILD_VIEW) {
        check(cv != iChildGameView.CHILD_VIEW.UNDEFINED)
        check(cv != iChildGameView.CHILD_VIEW.OVERLAND)
        val view = checkNotNull(m_pChildView[cv.v])

        ShowView(view.ParentView())
    }

    fun View(view: iChildGameView.CHILD_VIEW): iChildGameView? =
        if (view == iChildGameView.CHILD_VIEW.UNDEFINED) null else m_pChildView[view.v]

    fun ActView(): iChildGameView? = View(m_tActView)

//    fun MainView(): iMainView = m_pMainView  // todo

    suspend fun BeginBattle(bi: iBattleInfo) {
        check(m_pBattle == null)

        check(bi.m_pAssaulter.Owner() != PlayerId.NEUTRAL) { "Assaulter cannot be neutral" }
        val pAssaulter = iPlayer()  //m_Map.FindPlayer(bi.m_pAssaulter.Owner())  // todo
        val pDefender = when (bi.m_pDefender.Owner() == PlayerId.NEUTRAL) {
            true -> null
            false -> iPlayer()  //m_Map.FindPlayer(bi.m_pDefender.Owner())  // todo
        }
        checkNotNull(pAssaulter)
        check(pAssaulter != pDefender)

        // Reset env sounds
        if (pAssaulter.PlayerType() == PlayerType.HUMAN) {
//            m_soundMap.ResetEnvSounds()  // todo
        }

        // If one of side is controlled by human, the battle must be interactive
        val bInt = pAssaulter.PlayerType() == PlayerType.HUMAN ||
                (pDefender != null && pDefender.PlayerType() == PlayerType.HUMAN)
        if (bInt) {
            // Computer player attack the not active human player, so, change the active player first  // todo
//            if (pDefender != null && pDefender.PlayerType() == PlayerType.HUMAN && pAssaulter.PlayerType() != PlayerType.HUMAN && pDefender != m_Map.ActPlayer()) {
//                m_Map.SetNewActor(pDefender, true)
//            }

//            OnHeroStopMoving(gGame.Map().CurHero())  // todo
            if (gSettings.GetEntryValue(ConfigEntryType.QUICKCOMBAT) != 0) {
                // Autobattle with result
                TODO()
//                check(gGame.Map().CurHero() == bi.m_pAssaulter.SpellCaster())
//                m_pBattle = iAutoBattle(true)
            } else {
                // Interactive battle
                m_pBattle = iInteractBattle()
            }
        } else {
            // Auto battle
            TODO()
//            m_pBattle = iAutoBattle()
        }

        m_pBattle!!.BeginBattle(bi)
    }

    fun EndBattle() {
        check(m_pBattle != null)
        m_pBattle = null
    }

//    fun MeetHeroes(pHero1: iHero, pHero2: iHero, bAct: Boolean)  // todo

    override suspend fun Process(t: Double): Int {
        if (m_bGoToMainMenu) {
            m_bGoToMainMenu = false
            ExitGame(true)
        } else if (ActView() != null && ActView()!!.Process(t) && m_tActView == iChildGameView.CHILD_VIEW.OVERLAND) {
//            m_Map.Process(t)  // todo
        }

        return 0
    }

    override fun OnKeyDown(key: iKbdKey) {
        // empty in sources
    }

    override fun OnKeyUp(key: iKbdKey) {
        if (key == gSettings.ActionKey(ButtonActionType.MINIMIZE_APP)) {
            gApp.Minimize()
        } else if (key == gSettings.ActionKey(ButtonActionType.MAKE_SCREENSHOT)) {
            // todo
//            iStringT fname = gRootPath + _T("screenshot.bmp");
//            SaveDibBitmap16(gApp.Surface(), fname);
//            AddMsg(iStringT(_T("#F4B4")) + gTextMgr[TRID_MSG_SCREENSHOT_SAVED]);
        }
    }

    override fun OnSuspend() {
        // todo
    }

    override fun OnResume() {
        // todo
    }

    fun AddMsg(msg: String) {
//        m_pMainView?.AddMsg(msg)  // todo
    }

    fun AddCellMsg(msg: String, pos: IPointInt) {
//        m_pMainView?.AddCellMsg(msg, pos)  // todo
    }

//    fun OnDisapObject(sid: SpriteId, pos: IPointInt, offset: IPointInt = PointInt())  // todo

//    fun OnVictory()  // todo

//    fun OnDefeat(bExitGame: Boolean)  // todo

//    fun OnActorChanged(pNewActor: iPlayer, bAttack: Boolean)  // todo

//    fun OnPlayerChanged(pNewPlayer: iPlayer, bAct: Boolean)  // todo

//    fun OnPlayerVanquished(pid: PLAYER_ID)  // todo

//    fun OnAddCastle(pCastle: iCastle)  // todo

//    fun OnDelCastle(pCastle: iCastle)  // todo

//    fun OnCastleChanged(pCastle: iCastle)  // todo

//    fun OnAddHero(pHero: iHero)  // todo

//    fun OnDelHero(pHero: iHero)  // todo

//    fun OnHeroChanged(pHero: iHero)  // todo

//    fun OnHeroLevel(pHero: iHero, level: UByte, linfo: iNewLevelInfo)  // empty and unused in sources

//    fun OnHeroMoveTo(pHero: iHero, step: iPath.iStep)  // todo

//    fun OnHeroStopMoving(pHero: iHero)  // todo

//    fun OnHeroPosChanged(pHero: iHero, npos: IPointInt)  // todo

//    fun OnHeroTeleport(pHero: iHero, src: IPointInt, dst: IPointInt)  // todo

//    fun OnAttackHero(pHero1: iHero, pHero2: iHero)  // empty and unused in sources
}
