package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class WsvStringTest {
    @Test
    fun isWhitespace() {
        val wsCodePoints: IntArray = WsvChar.whitespaceCodePoints
        assertThat(String(wsCodePoints, 0, wsCodePoints.size).isWhitespace()).isTrue()
    }

    @Test
    fun isWhitespace_NonWhitespaceGiven_ShouldBeFalse() {
        assertThat(null.isWhitespace()).isFalse()
        assertThat("".isWhitespace()).isFalse()
        assertThat(" a ".isWhitespace()).isFalse()
        assertThat("\n".isWhitespace()).isFalse()
    }
}
