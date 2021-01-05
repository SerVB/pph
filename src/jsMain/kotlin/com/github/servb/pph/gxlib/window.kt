package com.github.servb.pph.gxlib

import kotlinx.browser.window

internal actual fun closeWindow() {
    window.close()
}
