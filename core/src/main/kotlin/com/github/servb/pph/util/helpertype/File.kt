package com.github.servb.pph.util.helpertype

import java.io.FileInputStream

// TODO: check for correctness (LE or BE)

fun FileInputStream.readShort(): Short = (read() + 256 * read()).toShort()
fun FileInputStream.readInt(): Int = read() + 256 * (read() + 256 * (read() + 256 * read()))
