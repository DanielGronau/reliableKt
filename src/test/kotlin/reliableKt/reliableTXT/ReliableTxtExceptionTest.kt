package reliableKt.reliableTXT

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ReliableTxtExceptionTest {
    @Test
    fun message() {
        assertThat(ReliableTxtException("Test").message).isEqualTo("Test")
    }
}
