package reliableKt.reliableTXT

import org.assertj.core.api.Assertions.assertThat
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.test.Test

class ReliableTxtEncodingTest {
    @Test
    fun values() {
        val expResult = arrayOf(
            ReliableTxtEncoding.UTF_8,
            ReliableTxtEncoding.UTF_16,
            ReliableTxtEncoding.UTF_16_REVERSE,
            ReliableTxtEncoding.UTF_32
        )
        assertThat(ReliableTxtEncoding.entries.toTypedArray()).isEqualTo(expResult)
    }

    @Test
    fun name() {
        assertThat(ReliableTxtEncoding.UTF_8.name).isEqualTo("UTF_8")
        assertThat(ReliableTxtEncoding.UTF_16.name).isEqualTo("UTF_16")
        assertThat(ReliableTxtEncoding.UTF_16_REVERSE.name).isEqualTo("UTF_16_REVERSE")
        assertThat(ReliableTxtEncoding.UTF_32.name).isEqualTo("UTF_32")
    }

    @Test
    fun valueOf() {
        assertThat(ReliableTxtEncoding.valueOf("UTF_8")).isEqualTo(ReliableTxtEncoding.UTF_8)
        assertThat(ReliableTxtEncoding.valueOf("UTF_16")).isEqualTo(ReliableTxtEncoding.UTF_16)
        assertThat(ReliableTxtEncoding.valueOf("UTF_16_REVERSE")).isEqualTo(ReliableTxtEncoding.UTF_16_REVERSE)
        assertThat(ReliableTxtEncoding.valueOf("UTF_32")).isEqualTo(ReliableTxtEncoding.UTF_32)
    }

    @Test
    fun charset() {
        assertThat(ReliableTxtEncoding.UTF_8.charset).isEqualTo(StandardCharsets.UTF_8)
        assertThat(ReliableTxtEncoding.UTF_16.charset).isEqualTo(StandardCharsets.UTF_16BE)
        assertThat(ReliableTxtEncoding.UTF_16_REVERSE.charset).isEqualTo(StandardCharsets.UTF_16LE)
        assertThat(ReliableTxtEncoding.UTF_32.charset).isEqualTo(Charset.forName("UTF-32BE"))
    }

    @Test
    fun preambleLength() {
        assertThat(ReliableTxtEncoding.UTF_8.preambleLength.toInt()).isEqualTo(3)
        assertThat(ReliableTxtEncoding.UTF_16.preambleLength.toInt()).isEqualTo(2)
        assertThat(ReliableTxtEncoding.UTF_16_REVERSE.preambleLength.toInt()).isEqualTo(2)
        assertThat(ReliableTxtEncoding.UTF_32.preambleLength.toInt()).isEqualTo(4)
    }
}
