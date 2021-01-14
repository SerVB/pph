package com.github.servb.pph.pheroes.game

import com.github.servb.pph.gxlib.GXLF_LANDSCAPE
import com.github.servb.pph.gxlib.iDibReader
import com.github.servb.pph.gxlib.iGXApp
import com.github.servb.pph.util.SizeT
import com.github.servb.pph.util.asRectangle
import com.github.servb.pph.util.center
import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.Stage
import com.soywiz.korio.async.delay
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.topLeft2

private lateinit var gDataPath: String
private lateinit var gSavePath: String
private lateinit var gMapsPath: String
val gApp: iGXApp = iGXApp()
private val gDibReader: iDibReader = iDibReader()
val gTextComposer: iTextComposer = iTextComposer()

val gTextMgr: iTextManager = iTextManager()

//val gGfxMgr: iGfxManager  // todo
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
        127u
    } else {
        48u
    }
    ComposeProgressBar(false, display.GetSurface(), rcPbar, pbarColor, curProg, 100)

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

    gDataPath = "Data/"
    gSavePath = "Save/"
    gMapsPath = "Maps/"

    if (!gSettings.Init(cmdLine)) {
        TODO("MessageBox(NULL, _T(\"Unable to init game settings!\"), NULL, MB_OK)")
    }

    val flags = GXLF_LANDSCAPE
    // todo: port other flags generation

    if (!gApp.Init(gGame, 30u, flags, stage)) {
        TODO("return -1, maybe MessageBox is better?")
    }

    // ShowLogo and intro image (disabled for this version)
    //iIntroDlg idlg(&gApp.ViewMgr());
    //idlg.DoModal();

    // todo:
//    if (gApp.SndPlayer().Inited()) {
//        gApp.SndPlayer().SetVolume(gSettings.GetEntryValue(CET_SFXVOLUME)*256/10);
//    }

    gApp.Display().SetGamma(1.0 + 0.05 * gSettings.GetEntryValue(ConfigEntryType.DISPGAMMA))

    if (!gTextComposer.Init()) {
        TODO("MessageBox(NULL, _T(\"Unable to init text composer!\"), NULL, MB_OK); return -1;")
    }

    if (!gTextMgr.Init()) {
        TODO("MessageBox(NULL, _T(\"Unable to init text manager!\"), NULL, MB_OK); return -1;")
    }

    // todo:
//    gGfxMgr.SetGamma( gSettings.GetEntryValue(CET_DISPGAMMA) );
//    if (!gGfxMgr.Load(0,(gDataPath+_T("game.gfx")).CStr(), gSettings.MapSpriteFile()?(iGfxManager::LM_MappedFile):(iGfxManager::LM_Memory))) {
//        MessageBox(NULL, _T("Unable to open sprite file!"), NULL, MB_OK);
//        return -1;
//    }

    // todo:
//    if (gSettings.GetEntryValue(CET_SFXVOLUME) != 0 && !gSfxMgr.Init(gDataPath+_T("game.sfx"))) {
//        MessageBox(NULL, _T("Unable to open sound resources file!"), NULL, MB_OK);
//        return -1;
//    }

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
