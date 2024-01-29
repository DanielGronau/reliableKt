package com.reliabletxt

import com.reliabletxt.Assert.equals
import java.io.IOException
import java.util.*
import kotlin.test.Test

class ReliableTxtDocumentTest {
    @Test
    fun getText() {
        getText("")
        getText("abc")
        getText("aß東\uD840\uDC07")
    }

    private fun getText(text: String) {
        equals(ReliableTxtDocument(text).text, text)
    }

    @Test
    fun getEncoding() {
        equals(ReliableTxtDocument("abc").encoding, ReliableTxtEncoding.UTF_8)
        getEncoding(ReliableTxtEncoding.UTF_8)
        getEncoding(ReliableTxtEncoding.UTF_16)
        getEncoding(ReliableTxtEncoding.UTF_16_REVERSE)
        getEncoding(ReliableTxtEncoding.UTF_32)
    }

    private fun getEncoding(encoding: ReliableTxtEncoding) {
        equals(ReliableTxtDocument("abc", encoding).encoding, encoding)
    }

    @Test
    fun getCodePoints() {
        getCodePoints(intArrayOf(0x61, 0xDF, 0x6771, 0x20007))
        getCodePoints("aß東\uD840\uDC07", intArrayOf(0x61, 0xDF, 0x6771, 0x20007))
    }

    private fun getCodePoints(inputAndExpected: IntArray) {
        equals(ReliableTxtDocument(inputAndExpected).getCodePoints(), inputAndExpected)
    }

    private fun getCodePoints(text: String, expected: IntArray) {
        equals(ReliableTxtDocument(text).getCodePoints(), expected)
    }

    @Test
    fun getLines() {
        getLines("", arrayOf(""))
        getLines("Line1", arrayOf("Line1"))
        getLines("Line1\nLine2", arrayOf("Line1", "Line2"))
        getLines("Line1\nLine2\nLine3", arrayOf("Line1", "Line2", "Line3"))
    }

    private fun getLines(text: String, expectedResult: Array<Any?>) {
        equals(ReliableTxtDocument(text).getLines() as Array<Any?>, expectedResult)
    }

    @Test
    fun constructor_LinesGiven() {
        constructor_LinesGiven(arrayOf(), "")
        constructor_LinesGiven(arrayOf("Line1"), "Line1")
        constructor_LinesGiven(arrayOf("Line1", "Line2"), "Line1\nLine2")
        constructor_LinesGiven(arrayOf("Line1", "Line2", "Line3"), "Line1\nLine2\nLine3")
    }

    private fun constructor_LinesGiven(lines: Array<String>, expectedResult: String) {
        equals(ReliableTxtDocument(*lines).toString(), expectedResult)
        val linesList = Arrays.asList(*lines)
        equals(ReliableTxtDocument(linesList).toString(), expectedResult)
    }

