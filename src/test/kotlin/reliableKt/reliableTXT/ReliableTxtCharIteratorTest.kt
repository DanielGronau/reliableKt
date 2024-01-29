package reliableKt.reliableTXT

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ReliableTxtCharIteratorTest {

    @Test
    fun text_ShouldEqualConstructorArgument() {
        getText_ShouldEqualConstructorArgument("")
        getText_ShouldEqualConstructorArgument("abc")
    }

    private fun getText_ShouldEqualConstructorArgument(text: String) {
        assertThat(ReliableTxtCharIterator(text).getText())
            .isEqualTo(text)
    }

    @Test
    fun lineInfo() {
        assertThat(newIterator("").getLineInfo())
            .isEqualTo(LineInfo(0, 0))
        assertThat(newIterator("abc").getLineInfo())
            .isEqualTo(LineInfo(0, 0))
        assertThat(newIterator("abc", 'a'.code).getLineInfo())
            .isEqualTo(LineInfo(0, 1))
        assertThat(newIterator("abc", 'a'.code, 'b'.code).getLineInfo())
            .isEqualTo(LineInfo(0, 2))
        assertThat(newIterator("abc", 'a'.code, 'b'.code, 'c'.code).getLineInfo())
            .isEqualTo(LineInfo(0, 3))
        assertThat(newIterator("a\nb").getLineInfo())
            .isEqualTo(LineInfo(0, 0))
        assertThat(newIterator("a\nb", 'a'.code).getLineInfo())
            .isEqualTo(LineInfo(0, 1))
        assertThat(newIterator("a\nb", 'a'.code, '\n'.code).getLineInfo())
            .isEqualTo(LineInfo(1, 0))
        assertThat(newIterator("a\nb", 'a'.code, '\n'.code, 'b'.code).getLineInfo())
            .isEqualTo(LineInfo(1, 1))
        assertThat(newIterator("\n\n\n").getLineInfo())
            .isEqualTo(LineInfo(0, 0))
        assertThat(newIterator("\n\n\n", '\n'.code).getLineInfo())
            .isEqualTo(LineInfo(1, 0))
        assertThat(newIterator("\n\n\n", '\n'.code, '\n'.code).getLineInfo())
            .isEqualTo(LineInfo(2, 0))
        assertThat(newIterator("\n\n\n", '\n'.code, '\n'.code, '\n'.code).getLineInfo())
            .isEqualTo(LineInfo(3, 0))
        assertThat(newIterator("a\uD840\uDC07\nb").getLineInfo())
            .isEqualTo(LineInfo(0, 0))
        assertThat(newIterator("a\uD840\uDC07\nb", 'a'.code).getLineInfo())
            .isEqualTo(LineInfo(0, 1))
        assertThat(newIterator("a\uD840\uDC07\nb", 'a'.code, 0x20007).getLineInfo())
            .isEqualTo(LineInfo(0, 2))
        assertThat(newIterator("a\uD840\uDC07\nb", 'a'.code, 0x20007, '\n'.code).getLineInfo())
            .isEqualTo(LineInfo(1, 0))
    }

    @Test
    fun isEndOfText() {
        assertThat(newIterator("").isEndOfText()).isTrue()
        assertThat(newIterator("abc").isEndOfText()).isFalse()
        assertThat(newIterator("abc", 'a'.code).isEndOfText()).isFalse()
        assertThat(newIterator("abc", 'a'.code, 'b'.code).isEndOfText()).isFalse()
        assertThat(newIterator("abc", 'a'.code, 'b'.code, 'c'.code).isEndOfText()).isTrue()
        assertThat(newIterator("a\nb").isEndOfText()).isFalse()
        assertThat(newIterator("a\nb", 'a'.code).isEndOfText()).isFalse()
        assertThat(newIterator("a\nb", 'a'.code, '\n'.code).isEndOfText()).isFalse()
        assertThat(newIterator("a\nb", 'a'.code, '\n'.code, 'b'.code).isEndOfText()).isTrue()
    }

    @Test
    fun isChar() {
        assertThat(newIterator("").isChar('a'.code)).isFalse()
        assertThat(newIterator("abc").isChar('a'.code)).isTrue()
        assertThat(newIterator("abc").isChar('b'.code)).isFalse()
        assertThat(newIterator("abc", 'a'.code).isChar('a'.code)).isFalse()
        assertThat(newIterator("abc", 'a'.code).isChar('b'.code)).isTrue()
        assertThat(newIterator("abc", 'a'.code, 'b'.code).isChar('c'.code)).isTrue()
        assertThat(newIterator("abc", 'a'.code, 'b'.code, 'c'.code).isChar('c'.code)).isFalse()
    }

    @Test
    fun tryReadChar() {
        val iterator = ReliableTxtCharIterator("abc")
        assertThat(iterator.tryReadChar('b'.code)).isFalse()
        assertThat(iterator.getLineInfo()).isEqualTo(LineInfo(0, 0))
        assertThat(iterator.tryReadChar('a'.code)).isTrue()
        assertThat(iterator.getLineInfo()).isEqualTo(LineInfo(0, 1))
    }

    private fun newIterator(text: String, vararg chars: Int): ReliableTxtCharIterator {
        val charIterator = ReliableTxtCharIterator(text)
        for (c in chars) {
            if (!charIterator.tryReadChar(c)) throw RuntimeException()
        }
        return charIterator
    }
}
