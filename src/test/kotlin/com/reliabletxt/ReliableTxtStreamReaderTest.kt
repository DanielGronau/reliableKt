package com.stenway.reliabletxt

import com.stenway.reliabletxt.Assert.equals
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
            equals(reader.encoding, encoding)
            var line: String? = null
            var lineCount = 0
            while (reader.readLine().also { line = it } != null) {
                lineCount++
                equals(line, "Line $lineCount")
            }
            equals(lineCount.toLong(), 3)
        }
    }
}
