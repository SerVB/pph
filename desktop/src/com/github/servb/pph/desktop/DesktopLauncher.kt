package com.github.servb.pph.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.servb.pph.Pph

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(Pph(), config)
}
