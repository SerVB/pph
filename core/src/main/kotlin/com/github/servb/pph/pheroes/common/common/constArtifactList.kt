package com.github.servb.pph.pheroes.common.common

import java.util.*

/** TODO: Remove the class. */
class constArtifactList {
    protected val m_Items = ArrayList<iItem>()

    data class iItem(val id: Short, val assign: HERO_ART_CELL)

    fun Count(): Int {
        return m_Items.size
    }

    fun At(idx: Int): iItem {
        return m_Items[idx]
    }
}
