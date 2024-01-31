package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class WsvParserExceptionTest {
    @Test
    fun message() {
        assertThat(WsvParserException(1, 2, 3, "Test")).hasMessage("Test (3, 4)")
    }
}