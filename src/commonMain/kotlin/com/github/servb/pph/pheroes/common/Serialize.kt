package com.github.servb.pph.pheroes.common

import com.github.servb.pph.gxlib.ReadString
import com.github.servb.pph.gxlib.Write
import com.soywiz.kmem.ByteArrayBuilder
import com.soywiz.korio.stream.AsyncInputStream
import com.soywiz.korio.stream.AsyncOutputStream

fun Serialize(buff: ByteArrayBuilder, string: String) {
    buff.Write(string)
}

suspend fun Serialize(buff: AsyncOutputStream, string: String) {
    buff.Write(string)
}

suspend fun DeserializeString(buff: AsyncInputStream): String {
    return buff.ReadString()
}
