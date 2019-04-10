@file:Suppress("UsePropertyAccessSyntax")

package com.github.servb.pph.gxlib.memory

import java.nio.BufferOverflowException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.ReadOnlyBufferException

@ExperimentalUnsignedTypes
class DynamicBuffer {
    private var byteBuffer: ByteBuffer

    var size: Int
        private set

    var increment: Int
        private set

    val isReadOnly: Boolean get() = byteBuffer.isReadOnly
    val capacity: Int get() = byteBuffer.capacity
    val position: Int get() = byteBuffer.position

    val isEnd: Boolean get() = byteBuffer.position >= size

    val data: ByteArray
        get() {
            val clone = byteBuffer.cloned()
            val bytes = size - clone.position

            return ByteArray(bytes).also { clone.get(it) }
        }

    constructor(capacity: Int = 0, increment: Int = DYNAMIC_BUFFER_INCREMENT) {
        this.byteBuffer = ByteBuffer.allocate(capacity)
        this.size = 0
        this.increment = increment.orDefaultIncrement()
    }

    fun reInit(capacity: Int = 0, increment: Int = DYNAMIC_BUFFER_INCREMENT) {
        this.byteBuffer = ByteBuffer.allocate(capacity)
        this.size = 0
        this.increment = increment.orDefaultIncrement()
    }

    constructor(other: DynamicBuffer) {
        byteBuffer = other.byteBuffer.cloned()
        size = other.size
        increment = other.increment
    }

    fun assign(other: DynamicBuffer) {
        byteBuffer = other.byteBuffer.cloned()
        size = other.size
        increment = other.increment
    }

    constructor(data: ByteArray, readOnly: Boolean = true) {
        val byteBuffer = ByteBuffer.allocate(data.size).also {
            it.put(data)
            it.position = 0
        }

        this.byteBuffer = if (readOnly) byteBuffer.asReadOnlyBuffer() else byteBuffer
        this.size = data.size
        this.increment = DYNAMIC_BUFFER_INCREMENT
    }

    fun setData(data: ByteArray, readOnly: Boolean = true) {
        val byteBuffer = ByteBuffer.allocate(data.size).also {
            it.put(data)
            it.position = 0
        }

        this.byteBuffer = if (readOnly) byteBuffer.asReadOnlyBuffer() else byteBuffer
        this.size = data.size
        this.increment = DYNAMIC_BUFFER_INCREMENT
    }

    fun clean() {
        byteBuffer = ByteBuffer.allocate(0)
        size = 0
    }

    fun reset() {
        byteBuffer.rewind()
        size = 0
    }

    fun incrementSize(offset: Int): Boolean {
        @Suppress("LiftReturnOrAssignment")
        if (size + offset > byteBuffer.capacity) {
            size = byteBuffer.capacity

            return false
        } else {
            size += offset

            return true
        }
    }

    fun incrementPosition(offset: Int): Int {
        val oldPosition = byteBuffer.position

        if (offset + oldPosition > byteBuffer.capacity) {
            byteBuffer.position = byteBuffer.capacity
        } else {
            byteBuffer.position += offset
        }

        return byteBuffer.position - oldPosition
    }

    fun seek(position: Int): Boolean {
        @Suppress("LiftReturnOrAssignment")
        if (position > byteBuffer.capacity) {
            size = byteBuffer.capacity

            return false
        } else {
            byteBuffer.position = position

            return true
        }
    }

    fun setSize(size: Int): Boolean {
        @Suppress("LiftReturnOrAssignment")
        if (size > byteBuffer.capacity) {
            this.size = byteBuffer.capacity

            return false
        } else {
            this.size = size

            return true
        }
    }

    private fun updateSize() {
        size = maxOf(size, byteBuffer.position)
    }

