package reliableKt.reliableTXT

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test

class ReliableTxtDocumentTest {

    @Test
    fun getText() {
        getText("")
        getText("abc")
        getText("aß東\uD840\uDC07")
    }

    private fun getText(text: String) {
        assertThat(ReliableTxtDocument(text).text).isEqualTo(text)
    }

    @Test
    fun getEncoding() {
        assertThat(ReliableTxtDocument("abc").encoding).isEqualTo(ReliableTxtEncoding.UTF_8)
        checkGetEncoding(ReliableTxtEncoding.UTF_8)
        checkGetEncoding(ReliableTxtEncoding.UTF_16)
        checkGetEncoding(ReliableTxtEncoding.UTF_16_REVERSE)
        checkGetEncoding(ReliableTxtEncoding.UTF_32)
    }

    private fun checkGetEncoding(encoding: ReliableTxtEncoding) {
        assertThat(ReliableTxtDocument("abc", encoding).encoding).isEqualTo(encoding)
    }

    @Test
    fun getCodePoints() {
        checkGetCodePoints(intArrayOf(0x61, 0xDF, 0x6771, 0x20007))
        checkGetCodePoints("aß東\uD840\uDC07", intArrayOf(0x61, 0xDF, 0x6771, 0x20007))
    }

    private fun checkGetCodePoints(inputAndExpected: IntArray) {
        assertThat(ReliableTxtDocument(inputAndExpected).getCodePoints()).isEqualTo(inputAndExpected)
    }

    private fun checkGetCodePoints(text: String, expected: IntArray) {
        assertThat(ReliableTxtDocument(text).getCodePoints()).isEqualTo(expected)
    }

    @Test
    fun getLines() {
        checkGetLines("", arrayOf(""))
        checkGetLines("Line1", arrayOf("Line1"))
        checkGetLines("Line1\nLine2", arrayOf("Line1", "Line2"))
        checkGetLines("Line1\nLine2\nLine3", arrayOf("Line1", "Line2", "Line3"))
    }

    private fun checkGetLines(text: String, expectedResult: Array<String>) {
        assertThat(ReliableTxtDocument(text).getLines()).isEqualTo(expectedResult)
    }

    @Test
    fun constructor_LinesGiven() {
        constructor_LinesGiven(arrayOf(), "")
        constructor_LinesGiven(arrayOf("Line1"), "Line1")
        constructor_LinesGiven(arrayOf("Line1", "Line2"), "Line1\nLine2")
        constructor_LinesGiven(arrayOf("Line1", "Line2", "Line3"), "Line1\nLine2\nLine3")
    }

    private fun constructor_LinesGiven(lines: Array<String>, expectedResult: String) {
        assertThat(ReliableTxtDocument(*lines).toString()).isEqualTo(expectedResult)
        val linesList = listOf(*lines)
        assertThat(ReliableTxtDocument(linesList).toString()).isEqualTo(expectedResult)
    }

