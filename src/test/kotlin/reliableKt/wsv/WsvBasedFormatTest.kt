package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import reliableKt.wsv.WsvBasedFormat.getWhitespaces
import kotlin.test.Test


class WsvBasedFormatTest {
    @Test
    fun whitespaces() {
        val line = WsvLine.parse("a  b  c #dfd")
        val whitespaces = getWhitespaces(line)
        assertThat(whitespaces).containsExactly(null, "  ", "  ", " ")
    }
}