package com.stenway.reliabletxt

import java.io.BufferedReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class ReliableTxtStreamReader(filePath: String) : AutoCloseable {
    val encoding = ReliableTxtDecoder.getEncodingFromFile(filePath)

    val reader: BufferedReader = Files.newBufferedReader(
        Paths.get(filePath),
        encoding.charset
    )

    val sb: StringBuilder = StringBuilder()

    var endReached = false

    init {
        if (encoding !== ReliableTxtEncoding.UTF_32) {
            reader.read()
        }
    }

    @Throws(IOException::class)
    fun readLine(): String? {
        if (endReached) {
            return null
        }
        var c: Int
        sb.setLength(0)
        while (true) {
            c = reader.read()
            if (c == '\n'.code) {
                break
            } else if (c < 0) {
                endReached = true
                break
            }
            sb.append(c.toChar())
        }
        return sb.toString()
    }

    @Throws(Exception::class)
    override fun close() {
        reader.close()
    }
}
