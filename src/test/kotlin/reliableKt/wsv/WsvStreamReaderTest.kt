package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import reliableKt.reliableTXT.ReliableTxtDocument
import reliableKt.reliableTXT.ReliableTxtEncoding
import reliableKt.reliableTXT.ReliableTxtEncoding.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test

class WsvStreamReaderTest {
    @Test
    fun readLine() {
        checkReadLine(UTF_8)
        checkReadLine(UTF_16)
        checkReadLine(UTF_16_REVERSE)
        checkReadLine(UTF_32)
    }

    private fun checkReadLine(encoding: ReliableTxtEncoding) {
        val filePath = "Test.wsv"
        ReliableTxtDocument.save("V11 V12\nV21 V22\nV31 V32", encoding, filePath)
        WsvStreamReader(filePath).use { reader ->
            assertThat(reader.encoding).isEqualTo(encoding)
            var line: WsvLine?
            var lineCount = 0
            while ((reader.readLine().also { line = it }) != null) {
                lineCount++
                assertThat(line!!.values.get(0)).isEqualTo("V" + lineCount + "1")
            }
            assertThat(lineCount).isEqualTo(3)
        }
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun cleanup() {
            Files.deleteIfExists(Path.of("Test.wsv"))
        }
    }
}
