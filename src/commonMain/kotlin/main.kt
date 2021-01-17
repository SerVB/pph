import com.github.servb.pph.pheroes.game.WinMain
import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge

suspend fun main() = Korge(
    width = 3 * 320, height = 3 * 240,
    virtualWidth = 320, virtualHeight = 240,
) {
    Logger.defaultLevel = Logger.Level.INFO

    WinMain(this, "")
}
