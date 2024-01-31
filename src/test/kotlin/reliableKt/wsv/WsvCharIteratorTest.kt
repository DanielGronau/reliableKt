package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test


class WsvCharIteratorTest {
    @Test
    fun readValue() {
        val charIterator = WsvCharIterator("Value1 Value2")
        assertThat(charIterator.readValue()).isEqualTo("Value1")
    }

    @Test
    fun readValue_EmptyTextGiven_ShouldThrowException() {
        assertThatThrownBy {
            val charIterator = WsvCharIterator("")
            charIterator.readValue()
        }
            .isInstanceOf(WsvParserException::class.java)
            .hasMessage("Invalid value (1, 1)")
    }
}