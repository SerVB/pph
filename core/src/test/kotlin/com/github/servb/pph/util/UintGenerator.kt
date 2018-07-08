package com.github.servb.pph.util

import io.kotlintest.properties.Gen
import unsigned.Uint
import unsigned.ui

class UintGenerator : Gen<Uint> {
    override fun constants() = listOf(Uint(0), Uint(1))
    override fun random() = generateSequence {
        Uint(Gen.int().random().first().ui)
    }
}
