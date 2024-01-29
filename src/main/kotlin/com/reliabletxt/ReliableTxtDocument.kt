package com.stenway.reliabletxt

import com.stenway.reliabletxt.ReliableTxtDecoder.decode
import com.stenway.reliabletxt.ReliableTxtEncoder.encode
import com.stenway.reliabletxt.ReliableTxtLines.split
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

data class ReliableTxtDocument(
    val text: String = "",
    val encoding: ReliableTxtEncoding = ReliableTxtEncoding.UTF_8
) {

    constructor(
        codePoints: IntArray,
        encoding: ReliableTxtEncoding = ReliableTxtEncoding.UTF_8
    ) : this(String(codePoints, 0, codePoints.size), encoding)

    constructor(
        vararg lines: CharSequence,
        encoding: ReliableTxtEncoding = ReliableTxtEncoding.UTF_8
    ) : this(ReliableTxtLines.join(*lines), encoding)

    constructor(
        lines: Iterable<CharSequence>,
        encoding: ReliableTxtEncoding = ReliableTxtEncoding.UTF_8
    ) : this(ReliableTxtLines.join(lines), encoding)

    override fun toString(): String = text

    fun getBytes(): ByteArray = encode(text, encoding)

    fun getCodePoints(): IntArray = text.codePoints().toArray()

    fun getLines(): Array<String> = split(text)

    @Throws(IOException::class)
    fun save(filePath: String) {
        Files.write(Paths.get(filePath), getBytes())
    }

    companion object {

        operator fun invoke(
            bytes: ByteArray
        ): ReliableTxtDocument {
            val (encoding, text) = decode(bytes)
            return ReliableTxtDocument(text, encoding)
        }

        @Throws(IOException::class)
        fun load(filePath: String): ReliableTxtDocument {
            val bytes = Files.readAllBytes(Paths.get(filePath))
            return ReliableTxtDocument(bytes)
        }

        @Throws(IOException::class)
        fun save(
            text: String,
            encoding: ReliableTxtEncoding = ReliableTxtEncoding.UTF_8,
            filePath: String
        ) {
            ReliableTxtDocument(text, encoding).save(filePath)
        }

        @Throws(IOException::class)
        fun save(
            codepoints: IntArray,
            encoding: ReliableTxtEncoding = ReliableTxtEncoding.UTF_8,
            filePath: String
        ) {
            ReliableTxtDocument(codepoints, encoding).save(filePath)
        }
    }
}
