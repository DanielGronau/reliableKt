package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reliableKt.wsv.WsvChar.isWhitespace


class WsvCharTest {

    private val whitespaceChars = intArrayOf(
        0x0009,
        0x000B,
        0x000C,
        0x000D,
        0x0020,
        0x0085,
        0x00A0,
        0x1680,
        0x2000,
        0x2001,
        0x2002,
        0x2003,
        0x2004,
        0x2005,
        0x2006,
        0x2007,
        0x2008,
        0x2009,
        0x200A,
        0x2028,
        0x2029,
        0x202F,
        0x205F,
        0x3000
    )


    @Test
    fun isWhitespace() {
        for (wsChar in whitespaceChars) {
            assertThat(isWhitespace(wsChar)).isTrue()
        }
    }

    @Test
    fun isWhitespace_NonWhitespaceGiven_ShouldBeFalse() {
        val wsList: List<Int> = whitespaceChars.toList()
        for (c in 0..0x10FFFF) {
            if (!wsList.contains(c)) {
                assertThat(isWhitespace(c)).isFalse()
            }
        }
    }

    @Test
    fun isWhitespace_LineFeedGiven_ShouldBeFalse() {
        assertThat(isWhitespace('\n'.code)).isFalse()
    }

    @Test
    fun whitespaceCodePoints() {
        assertThat(WsvChar.whitespaceCodePoints).isEqualTo(whitespaceChars)
    }
}