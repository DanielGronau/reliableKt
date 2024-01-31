package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class WsvStringTest {
    @Test
    fun isWhitespace() {
        val wsCodePoints: IntArray = WsvChar.whitespaceCodePoints
        assertThat(WsvString.isWhitespace(String(wsCodePoints, 0, wsCodePoints.size))).isTrue()
    }

    @Test
    fun isWhitespace_NonWhitespaceGiven_ShouldBeFalse() {
        assertThat(WsvString.isWhitespace(null)).isFalse()
        assertThat(WsvString.isWhitespace("")).isFalse()
        assertThat(WsvString.isWhitespace(" a ")).isFalse()
        assertThat(WsvString.isWhitespace("\n")).isFalse()
    }
}
