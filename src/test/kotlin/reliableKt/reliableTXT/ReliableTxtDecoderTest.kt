package reliableKt.reliableTXT

import reliableKt.reliableTXT.ReliableTxtDecoder.decode
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test

class ReliableTxtDecoderTest {
    @Test
    fun getEncoding() {
        assertThat(ReliableTxtDecoder.getEncoding(byteArray(0xEF, 0xBB, 0xBF)))
            .isEqualTo(ReliableTxtEncoding.UTF_8)
        assertThat(ReliableTxtDecoder.getEncoding(byteArray(0xFE, 0xFF)))
            .isEqualTo(ReliableTxtEncoding.UTF_16)
        assertThat(ReliableTxtDecoder.getEncoding(byteArray(0xFF, 0xFE)))
            .isEqualTo(ReliableTxtEncoding.UTF_16_REVERSE)
        assertThat(ReliableTxtDecoder.getEncoding(byteArray(0x00, 0x00, 0xFE, 0xFF)))
            .isEqualTo(ReliableTxtEncoding.UTF_32)
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
        assertThatThrownBy { ReliableTxtDecoder.getEncoding(bytes) }
            .isInstanceOf(ReliableTxtException::class.java)
            .hasMessage("Document does not have a ReliableTXT preamble")
    }

    @Test
    fun getEncodingFromFile() {
        assertThat(getEncodingFromFile(byteArray(0xEF, 0xBB, 0xBF)))
            .isEqualTo(ReliableTxtEncoding.UTF_8)
        assertThat(getEncodingFromFile(byteArray(0xFE, 0xFF)))
            .isEqualTo(ReliableTxtEncoding.UTF_16)
        assertThat(getEncodingFromFile(byteArray(0xFF, 0xFE)))
            .isEqualTo(ReliableTxtEncoding.UTF_16_REVERSE)
        assertThat(getEncodingFromFile(byteArray(0x00, 0x00, 0xFE, 0xFF)))
            .isEqualTo(ReliableTxtEncoding.UTF_32)
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
        assertThatThrownBy {
            val filePath = "Test.txt"
            Files.write(Paths.get(filePath), bytes)
            ReliableTxtDecoder.getEncodingFromFile(filePath)
        }
            .isInstanceOf(ReliableTxtException::class.java)
            .hasMessage("Document does not have a ReliableTXT preamble")
    }

    @Test
    fun decode() {
        checkDecode(byteArray(0xEF, 0xBB, 0xBF), ReliableTxtEncoding.UTF_8, "")
        checkDecode(byteArray(0xEF, 0xBB, 0xBF, 0x61), ReliableTxtEncoding.UTF_8, "a")
        checkDecode(byteArray(0xEF, 0xBB, 0xBF, 0x00), ReliableTxtEncoding.UTF_8, "\u0000")
        checkDecode(byteArray(0xEF, 0xBB, 0xBF, 0xC3, 0x9F), ReliableTxtEncoding.UTF_8, "\u00DF")
        checkDecode(byteArray(0xEF, 0xBB, 0xBF, 0xE6, 0x9D, 0xB1), ReliableTxtEncoding.UTF_8, "\u6771")
        checkDecode(
            byteArray(0xEF, 0xBB, 0xBF, 0xF0, 0xA0, 0x80, 0x87),
            ReliableTxtEncoding.UTF_8,
            "\uD840\uDC07"
        )
        checkDecode(byteArray(0xFE, 0xFF), ReliableTxtEncoding.UTF_16, "")
        checkDecode(byteArray(0xFE, 0xFF, 0x00, 0x61), ReliableTxtEncoding.UTF_16, "a")
        checkDecode(byteArray(0xFE, 0xFF, 0x00, 0x00), ReliableTxtEncoding.UTF_16, "\u0000")
        checkDecode(byteArray(0xFE, 0xFF, 0x00, 0xDF), ReliableTxtEncoding.UTF_16, "\u00DF")
        checkDecode(byteArray(0xFE, 0xFF, 0x67, 0x71), ReliableTxtEncoding.UTF_16, "\u6771")
        checkDecode(
            byteArray(0xFE, 0xFF, 0xD8, 0x40, 0xDC, 0x07),
            ReliableTxtEncoding.UTF_16,
            "\uD840\uDC07"
        )
        checkDecode(byteArray(0xFF, 0xFE), ReliableTxtEncoding.UTF_16_REVERSE, "")
        checkDecode(byteArray(0xFF, 0xFE, 0x61, 0x00), ReliableTxtEncoding.UTF_16_REVERSE, "a")
        checkDecode(byteArray(0xFF, 0xFE, 0x00, 0x00), ReliableTxtEncoding.UTF_16_REVERSE, "\u0000")
        checkDecode(byteArray(0xFF, 0xFE, 0xDF, 0x00), ReliableTxtEncoding.UTF_16_REVERSE, "\u00DF")
        checkDecode(byteArray(0xFF, 0xFE, 0x71, 0x67), ReliableTxtEncoding.UTF_16_REVERSE, "\u6771")
        checkDecode(
            byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x07, 0xDC),
            ReliableTxtEncoding.UTF_16_REVERSE,
            "\uD840\uDC07"
        )
        checkDecode(byteArray(0x00, 0x00, 0xFE, 0xFF), ReliableTxtEncoding.UTF_32, "")
        checkDecode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x61),
            ReliableTxtEncoding.UTF_32,
            "a"
        )
        checkDecode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00),
            ReliableTxtEncoding.UTF_32,
            "\u0000"
        )
        checkDecode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0xDF),
            ReliableTxtEncoding.UTF_32,
            "\u00DF"
        )
        checkDecode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x67, 0x71),
            ReliableTxtEncoding.UTF_32,
            "\u6771"
        )
        checkDecode(
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x02, 0x00, 0x07),
            ReliableTxtEncoding.UTF_32,
            "\uD840\uDC07"
        )
    }

    @Test
    fun decode_UTF32RGiven_ShouldBeMisinterpretedAsUTF16R() {
        checkDecode(
            byteArray(0xFF, 0xFE, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00),
            ReliableTxtEncoding.UTF_16_REVERSE,
            "\u0000a\u0000"
        )
    }

    private fun checkDecode(
        bytes: ByteArray,
        expectedEncoding: ReliableTxtEncoding,
        expectedStr: String
    ) {
        val (encoding, str) = decode(bytes)
        assertThat(encoding).isEqualTo(expectedEncoding)
        assertThat(str).isEqualTo(expectedStr)
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
        assertThatThrownBy { decode(bytes) }
            .isInstanceOf(ReliableTxtException::class.java)
            .hasMessage(expectedMessage)
    }

    private fun byteArray(vararg values: Int): ByteArray =
        ByteArray(values.size) { i -> values[i].toByte() }
}
