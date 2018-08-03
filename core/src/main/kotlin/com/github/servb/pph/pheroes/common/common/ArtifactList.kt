package com.github.servb.pph.pheroes.common.common

interface iItemC {
    val id: Short
    val assign: HERO_ART_CELL
}

data class iItem(override var id: Short, override var assign: HERO_ART_CELL) : iItemC

typealias ArtifactListC = List<iItemC>
typealias ArtifactList = MutableList<iItem>
