package com.reliabletxt

import com.reliabletxt.Assert.equals
import kotlin.test.Test

class ReliableTxtExceptionTest {
    @Test
    fun message() {
        equals(ReliableTxtException("Test").message, "Test")
    }
}
