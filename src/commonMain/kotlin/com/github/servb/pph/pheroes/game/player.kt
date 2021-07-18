package com.github.servb.pph.pheroes.game

import com.github.servb.pph.pheroes.common.common.PlayerId
import com.github.servb.pph.pheroes.common.common.PlayerType

open class iPlayer {

    open fun PlayerType(): PlayerType = PlayerType.HUMAN

    fun PlayerId(): PlayerId = PlayerId.CYAN  // todo
}