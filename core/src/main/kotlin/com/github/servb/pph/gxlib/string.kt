package com.github.servb.pph.gxlib

fun textToEscSeq(str: String): String = str
        .replace("""\n""", "\n")
        .replace("""\r""", "\r")
        .replace("""\t""", "\t")
        .replace("""\\""", "\\")

fun escSeqToText(str: String): String = str
        .replace("\n", """\n""")
        .replace("\r", """\r""")
        .replace("\t", """\t""")
        .replace("\\", """\\""")
