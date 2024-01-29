package com.reliabletxt

import com.reliabletxt.Assert.equals
import com.reliabletxt.ReliableTxtDecoder.decode
import org.junit.jupiter.api.Assertions.assertThrows
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test

class ReliableTxtDecoderTest {
    @Test
    fun getEncoding() {
        equals(
            ReliableTxtDecoder.getEncoding(byteArray(0xEF, 0xBB, 0xBF)),
            ReliableTxtEncoding.UTF_8
        )
        equals(ReliableTxtDecoder.getEncoding(byteArray(0xFE, 0xFF)), ReliableTxtEncoding.UTF_16)
        equals(
            ReliableTxtDecoder.getEncoding(byteArray(0xFF, 0xFE)),
            ReliableTxtEncoding.UTF_16_REVERSE
        )
        equals(
            ReliableTxtDecoder.getEncoding(byteArray(0x00, 0x00, 0xFE, 0xFF)),
            ReliableTxtEncoding.UTF_32
        )
    }

    @Test
    fun getEncoding_InvalidPreambleGiven_ShouldThrowException() {
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray())
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEE))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF, 0xBC))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF, 0xBB))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF, 0xBB, 0xBE))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF, 0xBC, 0xBF))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE, 0xFE))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF, 0xFF))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00, 0x00))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00, 0x00, 0xFE))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00, 0x00, 0xFE, 0xFE))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x01, 0x00, 0xFE, 0xFF))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00, 0x01, 0xFE, 0xFF))
        getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00, 0x00, 0xFF, 0xFE))
    }

    private fun getEncoding_InvalidPreambleGiven_ShouldThrowException(bytes: ByteArray) {
        val e: Exception = assertThrows(ReliableTxtException::class.java) {
            ReliableTxtDecoder.getEncoding(bytes)
        }
        equals(e.message, "Document does not have a ReliableTXT preamble")
    }

    @Test
    fun getEncodingFromFile() {
        equals(getEncodingFromFile(byteArray(0xEF, 0xBB, 0xBF)), ReliableTxtEncoding.UTF_8)
        equals(getEncodingFromFile(byteArray(0xFE, 0xFF)), ReliableTxtEncoding.UTF_16)
        equals(getEncodingFromFile(byteArray(0xFF, 0xFE)), ReliableTxtEncoding.UTF_16_REVERSE)
        equals(
            getEncodingFromFile(byteArray(0x00, 0x00, 0xFE, 0xFF)),
            ReliableTxtEncoding.UTF_32
        )
    }

    private fun getEncodingFromFile(bytes: ByteArray): ReliableTxtEncoding {
        val filePath = "Test.txt"
        Files.write(Paths.get(filePath), bytes)
        return ReliableTxtDecoder.getEncodingFromFile(filePath)
    }

    @Test
    fun getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException() {
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray())
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEE))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF, 0xBC))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF, 0xBB))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(
            byteArray(0xEF, 0xBB, 0xBE)
        )
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(
            byteArray(0xEF, 0xBC, 0xBF)
        )
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE, 0xFE))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF, 0xFF))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00, 0x00))
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(
            byteArray(0x00, 0x00, 0xFE)
        )
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(
            byteArray(0x00, 0x00, 0xFE, 0xFE)
        )
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(
            byteArray(0x01, 0x00, 0xFE, 0xFF)
        )
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(
            byteArray(0x00, 0x01, 0xFE, 0xFF)
        )
        getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(
            byteArray(0x00, 0x00, 0xFF, 0xFE)
        )
    }

    private fun getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(bytes: ByteArray) {
        try {
            val filePath = "Test.txt"
            Files.write(Paths.get(filePath), bytes)
            ReliableTxtDecoder.getEncodingFromFile(filePath)
        } catch (e: ReliableTxtException) {
            equals(e.message, "Document does not have a ReliableTXT preamble")
            return
        }
        throw RuntimeException("Preamble is valid")
    }

    @Test
    fun decode() {
        decode(byteArray(0xEF, 0xBB, 0xBF), ReliableTxtEncoding.UTF_8, "")
        decode(byteArray(0xEF, 0xBB, 0xBF, 0x61), ReliableTxtEncoding.UTF_8, "a")
        decode(byteArray(0xEF, 0xBB, 0xBF, 0x00), ReliableTxtEncoding.UTF_8, "\u0000")
        decode(byteArray(0xEF, 0xBB, 0xBF, 0xC3, 0x9F), ReliableTxtEncoding.UTF_8, "\u00DF")
        decode(byteArray(0xEF, 0xBB, 0xBF, 0xE6, 0x9D, 0xB1), ReliableTxtEncoding.UTF_8, "\u6771")
        decode(
            byteArray(0xEF, 0xBB, 0xBF, 0xF0, 0xA0, 0x80, 0x87),
            ReliableTxtEncoding.UTF_8,
            "\uD840\uDC07"
        )
        decode(byteArray(0xFE, 0xFF), ReliableTxtEncoding.UTF_16, "")
        decode(byteArray(0xFE, 0xFF, 0x00, 0x61), ReliableTxtEncoding.UTF_16, "a")
        decode(byteArray(0xFE, 0xFF, 0x00, 0x00), ReliableTxtEncoding.UTF_16, "\u0000")
        decode(byteArray(0xFE, 0xFF, 0x00, 0xDF), ReliableTxtEncoding.UTF_16, "\u00DF")
        decode(byteArray(0xFE, 0xFF, 0x67, 0x71), ReliableTxtEncoding.UTF_16, "\u6771")
        decode(
            byteArray(0xFE, 0xFF, 0xD8, 0x40, 0xDC, 0x07),
            ReliableTxtEncoding.UTF_16,
            "\uD840\uDC07"
        )
        decode(byteArray(0xFF, 0xFE), ReliableTxtEncoding.UTF_16_REVERSE, "")
        decode(byteArray(0xFF, 0xFE, 0x61, 0x00), ReliableTxtEncoding.UTF_16_REVERSE, "a")
        decode(byteArray(0xFF, 0xFE, 0x00, 0x00), ReliableTxtEncoding.UTF_16_REVERSE, "\u0000")
        decode(byteArray(0xFF, 0xFE, 0xDF, 0x00), ReliableTxtEncoding.UTF_16_REVERSE, "\u00DF")
        decode(byteArray(0xFF, 0xFE, 0x71, 0x67), ReliableTxtEncoding.UTF_16_REVERSE, "\u6771")
        decode(
            byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x07, 0xDC),
            ReliableTxtEncoding.UTF_16_REVERSE,
            "\uD840\uDC07"
        )
        decode(byteArray(0x00, 0x00, 0xFE, 0xFF), ReliableTxtEncoding.UTF_32, "")
        decode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x61),
            ReliableTxtEncoding.UTF_32,
            "a"
        )
        decode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00),
            ReliableTxtEncoding.UTF_32,
            "\u0000"
        )
        decode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0xDF),
            ReliableTxtEncoding.UTF_32,
            "\u00DF"
        )
        decode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x67, 0x71),
            ReliableTxtEncoding.UTF_32,
            "\u6771"
        )
        decode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x02, 0x00, 0x07),
            ReliableTxtEncoding.UTF_32,
            "\uD840\uDC07"
        )
    }

    @Test
    fun decode_UTF32RGiven_ShouldBeMisinterpretedAsUTF16R() {
        decode(
            byteArray(0xFF, 0xFE, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00),
            ReliableTxtEncoding.UTF_16_REVERSE,
            "\u0000a\u0000"
        )
    }

    private fun decode(
        bytes: ByteArray,
        expectedEncoding: ReliableTxtEncoding,
        expectedStr: String
    ) {
        val (encoding, str) = decode(bytes)
        equals(encoding, expectedEncoding)
        equals(str, expectedStr)
    }

    @Test
    fun decode_InvalidEncodedDataGiven_ShouldThrowException() {
        decode_InvalidEncodedDataGiven_ShouldThrowException(
            byteArray(0xEF, 0xBB, 0xBF, 0xFF),
            "The UTF_8 encoded text contains invalid data."
        )
        decode_InvalidEncodedDataGiven_ShouldThrowException(
            byteArray(0xFE, 0xFF, 0xD8, 0x40, 0x00, 0x61),
            "The UTF_16 encoded text contains invalid data."
        )
        decode_InvalidEncodedDataGiven_ShouldThrowException(
            byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x61, 0x00),
            "The UTF_16_REVERSE encoded text contains invalid data."
        )
        decode_InvalidEncodedDataGiven_ShouldThrowException(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x11, 0x00, 0x00),
            "The UTF_32 encoded text contains invalid data."
        )
    }

    private fun decode_InvalidEncodedDataGiven_ShouldThrowException(
        bytes: ByteArray,
        expectedMessage: String
    ) {
        try {
            decode(bytes)
        } catch (e: ReliableTxtException) {
            equals(e.message, expectedMessage)
            return
        }
        throw RuntimeException("Encoded data was valid")
    }

    private fun byteArray(vararg values: Int): ByteArray =
        ByteArray(values.size) { i -> values[i].toByte() }
}
