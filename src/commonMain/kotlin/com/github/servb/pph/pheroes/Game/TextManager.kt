package com.github.servb.pph.pheroes.Game

import com.github.servb.pph.pheroes.common.LANG_DATA
import com.github.servb.pph.pheroes.common.TextResId
import kotlin.properties.Delegates

class iTextManager {

    private var m_bHasLngFile: Boolean by Delegates.notNull()
    private val m_lngData: Map<TextResId, String> = mutableMapOf()

    fun Init(): Boolean {
        m_bHasLngFile = false

        // todo: seems like this version doesn't have lang selector, need to implement it
        //       create a dialog at every start, for example?

        return true
    }

    operator fun get(resId: TextResId): String {
        return if (m_bHasLngFile) {
            m_lngData[resId] ?: LANG_DATA[resId.v]
        } else {
            LANG_DATA[resId.v]
        }
    }
}
