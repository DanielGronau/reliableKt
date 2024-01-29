package com.reliabletxt

import com.reliabletxt.Assert.equals
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test

class ReliableTxtStreamWriterTest {
    @Test
    fun constructor() {
        val filePath = "Test.txt"
        ReliableTxtStreamWriter(filePath).use { }
        ReliableTxtStreamWriter(filePath, append = true).use { }
    }

    @Test
    fun constructor_append() {
        constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_8)
        constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16)
        constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16_REVERSE)
        constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_32)
        constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_8)
        constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16)
        constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16_REVERSE)
        constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_32)
        constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_8)
        constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16)
        constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16_REVERSE)
        constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_32)
        constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_8)
        constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16)
        constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16_REVERSE)
        constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_32)
    }

    private fun constructor_append(
        firstEncoding: ReliableTxtEncoding,
        secondEncoding: ReliableTxtEncoding
    ) {
        val filePath = "Append.txt"
        deleteAppendFile(filePath)
        ReliableTxtStreamWriter(filePath, firstEncoding, true).use { writer ->
            writer.writeLine("Line 1")
            equals(writer.encoding, firstEncoding)
        }
        load(filePath, "Line 1", firstEncoding)
        ReliableTxtStreamWriter(filePath, secondEncoding, true).use { writer ->
            writer.writeLine("Line 2")
            equals(writer.encoding, firstEncoding)
        }
        load(filePath, "Line 1\nLine 2", firstEncoding)
    }

    private fun deleteAppendFile(filePath: String) {
        try {
            Files.deleteIfExists(Paths.get(filePath))
        } catch (ex: IOException) {
            throw RuntimeException()
        }
    }

    @Test
    fun constructor_ZeroByteFileGiven() {
        constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_8)
        constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16)
        constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16_REVERSE)
        constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_32)
    }

    private fun constructor_ZeroByteFileGiven(encoding: ReliableTxtEncoding) {
        val filePath = "Append.txt"
        deleteAppendFile(filePath)
        Files.write(Paths.get(filePath), byteArrayOf())
        ReliableTxtStreamWriter(filePath, encoding, true).use { writer ->
            writer.writeLine("Line 1")
        }
        load(filePath, "Line 1", encoding)
    }

    @Test
    fun writeLine() {
        writeLine(ReliableTxtEncoding.UTF_8)
        writeLine(ReliableTxtEncoding.UTF_16)
        writeLine(ReliableTxtEncoding.UTF_16_REVERSE)
        writeLine(ReliableTxtEncoding.UTF_32)
    }

    private fun writeLine(encoding: ReliableTxtEncoding) {
        val filePath = "Test.txt"
        ReliableTxtStreamWriter(filePath, encoding).use { writer ->
            for (i in 1..3) {
                writer.writeLine("Line $i")
            }
        }
        load(filePath, "Line 1\nLine 2\nLine 3", encoding)
    }

    @Test
    fun writeLines() {
        val filePath = "Test.txt"
        ReliableTxtStreamWriter(filePath).use { writer ->
            writer.writeLines(
                "Line 1",
                "Line 2",
                "Line 3"
            )
        }
        load(filePath, "Line 1\nLine 2\nLine 3", ReliableTxtEncoding.UTF_8)
    }

    private fun load(filePath: String, text: String, encoding: ReliableTxtEncoding) {
        val (text1, encoding1) = ReliableTxtDocument.load(filePath)
        equals(encoding1, encoding)
        equals(text1, text)
    }
}
