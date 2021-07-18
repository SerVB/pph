package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.GXLF_LANDSCAPE
import com.github.servb.pph.gxlib.cColor
import com.github.servb.pph.gxlib.iDibReader
import com.github.servb.pph.gxlib.iGXApp
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.asRectangle
import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.Stage
import com.soywiz.korio.async.delay
import com.soywiz.korma.geom.PointInt
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.center
import com.soywiz.korma.geom.topLeft2

lateinit var gDataPath: String
lateinit var gSavePath: String
lateinit var gMapsPath: String
val gApp: iGXApp = iGXApp()
private val gDibReader: iDibReader = iDibReader()
val gTextComposer: iTextComposer = iTextComposer()

val gTextMgr: iTextManager = iTextManager()

val gGfxMgr: iGfxManager = iGfxManager()

//private val gSfxMgr: iSfxManager  // todo
val gGame: Game = Game()
val gSettings: iSettings = iSettings()

private suspend fun ShowProgressReport(curProg: SizeT, initial: Boolean = true) {
    val display = gApp.Display()
    val rcScreen = display.SurfMetrics().asRectangle()
    val ldrOrg = rcScreen.center
    ldrOrg.y += rcScreen.height / 3
    val pbarWidth = (rcScreen.width * 5) / 6
    val rcPbar = RectangleInt(ldrOrg.x - pbarWidth / 2, ldrOrg.y, pbarWidth, 10)
    val pbarColor: UShort = if (initial) {
        48u
    } else {
        127u
    }
    ComposeProgressBar(false, display.GetSurface(), rcPbar, pbarColor, curProg, 100)
    //gGfxMgr.Blit(12,gApp.Display().GetSurface(), iPoint(0,0) );  // commented in sources

    if (!initial) {
        rcPbar.y -= 16
        val loadingFC = iTextComposer.FontConfig(GetButtonFont(0))
        gTextComposer.TextOut(loadingFC, display.GetSurface(), rcPbar.topLeft2, "Prepare for battle...")
    }

    display.DoPaint(display.SurfMetrics().asRectangle())
    delay(1.milliseconds)
}

suspend fun WinMain(stage: Stage, cmdLine: String) {
    // skipped remaining memory check
    // skipped single instance mutex

    gDataPath = "Game/Data"
    gSavePath = "Game/Save"
    gMapsPath = "pheroes/bin/GameMaps"

    if (!gSettings.Init(cmdLine)) {
        TODO("MessageBox(NULL, _T(\"Unable to init game settings!\"), NULL, MB_OK)")
    }

    val flags = GXLF_LANDSCAPE
    // todo: port other flags generation

    if (!gApp.Init(gGame, 30u, flags, stage)) {
        TODO("return -1, maybe MessageBox is better?")
    }

    FillStaredRect(gApp.Display().GetSurface(), gApp.Display().GetSurface().GetSize().asRectangle(), PointInt())
    ShowProgressReport(0)

    // ShowLogo and intro image (disabled for this version)
    //iIntroDlg idlg(&gApp.ViewMgr());
    //idlg.DoModal();

    // todo:
//    if (gApp.SndPlayer().Inited()) {
//        gApp.SndPlayer().SetVolume(gSettings.GetEntryValue(CET_SFXVOLUME)*256/10);
//    }

    gApp.Display().SetGamma(1.0 + 0.05 * gSettings.GetEntryValue(ConfigEntryType.DISPGAMMA))

    ShowProgressReport(15)

    if (!gTextComposer.Init()) {
        TODO("MessageBox(NULL, _T(\"Unable to init text composer!\"), NULL, MB_OK); return -1;")
    }

    ShowProgressReport(45)

    if (!gTextMgr.Init()) {
        TODO("MessageBox(NULL, _T(\"Unable to init text manager!\"), NULL, MB_OK); return -1;")
    }

    ShowProgressReport(50)

    gGfxMgr.SetGamma(gSettings.GetEntryValue(ConfigEntryType.DISPGAMMA).toUInt())
    if (!gGfxMgr.Load(0, "pheroes/bin/Resources/hmm/GFX/spriteset.xml")) {
        TODO("MessageBox(NULL, _T(\"Unable to open sprite file!\"), NULL, MB_OK); return -1;")
    }

    ShowProgressReport(95)

    // todo:
//    if (gSettings.GetEntryValue(CET_SFXVOLUME) != 0 && !gSfxMgr.Init(gDataPath+_T("game.sfx"))) {
//        MessageBox(NULL, _T("Unable to open sound resources file!"), NULL, MB_OK);
//        return -1;
//    }

    gApp.Display().GetSurface().Fill(cColor.Gray64.pixel)
    val mdlg = iLangMenuDlg(gApp.ViewMgr())
    val res = mdlg.DoModal()
    val languageId = res - 100
    val language = Language.values()[languageId]
    gTextMgr.SetLanguage(language)

    if (!gGame.Init()) {
        TODO("return -1")
    }

    gApp.Run()
    gApp.Destroy()
}