    @Test
    fun getBytes() {
        checkGetBytes(ReliableTxtDocument(), byteArray(0xEF, 0xBB, 0xBF))
        checkGetBytes("", byteArray(0xEF, 0xBB, 0xBF))
        checkGetBytes("a", byteArray(0xEF, 0xBB, 0xBF, 0x61))
        checkGetBytes("", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF))
        checkGetBytes("a", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0x61))
        checkGetBytes("\u0000", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0x00))
        checkGetBytes("\u00DF", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0xC3, 0x9F))
        checkGetBytes(
            "\u6771",
            ReliableTxtEncoding.UTF_8,
            byteArray(0xEF, 0xBB, 0xBF, 0xE6, 0x9D, 0xB1)
        )
        checkGetBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_8,
            byteArray(0xEF, 0xBB, 0xBF, 0xF0, 0xA0, 0x80, 0x87)
        )
        checkGetBytes("", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF))
        checkGetBytes("a", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0x61))
        checkGetBytes("\u0000", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0x00))
        checkGetBytes("\u00DF", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0xDF))
        checkGetBytes("\u6771", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x67, 0x71))
        checkGetBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_16,
            byteArray(0xFE, 0xFF, 0xD8, 0x40, 0xDC, 0x07)
        )
        checkGetBytes("", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE))
        checkGetBytes("a", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE, 0x61, 0x00))
        checkGetBytes(
            "\u0000",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0x00, 0x00)
        )
        checkGetBytes(
            "\u00DF",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0xDF, 0x00)
        )
        checkGetBytes(
            "\u6771",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0x71, 0x67)
        )
        checkGetBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x07, 0xDC)
        )
        checkGetBytes("", ReliableTxtEncoding.UTF_32, byteArray(0x00, 0x00, 0xFE, 0xFF))
        checkGetBytes(
            "a",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x61)
        )
        checkGetBytes(
            "\u0000",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00)
        )
        checkGetBytes(
            "\u00DF",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0xDF)
        )
        checkGetBytes(
            "\u6771",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x67, 0x71)
        )
        checkGetBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x02, 0x00, 0x07)
        )
        checkGetBytes(byteArray(0xEF, 0xBB, 0xBF))
        checkGetBytes(byteArray(0xEF, 0xBB, 0xBF, 0x61))
        checkGetBytes(byteArray(0xEF, 0xBB, 0xBF, 0x00))
        checkGetBytes(byteArray(0xEF, 0xBB, 0xBF, 0xC3, 0x9F))
        checkGetBytes(byteArray(0xEF, 0xBB, 0xBF, 0xE6, 0x9D, 0xB1))
        checkGetBytes(byteArray(0xEF, 0xBB, 0xBF, 0xF0, 0x90, 0x90, 0x80))
        checkGetBytes(byteArray(0xFE, 0xFF))
        checkGetBytes(byteArray(0xFE, 0xFF, 0x00, 0x61))
        checkGetBytes(byteArray(0xFE, 0xFF, 0x00, 0x00))
        checkGetBytes(byteArray(0xFE, 0xFF, 0x00, 0xDF))
        checkGetBytes(byteArray(0xFE, 0xFF, 0x67, 0x71))
        checkGetBytes(byteArray(0xFE, 0xFF, 0xD8, 0x40, 0xDC, 0x07))
        checkGetBytes(byteArray(0xFF, 0xFE))
        checkGetBytes(byteArray(0xFF, 0xFE, 0x61, 0x00))
        checkGetBytes(byteArray(0xFF, 0xFE, 0x00, 0x00))
        checkGetBytes(byteArray(0xFF, 0xFE, 0xDF, 0x00))
        checkGetBytes(byteArray(0xFF, 0xFE, 0x71, 0x67))
        checkGetBytes(byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x07, 0xDC))
        checkGetBytes(byteArray(0x00, 0x00, 0xFE, 0xFF))
        checkGetBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x61))
        checkGetBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00))
        checkGetBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0xDF))
        checkGetBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x67, 0x71))
        checkGetBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x02, 0x00, 0x07))
    }

    private fun checkGetBytes(document: ReliableTxtDocument, expectedBytes: ByteArray) {
        assertThat(document.getBytes()).isEqualTo(expectedBytes)
    }

    private fun checkGetBytes(str: String, expectedBytes: ByteArray) {
        assertThat(ReliableTxtDocument(str).getBytes()).isEqualTo(expectedBytes)
    }

    private fun checkGetBytes(str: String, encoding: ReliableTxtEncoding, expectedBytes: ByteArray) {
        assertThat(ReliableTxtDocument(str, encoding).getBytes()).isEqualTo(expectedBytes)
    }

    private fun checkGetBytes(inputAndExpected: ByteArray) {
        assertThat(ReliableTxtDocument(inputAndExpected).getBytes()).isEqualTo(inputAndExpected)
    }

    @Test
    fun save() {
        checkSave("")
        checkSave("abc")
        checkSave("aß東\uD840\uDC07")
        checkSave("", ReliableTxtEncoding.UTF_8)
        checkSave("", ReliableTxtEncoding.UTF_16)
        checkSave("", ReliableTxtEncoding.UTF_16_REVERSE)
        checkSave("", ReliableTxtEncoding.UTF_32)
        checkSave("abc", ReliableTxtEncoding.UTF_8)
        checkSave("abc", ReliableTxtEncoding.UTF_16)
        checkSave("abc", ReliableTxtEncoding.UTF_16_REVERSE)
        checkSave("abc", ReliableTxtEncoding.UTF_32)
        checkSave("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_8)
        checkSave("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_16)
        checkSave("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_16_REVERSE)
        checkSave("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_32)
    }

    private fun checkSave(text: String, encoding: ReliableTxtEncoding) {
        try {
            val filePath = "Test.txt"
            ReliableTxtDocument(text, encoding).save(filePath)
            checkLoad(filePath, text, encoding)
            ReliableTxtDocument.save(text, encoding, filePath)
            checkLoad(filePath, text, encoding)
            ReliableTxtDocument.save(text.codePoints().toArray(), encoding, filePath)
            checkLoad(filePath, text, encoding)
        } catch (ex: IOException) {
            throw RuntimeException()
        }
    }

    private fun checkSave(text: String) {
        try {
            val filePath = "Test.txt"
            ReliableTxtDocument(text).save(filePath)
            checkLoad(filePath, text, ReliableTxtEncoding.UTF_8)
            ReliableTxtDocument.save(text = text, filePath = filePath)
            checkLoad(filePath, text, ReliableTxtEncoding.UTF_8)
            ReliableTxtDocument.save(codepoints = text.codePoints().toArray(), filePath = filePath)
            checkLoad(filePath, text, ReliableTxtEncoding.UTF_8)
        } catch (ex: IOException) {
            throw RuntimeException()
        }
    }

    private fun checkLoad(filePath: String, expectedText: String, expectedEncoding: ReliableTxtEncoding) {
        val (text, encoding) = ReliableTxtDocument.load(filePath)
        assertThat(encoding).isEqualTo(expectedEncoding)
        assertThat(text).isEqualTo(expectedText)
    }

    companion object {

        private fun byteArray(vararg values: Int): ByteArray {
            val bytes = ByteArray(values.size)
            for (i in values.indices) {
                bytes[i] = values[i].toByte()
            }
            return bytes
        }

        @JvmStatic
        @AfterAll
        fun cleanup() {
            Files.deleteIfExists(Path.of("Test.txt"))
        }
    }
}
