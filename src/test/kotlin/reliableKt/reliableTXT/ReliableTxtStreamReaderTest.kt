package reliableKt.reliableTXT

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test

class ReliableTxtStreamReaderTest {
    @Test
    fun readLine() {
        readLine(ReliableTxtEncoding.UTF_8)
        readLine(ReliableTxtEncoding.UTF_16)
        readLine(ReliableTxtEncoding.UTF_16_REVERSE)
        readLine(ReliableTxtEncoding.UTF_32)
    }

    private fun readLine(encoding: ReliableTxtEncoding) {
        val filePath = "Test.txt"
        ReliableTxtDocument.save("Line 1\nLine 2\nLine 3", encoding, filePath)
        ReliableTxtStreamReader(filePath).use { reader ->
            assertThat(reader.encoding).isEqualTo(encoding)
            var line: String?
            var lineCount = 0
            while (reader.readLine().also { line = it } != null) {
                lineCount++
                assertThat(line).isEqualTo("Line $lineCount")
            }
            assertThat(lineCount).isEqualTo(3)
        }
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun cleanup() {
            Files.deleteIfExists(Path.of("Test.txt"))
        }
    }
}
