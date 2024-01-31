package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import reliableKt.reliableTXT.ReliableTxtDocument
import reliableKt.reliableTXT.ReliableTxtEncoding
import reliableKt.reliableTXT.ReliableTxtEncoding.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.Test

class WsvStreamWriterTest {
    @Test
    fun constructor() {
        val filePath = "Test.wsv"
        WsvStreamWriter(filePath).use { }
        WsvStreamWriter(filePath, true).use { }
    }

    @Test
    fun constructor_append() {
        constructor_append(UTF_8, UTF_8)
        constructor_append(UTF_8, UTF_16)
        constructor_append(UTF_8, UTF_16_REVERSE)
        constructor_append(UTF_8, UTF_32)

        constructor_append(UTF_16, UTF_8)
        constructor_append(UTF_16, UTF_16)
        constructor_append(UTF_16, UTF_16_REVERSE)
        constructor_append(UTF_16, UTF_32)

        constructor_append(UTF_16_REVERSE, UTF_8)
        constructor_append(UTF_16_REVERSE, UTF_16)
        constructor_append(UTF_16_REVERSE, UTF_16_REVERSE)
        constructor_append(UTF_16_REVERSE, UTF_32)

        constructor_append(UTF_32, UTF_8)
        constructor_append(UTF_32, UTF_16)
        constructor_append(UTF_32, UTF_16_REVERSE)
        constructor_append(UTF_32, UTF_32)
    }

    @Throws(IOException::class, Exception::class)
    private fun constructor_append(firstEncoding: ReliableTxtEncoding, secondEncoding: ReliableTxtEncoding) {
        val filePath = "Append.wsv"
        deleteAppendFile(filePath)
        WsvStreamWriter(filePath, firstEncoding, true).use { writer ->
            writer.writeLine("V11", "V12")
            assertThat(writer.encoding).isEqualTo(firstEncoding)
        }
        load(filePath, "V11 V12", firstEncoding)
        WsvStreamWriter(filePath, secondEncoding, true).use { writer ->
            writer.writeLine("V21", "V22")
            assertThat(writer.encoding).isEqualTo(firstEncoding)
        }
        load(filePath, "V11 V12\nV21 V22", firstEncoding)
    }

    private fun deleteAppendFile(filePath: String) {
        try {
            Files.deleteIfExists(Paths.get(filePath))
        } catch (ex: IOException) {
            throw RuntimeException()
        }
    }

    @Test
    @Throws(IOException::class, Exception::class)
    fun constructor_ZeroByteFileGiven() {
        constructor_ZeroByteFileGiven(UTF_8)
        constructor_ZeroByteFileGiven(UTF_16)
        constructor_ZeroByteFileGiven(UTF_16_REVERSE)
        constructor_ZeroByteFileGiven(UTF_32)
    }

    @Throws(IOException::class, Exception::class)
    private fun constructor_ZeroByteFileGiven(encoding: ReliableTxtEncoding) {
        val filePath = "Append.wsv"
        deleteAppendFile(filePath)
        Files.write(Paths.get(filePath), byteArrayOf())
        WsvStreamWriter(filePath, encoding, true).use { writer ->
            writer.writeLine("V11", "V12")
        }
        load(filePath, "V11 V12", encoding)
    }

    @Test
    @Throws(Exception::class)
    fun writeLine() {
        writeLine(UTF_8)
        writeLine(UTF_16)
        writeLine(UTF_16_REVERSE)
        writeLine(UTF_32)
    }

    @Throws(Exception::class)
    private fun writeLine(encoding: ReliableTxtEncoding) {
        val filePath = "Test.wsv"
        WsvStreamWriter(filePath, encoding).use { writer ->
            for (i in 1..3) {
                writer.writeLine("V" + i + "1")
            }
        }
        load(filePath, "V11\nV21\nV31", encoding)
    }

    @Test
    @Throws(Exception::class)
    fun writeLine_WhitespaceAndCommentsGiven() {
        writeLine_WhitespaceAndCommentsGiven(UTF_8)
        writeLine_WhitespaceAndCommentsGiven(UTF_16)
        writeLine_WhitespaceAndCommentsGiven(UTF_16_REVERSE)
        writeLine_WhitespaceAndCommentsGiven(UTF_32)
    }

    @Throws(Exception::class)
    private fun writeLine_WhitespaceAndCommentsGiven(encoding: ReliableTxtEncoding) {
        val filePath = "Test.wsv"
        WsvStreamWriter(filePath, encoding).use { writer ->
            for (i in 1..3) {
                writer.writeLine(
                    arrayOf("V" + i + "1"),
                    arrayOf("  ", "   "),
                    "comment"
                )
            }
        }
        load(filePath, "  V11   #comment\n  V21   #comment\n  V31   #comment", encoding)
    }

    @Test
    @Throws(Exception::class)
    fun writeLines() {
        val filePath = "Test.wsv"
        WsvStreamWriter(filePath).use { writer ->
            writer.writeLines(
                WsvLine.parse("V11 V12"),
                WsvLine.parse("V21 V22"),
                WsvLine.parse("V31 V32")
            )
        }
        load(filePath, "V11 V12\nV21 V22\nV31 V32", UTF_8)
    }

    @Test
    @Throws(Exception::class)
    fun writeLines_DocumentGiven() {
        val filePath = "Test.wsv"
        WsvStreamWriter(filePath).use { writer ->
            val doc = WsvDocument.parse("V11 V12  #Comment\nV21 V22\nV31 V32")
            writer.writeLines(doc)
        }
        load(filePath, "V11 V12  #Comment\nV21 V22\nV31 V32", UTF_8)
    }

    private fun load(filePath: String, text: String, encoding: ReliableTxtEncoding) {
        val loaded = ReliableTxtDocument.load(filePath)
        assertThat(loaded.encoding).isEqualTo(encoding)
        assertThat(loaded.text).isEqualTo(text)
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun cleanup() {
            Files.deleteIfExists(Path.of("Test.wsv"))
        }
    }
}