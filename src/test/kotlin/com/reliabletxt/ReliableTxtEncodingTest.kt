package com.reliabletxt

import com.reliabletxt.Assert.equals
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
        equals(ReliableTxtEncoding.entries.toTypedArray(), expResult)
    }

    @Test
    fun name() {
        equals(ReliableTxtEncoding.UTF_8.name, "UTF_8")
        equals(ReliableTxtEncoding.UTF_16.name, "UTF_16")
        equals(ReliableTxtEncoding.UTF_16_REVERSE.name, "UTF_16_REVERSE")
        equals(ReliableTxtEncoding.UTF_32.name, "UTF_32")
    }

    @Test
    fun valueOf() {
        equals(ReliableTxtEncoding.valueOf("UTF_8"), ReliableTxtEncoding.UTF_8)
        equals(ReliableTxtEncoding.valueOf("UTF_16"), ReliableTxtEncoding.UTF_16)
        equals(ReliableTxtEncoding.valueOf("UTF_16_REVERSE"), ReliableTxtEncoding.UTF_16_REVERSE)
        equals(ReliableTxtEncoding.valueOf("UTF_32"), ReliableTxtEncoding.UTF_32)
    }

    @Test
    fun charset() {
        equals(ReliableTxtEncoding.UTF_8.charset, StandardCharsets.UTF_8)
        equals(ReliableTxtEncoding.UTF_16.charset, StandardCharsets.UTF_16BE)
        equals(ReliableTxtEncoding.UTF_16_REVERSE.charset, StandardCharsets.UTF_16LE)
        equals(ReliableTxtEncoding.UTF_32.charset, Charset.forName("UTF-32BE"))
    }

    @Test
    fun preambleLength() {
        equals(ReliableTxtEncoding.UTF_8.preambleLength.toLong(), 3)
        equals(ReliableTxtEncoding.UTF_16.preambleLength.toLong(), 2)
        equals(ReliableTxtEncoding.UTF_16_REVERSE.preambleLength.toLong(), 2)
        equals(ReliableTxtEncoding.UTF_32.preambleLength.toLong(), 4)
    }
}
