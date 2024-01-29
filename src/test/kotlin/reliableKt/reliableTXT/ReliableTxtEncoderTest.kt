package reliableKt.reliableTXT

import reliableKt.reliableTXT.ReliableTxtEncoder.encode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test

class ReliableTxtEncoderTest {
    @Test
    fun encode() {
        checkEncode("", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF))
        checkEncode("a", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0x61))
        checkEncode("\u0000", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0x00))
        checkEncode("\u00DF", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0xC3, 0x9F))
        checkEncode("\u6771", ReliableTxtEncoding.UTF_8, byteArray(0xEF, 0xBB, 0xBF, 0xE6, 0x9D, 0xB1))
        checkEncode(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_8,
            byteArray(0xEF, 0xBB, 0xBF, 0xF0, 0xA0, 0x80, 0x87)
        )
        checkEncode("", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF))
        checkEncode("a", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0x61))
        checkEncode("\u0000", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0x00))
        checkEncode("\u00DF", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x00, 0xDF))
        checkEncode("\u6771", ReliableTxtEncoding.UTF_16, byteArray(0xFE, 0xFF, 0x67, 0x71))
        checkEncode(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_16,
            byteArray(0xFE, 0xFF, 0xD8, 0x40, 0xDC, 0x07)
        )
        checkEncode("", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE))
        checkEncode("a", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE, 0x61, 0x00))
        checkEncode("\u0000", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE, 0x00, 0x00))
        checkEncode("\u00DF", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE, 0xDF, 0x00))
        checkEncode("\u6771", ReliableTxtEncoding.UTF_16_REVERSE, byteArray(0xFF, 0xFE, 0x71, 0x67))
        checkEncode(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_16_REVERSE,
            byteArray(0xFF, 0xFE, 0x40, 0xD8, 0x07, 0xDC)
        )
        checkEncode("", ReliableTxtEncoding.UTF_32, byteArray(0x00, 0x00, 0xFE, 0xFF))
        checkEncode(
            "a",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x61)
        )
        checkEncode(
            "\u0000",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00)
        )
        checkEncode(
            "\u00DF",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0xDF)
        )
        checkEncode(
            "\u6771",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x67, 0x71)
        )
        checkEncode(
            "\uD840\uDC07",
            ReliableTxtEncoding.UTF_32,
            byteArray(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x02, 0x00, 0x07)
        )
    }

    private fun checkEncode(str: String, encoding: ReliableTxtEncoding, expectedBytes: ByteArray) {
        assertThat(encode(str, encoding)).isEqualTo(expectedBytes)
    }

    @Test
    fun encode_InvalidSurrogateGiven_ShouldThrowException() {
        encode_InvalidSurrogateGiven_ShouldThrowException("\uD840\u0061", ReliableTxtEncoding.UTF_8)
        encode_InvalidSurrogateGiven_ShouldThrowException(
            "\uD840\u0061",
            ReliableTxtEncoding.UTF_16
        )
        encode_InvalidSurrogateGiven_ShouldThrowException(
            "\uD840\u0061",
            ReliableTxtEncoding.UTF_16_REVERSE
        )
        encode_InvalidSurrogateGiven_ShouldThrowException(
            "\uD840\u0061",
            ReliableTxtEncoding.UTF_32
        )
        encode_InvalidSurrogateGiven_ShouldThrowException("\uDC07\uD840", ReliableTxtEncoding.UTF_8)
        encode_InvalidSurrogateGiven_ShouldThrowException(
            "\uDC07\uD840",
            ReliableTxtEncoding.UTF_16
        )
        encode_InvalidSurrogateGiven_ShouldThrowException(
            "\uDC07\uD840",
            ReliableTxtEncoding.UTF_16_REVERSE
        )
        encode_InvalidSurrogateGiven_ShouldThrowException(
            "\uDC07\uD840",
            ReliableTxtEncoding.UTF_32
        )
    }

    private fun encode_InvalidSurrogateGiven_ShouldThrowException(
        str: String,
        encoding: ReliableTxtEncoding
    ) {
        assertThrows(Exception::class.java) {
            encode(str, encoding)
        }
    }

    private fun byteArray(vararg values: Int): ByteArray {
        val bytes = ByteArray(values.size)
        for (i in values.indices) {
            bytes[i] = values[i].toByte()
        }
        return bytes
    }
}
