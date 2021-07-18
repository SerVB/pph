import com.github.servb.pph.pheroes.game.WinMain
import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.std.openAsZip
import com.soywiz.korio.file.std.resourcesVfs

lateinit var rootVfs: VfsFile
    private set

suspend fun main() = Korge(
    width = 3 * 320, height = 3 * 240,
    virtualWidth = 320, virtualHeight = 240,
    title = "Pocket Palm Heroes",
) {
    Logger.defaultLevel = Logger.Level.INFO

    rootVfs =
        resourcesVfs["resources.zip"].openAsZip(caseSensitive = false)  // case insensitive to avoid strict matching

    WinMain(this, "")
//        mainDev()
}