    @Test
    fun getBytes() {
        getBytes(ReliableTxtDocument(), byteArray(0xEF, 0xBB, 0xBF))
        getBytes("", byteArray(0xEF, 0xBB, 0xBF))
        getBytes("a", byteArray(0xEF, 0xBB, 0xBF, 0x61))
        getBytes("", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF))
        getBytes("a", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0x61))
        getBytes("\u0000", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0x00))
        getBytes("\u00DF", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0xC3, 0x9F))
        getBytes(
            "\u6771",
            ReliableTxtEncoding.UTF_8,
            byteArray(0xEF, 0xBB, 0xBF, 0xE6, 0x9D, 0xB1)
        )
        getBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_8,
            byteArray(0xEF, 0xBB, 0xBF, 0xF0, 0xA0, 0x80, 0x87)
        )
        getBytes("", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF))
        getBytes("a", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0x61))
        getBytes("\u0000", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0x00))
        getBytes("\u00DF", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0xDF))
        getBytes("\u6771", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x67, 0x71))
        getBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_16,
            byteArray(0xFE, 0xFF, 0xD8, 0x40, 0xDC, 0x07)
        )
        getBytes("", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE))
        getBytes("a", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE, 0x61, 0x00))
        getBytes(
            "\u0000",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0x00, 0x00)
        )
        getBytes(
            "\u00DF",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0xDF, 0x00)
        )
        getBytes(
            "\u6771",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0x71, 0x67)
        )
        getBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x07, 0xDC)
        )
        getBytes("", ReliableTxtEncoding.UTF_32, byteArray(0x00, 0x00, 0xFE, 0xFF))
        getBytes(
            "a",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x61)
        )
        getBytes(
            "\u0000",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00)
        )
        getBytes(
            "\u00DF",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0xDF)
        )
        getBytes(
            "\u6771",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x67, 0x71)
        )
        getBytes(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x02, 0x00, 0x07)
        )
        getBytes(byteArray(0xEF, 0xBB, 0xBF))
        getBytes(byteArray(0xEF, 0xBB, 0xBF, 0x61))
        getBytes(byteArray(0xEF, 0xBB, 0xBF, 0x00))
        getBytes(byteArray(0xEF, 0xBB, 0xBF, 0xC3, 0x9F))
        getBytes(byteArray(0xEF, 0xBB, 0xBF, 0xE6, 0x9D, 0xB1))
        getBytes(byteArray(0xEF, 0xBB, 0xBF, 0xF0, 0x90, 0x90, 0x80))
        getBytes(byteArray(0xFE, 0xFF))
        getBytes(byteArray(0xFE, 0xFF, 0x00, 0x61))
        getBytes(byteArray(0xFE, 0xFF, 0x00, 0x00))
        getBytes(byteArray(0xFE, 0xFF, 0x00, 0xDF))
        getBytes(byteArray(0xFE, 0xFF, 0x67, 0x71))
        getBytes(byteArray(0xFE, 0xFF, 0xD8, 0x40, 0xDC, 0x07))
        getBytes(byteArray(0xFF, 0xFE))
        getBytes(byteArray(0xFF, 0xFE, 0x61, 0x00))
        getBytes(byteArray(0xFF, 0xFE, 0x00, 0x00))
        getBytes(byteArray(0xFF, 0xFE, 0xDF, 0x00))
        getBytes(byteArray(0xFF, 0xFE, 0x71, 0x67))
        getBytes(byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x07, 0xDC))
        getBytes(byteArray(0x00, 0x00, 0xFE, 0xFF))
        getBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x61))
        getBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00))
        getBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0xDF))
        getBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x67, 0x71))
        getBytes(byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x02, 0x00, 0x07))
    }

    private fun getBytes(document: ReliableTxtDocument, expectedBytes: ByteArray) {
        equals(document.getBytes(), expectedBytes)
    }

    private fun getBytes(str: String, expectedBytes: ByteArray) {
        equals(ReliableTxtDocument(str).getBytes(), expectedBytes)
    }

    private fun getBytes(str: String, encoding: ReliableTxtEncoding, expectedBytes: ByteArray) {
        equals(ReliableTxtDocument(str, encoding).getBytes(), expectedBytes)
    }

    private fun getBytes(inputAndExpected: ByteArray) {
        equals(ReliableTxtDocument(inputAndExpected).getBytes(), inputAndExpected)
    }

    @Test
    fun save() {
        save("")
        save("abc")
        save("aß東\uD840\uDC07")
        save("", ReliableTxtEncoding.UTF_8)
        save("", ReliableTxtEncoding.UTF_16)
        save("", ReliableTxtEncoding.UTF_16_REVERSE)
        save("", ReliableTxtEncoding.UTF_32)
        save("abc", ReliableTxtEncoding.UTF_8)
        save("abc", ReliableTxtEncoding.UTF_16)
        save("abc", ReliableTxtEncoding.UTF_16_REVERSE)
        save("abc", ReliableTxtEncoding.UTF_32)
        save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_8)
        save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_16)
        save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_16_REVERSE)
        save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_32)
    }

    private fun save(text: String, encoding: ReliableTxtEncoding) {
        try {
            val filePath = "Test.txt"
            ReliableTxtDocument(text, encoding).save(filePath)
            load(filePath, text, encoding)
            ReliableTxtDocument.save(text, encoding, filePath)
            load(filePath, text, encoding)
            ReliableTxtDocument.save(text.codePoints().toArray(), encoding, filePath)
            load(filePath, text, encoding)
        } catch (ex: IOException) {
            throw RuntimeException()
        }
    }

    private fun save(text: String) {
        try {
            val filePath = "Test.txt"
            ReliableTxtDocument(text).save(filePath)
            load(filePath, text, ReliableTxtEncoding.UTF_8)
            ReliableTxtDocument.save(text = text, filePath = filePath)
            load(filePath, text, ReliableTxtEncoding.UTF_8)
            ReliableTxtDocument.save(codepoints = text.codePoints().toArray(), filePath = filePath)
            load(filePath, text, ReliableTxtEncoding.UTF_8)
        } catch (ex: IOException) {
            throw RuntimeException()
        }
    }

    private fun load(filePath: String, text: String, encoding: ReliableTxtEncoding) {
        val (text1, encoding1) = ReliableTxtDocument.load(filePath)
        equals(encoding1, encoding)
        equals(text1, text)
    }

    companion object {

        private fun byteArray(vararg values: Int): ByteArray {
            val bytes = ByteArray(values.size)
            for (i in values.indices) {
                bytes[i] = values[i].toByte()
            }
            return bytes
        }
    }
}
