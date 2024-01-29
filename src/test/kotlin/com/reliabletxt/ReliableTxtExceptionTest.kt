package com.stenway.reliabletxt

import com.stenway.reliabletxt.Assert.equals
import kotlin.test.Test

class ReliableTxtExceptionTest {
    @Test
    fun message() {
        equals(ReliableTxtException("Test").message, "Test")
    }
}
