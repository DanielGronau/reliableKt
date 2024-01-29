package reliableKt.reliableTXT

import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.CodingErrorAction
import java.util.*

data class DecodingResult(val encoding: ReliableTxtEncoding, val text: String)

object ReliableTxtDecoder {

    private const val NO_RELIABLETXT_PREAMBLE = "Document does not have a ReliableTXT preamble"

    fun getEncoding(bytes: ByteArray): ReliableTxtEncoding {
        return when {
            bytes.size >= 3 &&
                bytes[0] == 0xEF.toByte() &&
                bytes[1] == 0xBB.toByte() &&
                bytes[2] == 0xBF.toByte() -> ReliableTxtEncoding.UTF_8

            bytes.size >= 2 &&
                bytes[0] == 0xFE.toByte() &&
                bytes[1] == 0xFF.toByte() -> ReliableTxtEncoding.UTF_16

            bytes.size >= 2 &&
                bytes[0] == 0xFF.toByte() &&
                bytes[1] == 0xFE.toByte() -> ReliableTxtEncoding.UTF_16_REVERSE

            bytes.size >= 4 &&
                bytes[0].toInt() == 0 &&
                bytes[1].toInt() == 0 &&
                bytes[2] == 0xFE.toByte() &&
                bytes[3] == 0xFF.toByte() -> ReliableTxtEncoding.UTF_32

            else ->
                throw ReliableTxtException(NO_RELIABLETXT_PREAMBLE)
        }
    }

    @Throws(IOException::class)
    fun getEncodingFromFile(filePath: String): ReliableTxtEncoding {
        val bytes = ByteArray(4)
        FileInputStream(filePath).use { inputStream ->
            if (inputStream.read(bytes, 0, 2) == 2) {
                when {
                    bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() -> {
                        if (inputStream.read(bytes, 2, 1) == 1 &&
                            bytes[2] == 0xBF.toByte()
                        ) {
                            return ReliableTxtEncoding.UTF_8
                        }
                    }

                    bytes[0] == 0xFE.toByte() && bytes[1] == 0xFF.toByte() ->
                        return ReliableTxtEncoding.UTF_16

                    bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte() ->
                        return ReliableTxtEncoding.UTF_16_REVERSE

                    bytes[0].toInt() == 0 && bytes[1].toInt() == 0 -> {
                        if (inputStream.read(bytes, 2, 2) == 2 &&
                            bytes[2] == 0xFE.toByte() &&
                            bytes[3] == 0xFF.toByte()
                        ) {
                            return ReliableTxtEncoding.UTF_32
                        }
                    }
                }
            }
        }
        throw ReliableTxtException(NO_RELIABLETXT_PREAMBLE)
    }

    fun decode(bytes: ByteArray): DecodingResult {
        val detectedEncoding = getEncoding(bytes)
        val charset = detectedEncoding.charset
        val preambleLength = detectedEncoding.preambleLength
        val decoder = charset.newDecoder()
        decoder.onMalformedInput(CodingErrorAction.REPORT)
        decoder.onUnmappableCharacter(CodingErrorAction.REPORT)
        val byteBuffer = ByteBuffer.wrap(
            bytes,
            preambleLength.toInt(),
            bytes.size - preambleLength
        )
        return try {
            val charBuffer = decoder.decode(byteBuffer)
            DecodingResult(detectedEncoding, charBuffer.toString())
        } catch (e: Exception) {
            throw ReliableTxtException(
                "The ${detectedEncoding.name} encoded text contains invalid data."
            )
        }
    }
}