    fun write(value: Byte) = byteBuffer.putIfCan { put(value); updateSize() }
    fun write(value: UByte) = byteBuffer.putIfCan { put(value.toByte()); updateSize() }
    fun write(value: Short) = byteBuffer.putIfCan { putShort(value); updateSize() }
    fun write(value: UShort) = byteBuffer.putIfCan { putShort(value.toShort()); updateSize() }
    fun write(value: Int) = byteBuffer.putIfCan { putInt(value); updateSize() }
    fun write(value: UInt) = byteBuffer.putIfCan { putInt(value.toInt()); updateSize() }
    fun write(value: Long) = byteBuffer.putIfCan { putLong(value); updateSize() }
    fun write(value: ULong) = byteBuffer.putIfCan { putLong(value.toLong()); updateSize() }

    fun write(string: String) = byteBuffer.putIfCan {
        if (string.isNotEmpty()) {
            val lengthBytes: ByteArray = ByteBuffer.allocate(Long.SIZE_BYTES).putInt(string.length).array()

            val lengthAndString = lengthBytes + string.toByteArray()
            put(lengthAndString)
        } else {
            putInt(0)
        }

        updateSize()
    }

    fun write(buffer: ByteArray, elementSize: Int, count: Int): Int {
        val length = elementSize * count
        addLength(length)

        val can = byteBuffer.putIfCan { put(buffer); updateSize() }

        return if (can) count else 0
    }

    sealed class ReadResult<out T> {
        class ReadSuccess<T>(val value: T) : ReadResult<T>()
        object ReadError : ReadResult<Nothing>()
    }

    fun readByte() = byteBuffer.readIfCan { get() }
    fun readUByte() = byteBuffer.readIfCan { get().toUByte() }
    fun readShort() = byteBuffer.readIfCan { getShort() }
    fun readUShort() = byteBuffer.readIfCan { getShort().toUShort() }
    fun readInt() = byteBuffer.readIfCan { getInt() }
    fun readUInt() = byteBuffer.readIfCan { getInt().toUInt() }
    fun readLong() = byteBuffer.readIfCan { getLong() }
    fun readULong() = byteBuffer.readIfCan { getLong().toULong() }

    fun readString() = byteBuffer.readIfCan {
        val length = getInt()
        val bytes = ByteArray(length).also { get(it, 0, length) }

        return@readIfCan String(bytes)
    }

    fun read(buffer: ByteArray, elementSize: Int, count: Int): Int {
        val length = elementSize * count

        val can = byteBuffer.putIfCan { get(buffer, 0, length) }

        return if (can) count else 0
    }

    fun addLength(bySize: Int) {
        if (bySize > byteBuffer.capacity - byteBuffer.position) {
            val newLength = ((byteBuffer.position + bySize + increment - 1) / increment) * increment
            byteBuffer = byteBuffer.cloned(newLength)
        }
    }

    companion object {
        private const val DYNAMIC_BUFFER_INCREMENT = 512

        private fun Int.orDefaultIncrement(): Int {
            if (this == 0) {
                return DYNAMIC_BUFFER_INCREMENT
            }

            return this
        }

        private var ByteBuffer.position: Int
            get() = this.position()
            set(value) {
                this.position(value)
            }

        private val ByteBuffer.capacity: Int
            get() = this.capacity()

        private fun ByteBuffer.cloned(newCapacity: Int = this.capacity()): ByteBuffer {
            val clone = ByteBuffer.allocate(newCapacity)

            val readOnlyCopy = this.asReadOnlyBuffer()

            readOnlyCopy.flip()
            clone.put(readOnlyCopy)

            clone.position = this.position
            clone.limit(this.limit())
            clone.order(this.order())

            return clone
        }

        private fun ByteBuffer.putIfCan(operations: ByteBuffer.() -> Unit): Boolean {
            return try {
                operations()

                true
            } catch (e: BufferOverflowException) {
                false
            } catch (e: ReadOnlyBufferException) {
                false
            }
        }

        private inline fun <reified T> ByteBuffer.readIfCan(operations: ByteBuffer.() -> T): ReadResult<T> {
            return try {
                ReadResult.ReadSuccess(operations())
            } catch (e: BufferUnderflowException) {
                ReadResult.ReadError
            }
        }
    }
}
