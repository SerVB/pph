package com.github.servb.pph.pheroes.common

import com.github.servb.pph.gxlib.ReadS16
import com.github.servb.pph.gxlib.ReadString
import com.github.servb.pph.gxlib.Write
import com.soywiz.kmem.ByteArrayBuilder
import com.soywiz.korio.stream.AsyncInputStream
import com.soywiz.korio.stream.AsyncOutputStream
import com.soywiz.korma.geom.IPointInt
import com.soywiz.korma.geom.PointInt

fun Serialize(buff: ByteArrayBuilder, point: IPointInt) {
    buff.Write(point.x.toShort())
    buff.Write(point.y.toShort())
}

suspend fun Serialize(buff: AsyncOutputStream, point: IPointInt) {
    buff.Write(point.x.toShort())
    buff.Write(point.y.toShort())
}

suspend fun DeserializePoint(buff: AsyncInputStream): PointInt {
    return PointInt(
        x = buff.ReadS16().toInt(),
        y = buff.ReadS16().toInt(),
    )
}

fun Serialize(buff: ByteArrayBuilder, string: String) {
    buff.Write(string)
}

suspend fun Serialize(buff: AsyncOutputStream, string: String) {
    buff.Write(string)
}

suspend fun DeserializeString(buff: AsyncInputStream): String {
    return buff.ReadString()
}
