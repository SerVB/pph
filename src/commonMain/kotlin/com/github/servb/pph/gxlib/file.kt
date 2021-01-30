package com.github.servb.pph.gxlib

import com.soywiz.kmem.ByteArrayBuilder
import com.soywiz.korio.lang.Charsets
import com.soywiz.korio.lang.toByteArray
import com.soywiz.korio.stream.*

fun ByteArrayBuilder.Write(v: UByte) = s8(v.toInt())
fun ByteArrayBuilder.Write(v: Byte) = s8(v.toInt())
fun ByteArrayBuilder.Write(v: UShort) = s16LE(v.toInt())
fun ByteArrayBuilder.Write(v: Short) = s16LE(v.toInt())
fun ByteArrayBuilder.Write(v: UInt) = s32LE(v.toInt())
fun ByteArrayBuilder.Write(v: Int) = s32LE(v)

suspend fun AsyncOutputStream.Write(v: UShort) = write16LE(v.toInt())
suspend fun AsyncOutputStream.Write(v: Short) = write16LE(v.toInt())
suspend fun AsyncOutputStream.Write(v: UInt) = write32LE(v.toInt())
suspend fun AsyncOutputStream.Write(v: Int) = write32LE(v)

fun SyncInputStream.ReadU8(): UByte = readU8().toUByte()
fun SyncInputStream.ReadU16(): UShort = readU16LE().toUShort()
fun SyncInputStream.ReadU32(): UInt = readU32LE().toUInt()
fun SyncInputStream.ReadS8(): Byte = readS8().toByte()
fun SyncInputStream.ReadS16(): Short = readS16LE().toShort()
fun SyncInputStream.ReadS32(): Int = readS32LE()

suspend fun AsyncInputStream.ReadU16(): UShort = readU16LE().toUShort()
suspend fun AsyncInputStream.ReadU32(): UInt = readU32LE().toUInt()
suspend fun AsyncInputStream.ReadS16(): Short = readS16LE().toShort()
suspend fun AsyncInputStream.ReadS32(): Int = readS32LE()

fun ByteArrayBuilder.Write(str: String) {
    Write(str.length)
    append(str.toByteArray(Charsets.UTF16_LE))
}

suspend fun AsyncOutputStream.Write(str: String) {
    Write(str.length)
    writeString(str, Charsets.UTF16_LE)
}

suspend fun AsyncInputStream.ReadString(): String {
    val length = ReadS32()
    return readString(length, Charsets.UTF16_LE)
}
