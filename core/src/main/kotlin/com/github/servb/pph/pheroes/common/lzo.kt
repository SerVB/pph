package com.github.servb.pph.pheroes.common

import com.github.servb.pph.gxlib.memory.DynamicBuffer
import io.airlift.compress.lzo.LzoCompressor
import io.airlift.compress.lzo.LzoDecompressor

private val LZO_BLOCK_HDR: Short = ('L' + 256 * 'Z'.toInt()).toShort()

@ExperimentalUnsignedTypes
fun decompress(lzoBuffer: ByteArray, lzoBufferLength: Int, rawBuffer: DynamicBuffer): Int {
    val hdr = (lzoBuffer[0] + 256 * lzoBuffer[1]).toShort()
    check(hdr == LZO_BLOCK_HDR) { "Wrong HDR: $hdr" }

    val rawBufferLength = lzoBuffer[2] + 256 * (lzoBuffer[3] + 256 * (lzoBuffer[4] + 256 * lzoBuffer[5]))
    check(rawBufferLength >= 0) { "Negative raw buffer length: $rawBufferLength" }

    rawBuffer.reInit(rawBufferLength)
    rawBuffer.incrementSize(rawBufferLength)

    val rawArray = ByteArray(rawBufferLength)
    LzoDecompressor().decompress(lzoBuffer, 6, lzoBufferLength - 6, rawArray, 0, rawBufferLength)
    rawBuffer.write(rawArray, 1, rawBufferLength)

    return rawBufferLength
}

@ExperimentalUnsignedTypes
fun compress(rawBuffer: ByteArray, rawBufferLength: Int, lzoBuffer: DynamicBuffer): Int {
    val outLength = rawBufferLength + rawBufferLength / 16 + 64 + 3
    val outBuffer = ByteArray(outLength)

    LzoCompressor().compress(rawBuffer, 0, rawBufferLength, outBuffer, 0, outLength)

    lzoBuffer.write(LZO_BLOCK_HDR)
    lzoBuffer.write(rawBufferLength)
    lzoBuffer.write(outBuffer, 1, outLength)

    return lzoBuffer.size
}
