package com.reliabletxt

import java.nio.CharBuffer

object ReliableTxtEncoder {

    fun encode(text: String, encoding: ReliableTxtEncoding): ByteArray {
        val charset = encoding.charset
        val textWithPreamble = 65279.toChar().toString() + text
        val encoder = charset.newEncoder()
        val charBuffer = CharBuffer.wrap(textWithPreamble)
        return try {
            val byteBuffer = encoder.encode(charBuffer)
            val numBytes = byteBuffer.limit()
            ByteArray(numBytes)
                .also { byteBuffer[it] }
        } catch (e: Exception) {
            throw ReliableTxtException("Text contains invalid characters")
        }
    }
}
