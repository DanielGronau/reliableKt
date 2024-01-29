package com.reliabletxt

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

private val CHARSET_UTF_32 = Charset.forName("UTF-32BE")

enum class ReliableTxtEncoding(
    val charset: Charset,
    val preambleLength: Byte
) {
    UTF_8(StandardCharsets.UTF_8, 3),
    UTF_16(StandardCharsets.UTF_16BE, 2),
    UTF_16_REVERSE(StandardCharsets.UTF_16LE, 2),
    UTF_32(CHARSET_UTF_32, 4)
}
