package reliableKt.reliableTXT

import reliableKt.reliableTXT.ReliableTxtLines.join
import reliableKt.reliableTXT.ReliableTxtLines.split
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ReliableTxtLinesTest {
    @Test
    fun split() {
        checkSplit("", arrayOf(""))
        checkSplit("Line1", arrayOf("Line1"))
        checkSplit("Line1\nLine2", arrayOf("Line1", "Line2"))
        checkSplit("Line1\nLine2\nLine3", arrayOf("Line1", "Line2", "Line3"))
    }

    private fun checkSplit(text: String, expectedResult: Array<String>) {
        assertThat(split(text)).isEqualTo(expectedResult)
    }

    @Test
    fun join() {
        checkJoin(arrayOf(), "")
        checkJoin(arrayOf("Line1"), "Line1")
        checkJoin(arrayOf("Line1", "Line2"), "Line1\nLine2")
        checkJoin(arrayOf("Line1", "Line2", "Line3"), "Line1\nLine2\nLine3")
    }

    private fun checkJoin(lines: Array<String>, expectedResult: String) {
        assertThat(join(*lines)).isEqualTo(expectedResult)
        assertThat(join(listOf(*lines))).isEqualTo(expectedResult)
    }
}
