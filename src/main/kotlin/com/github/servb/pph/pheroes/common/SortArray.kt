package com.github.servb.pph.pheroes.common

class SortArray<E> {
    data class Entry<T>(val idx: Int, val value: T)

    private val array = mutableListOf<Entry<E>>()

    val ptr: MutableList<Entry<E>>
        get() = TODO(
                "return `array` here if this val is used somewhere, otherwise, remove this val"
        )

    fun init(other: SortArray<E>) {
        this.array.clear()

        other.array.forEach {
            this.array.add(it)
        }
    }

    fun pop(): E = array.removeAt(array.lastIndex).value

    val firstIdx: Int get() = array.first().idx
    val lastIdx: Int get() = array.last().idx

    val last: E get() = array.last().value

    fun removeAt(idx: Int): Boolean {
        array.removeAt(idx)
        return true
    }

    /** Indices: from smaller to bigger. */
    fun insert(value: E, idx: Int): Boolean {
        var length = array.size
        if (length == 0 || array.last().idx <= idx) {
            array.add(Entry(idx, value))
            return true
        }

        if (idx < array.first().idx) {
            array.add(0, Entry(idx, value))
            return true
        }

        var first = 0

        while (length > 0) {
            val half = length shr 1
            val middle = first + half
            if (array[middle].idx <= idx) {
                first = middle + 1
                length = length - half - 1
            } else {
                length = half
            }
        }
        array.add(first, Entry(idx, value))
        return true
    }

    fun cleanup() = array.clear()

    val size: Int get() = array.size

    operator fun get(idx: Int): Entry<E> = array[idx]
    fun value(idx: Int): E = array[idx].value
    fun index(idx: Int): Int = array[idx].idx

    fun selfTest(): Boolean {
        // TODO: Move to tests

        if (size < 2) {
            return true
        }

        return array.windowed(2).all { it.first().idx <= it.last().idx }
    }
}
