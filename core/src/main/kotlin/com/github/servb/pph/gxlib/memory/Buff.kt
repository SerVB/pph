package com.github.servb.pph.gxlib.memory

class Buff<E> {
    private var data: MutableList<E?>?

    var size: Int
        private set

    val ptr: List<E?>? get() = data

    constructor() {
        this.data = null
        this.size = 0
    }

    constructor(size: Int) : this() {
        allocate(null, size)
    }

    constructor(buff: List<E>, size: Int) : this() {
        allocate(buff, size)
    }

    fun allocate(buff: List<E>?, size: Int) {
        this.size = size
        val data = MutableList<E?>(size) { null }

        if (buff != null) {
            for (idx in 0 until size) {
                data[idx] = buff[idx]
            }
        }

        this.data = data
    }

    fun clean() {
        this.data = null
        this.size = 0
    }
}
