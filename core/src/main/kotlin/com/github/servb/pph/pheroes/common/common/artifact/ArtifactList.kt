package com.github.servb.pph.pheroes.common.common.artifact

interface iItemC {
    val id: Short
    val assign: HeroArtifactCell
}

data class iItem(override var id: Short, override var assign: HeroArtifactCell) : iItemC

typealias ArtifactListC = List<iItemC>
typealias ArtifactList = MutableList<iItem>
