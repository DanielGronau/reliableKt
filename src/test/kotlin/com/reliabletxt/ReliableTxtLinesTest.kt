package com.stenway.reliabletxt

import com.stenway.reliabletxt.Assert.equals
import com.stenway.reliabletxt.ReliableTxtLines.join
import com.stenway.reliabletxt.ReliableTxtLines.split
import java.util.*
import kotlin.test.Test

class ReliableTxtLinesTest {
    @Test
    fun split() {
        split("", arrayOf(""))
        split("Line1", arrayOf("Line1"))
        split("Line1\nLine2", arrayOf("Line1", "Line2"))
        split("Line1\nLine2\nLine3", arrayOf("Line1", "Line2", "Line3"))
    }

    private fun split(text: String, expectedResult: Array<String>) {
        equals(split(text), expectedResult)
    }

    @Test
    fun join() {
        join(arrayOf(), "")
        join(arrayOf("Line1"), "Line1")
        join(arrayOf("Line1", "Line2"), "Line1\nLine2")
        join(arrayOf("Line1", "Line2", "Line3"), "Line1\nLine2\nLine3")
    }

    private fun join(lines: Array<String>, expectedResult: String) {
        equals(join(*lines), expectedResult)
        val linesList = Arrays.asList(*lines)
        equals(join(linesList), expectedResult)
    }
}
