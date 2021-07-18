package com.github.servb.pph.pheroes.common

import com.github.servb.pph.util.SizeT

open class iSortArray<E : Any> {

    data class iEntry<T : Any>(val idx: Int, val value: T)

    protected val m_Array: MutableList<iEntry<E>> = mutableListOf()

    fun GetPtr(): MutableList<iEntry<E>> = m_Array

    fun Init(other: iSortArray<E>) {
        this.setTo(other)
    }

    fun setTo(other: iSortArray<E>) {
        this.m_Array.clear()
        this.m_Array.addAll(other.m_Array)
    }

    fun Pop(): E = m_Array.removeLast().value

    fun FirstIdx(): Int = m_Array.first().idx
    fun LastIdx(): Int = m_Array.last().idx

    fun Last(): E = m_Array.last().value

    fun RemoveAt(idx: Int) {
        m_Array.removeAt(idx)
    }

    // indexes: from smaller to bigger
    fun Insert(value: E, idx: Int) {
        var length: SizeT = m_Array.size
        if (length == 0 || m_Array[length - 1].idx <= idx) {
            m_Array.add(iEntry(idx, value))
            return
        }

        if (idx < m_Array.first().idx) {
            m_Array.add(0, iEntry(idx, value))
            return
        }

        var first: SizeT = 0
        while (length > 0) {
            val half: SizeT = length shr 1
            val middle = first + half
            if (m_Array[middle].idx <= idx) {
                first = middle + 1
                length = length - half - 1
            } else {
                length = half
            }
        }
        m_Array.add(first, iEntry(idx, value))
    }

    fun Cleanup() {
        m_Array.clear()
    }

    fun Size(): SizeT = m_Array.size

    operator fun get(idx: SizeT): iEntry<E> = m_Array[idx]
    fun Value(idx: SizeT): E = m_Array[idx].value
    fun Index(idx: SizeT): Int = m_Array[idx].idx

    // todo: create tests
    // fun SelfTest()
}