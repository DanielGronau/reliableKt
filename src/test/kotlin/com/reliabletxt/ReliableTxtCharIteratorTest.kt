package com.stenway.reliabletxt

import com.stenway.reliabletxt.Assert.equals
import javax.sound.sampled.Line
import kotlin.test.Test

class ReliableTxtCharIteratorTest {

    @Test
    fun text_ShouldEqualConstructorArgument() {
        getText_ShouldEqualConstructorArgument("")
        getText_ShouldEqualConstructorArgument("abc")
    }

    private fun getText_ShouldEqualConstructorArgument(text: String) {
        equals(ReliableTxtCharIterator(text).getText(), text)
    }

    @Test
    fun lineInfo() {
        equals(newIterator("").getLineInfo(), LineInfo(0, 0))
        equals(newIterator("abc").getLineInfo(), LineInfo(0, 0))
        equals(newIterator("abc", 'a'.code).getLineInfo(), LineInfo(0, 1))
        equals(newIterator("abc", 'a'.code, 'b'.code).getLineInfo(), LineInfo(0, 2))
        equals(
            newIterator("abc", 'a'.code, 'b'.code, 'c'.code).getLineInfo(),
            LineInfo(0, 3)
        )
        equals(newIterator("a\nb").getLineInfo(), LineInfo(0, 0))
        equals(newIterator("a\nb", 'a'.code).getLineInfo(), LineInfo(0, 1))
        equals(newIterator("a\nb", 'a'.code, '\n'.code).getLineInfo(), LineInfo(1, 0))
        equals(
            newIterator("a\nb", 'a'.code, '\n'.code, 'b'.code).getLineInfo(),
            LineInfo(1, 1)
        )
        equals(newIterator("\n\n\n").getLineInfo(), LineInfo(0, 0))
        equals(newIterator("\n\n\n", '\n'.code).getLineInfo(), LineInfo(1, 0))
        equals(newIterator("\n\n\n", '\n'.code, '\n'.code).getLineInfo(), LineInfo(2, 0))
        equals(
            newIterator("\n\n\n", '\n'.code, '\n'.code, '\n'.code).getLineInfo(),
            LineInfo(3, 0)
        )
        equals(newIterator("a\uD840\uDC07\nb").getLineInfo(), LineInfo(0, 0))
        equals(newIterator("a\uD840\uDC07\nb", 'a'.code).getLineInfo(), LineInfo(0, 1))
        equals(
            newIterator("a\uD840\uDC07\nb", 'a'.code, 0x20007).getLineInfo(),
            LineInfo(0, 2)
        )
        equals(
            newIterator("a\uD840\uDC07\nb", 'a'.code, 0x20007, '\n'.code).getLineInfo(),
            LineInfo(1, 0)
        )
    }

    @Test
    fun isEndOfText() {
        equals(newIterator("").isEndOfText(), true)
        equals(newIterator("abc").isEndOfText(), false)
        equals(newIterator("abc", 'a'.code).isEndOfText(), false)
        equals(newIterator("abc", 'a'.code, 'b'.code).isEndOfText(), false)
        equals(newIterator("abc", 'a'.code, 'b'.code, 'c'.code).isEndOfText(), true)
        equals(newIterator("a\nb").isEndOfText(), false)
        equals(newIterator("a\nb", 'a'.code).isEndOfText(), false)
        equals(newIterator("a\nb", 'a'.code, '\n'.code).isEndOfText(), false)
        equals(newIterator("a\nb", 'a'.code, '\n'.code, 'b'.code).isEndOfText(), true)
    }

    @Test
    fun isChar() {
        equals(newIterator("").isChar('a'.code), false)
        equals(newIterator("abc").isChar('a'.code), true)
        equals(newIterator("abc").isChar('b'.code), false)
        equals(newIterator("abc", 'a'.code).isChar('a'.code), false)
        equals(newIterator("abc", 'a'.code).isChar('b'.code), true)
        equals(newIterator("abc", 'a'.code, 'b'.code).isChar('c'.code), true)
        equals(newIterator("abc", 'a'.code, 'b'.code, 'c'.code).isChar('c'.code), false)
    }

    @Test
    fun tryReadChar() {
        val iterator = ReliableTxtCharIterator("abc")
        equals(iterator.tryReadChar('b'.code), false)
        equals(iterator.getLineInfo(), LineInfo(0, 0))
        equals(iterator.tryReadChar('a'.code), true)
        equals(iterator.getLineInfo(), LineInfo(0, 1))
    }

    private fun newIterator(text: String, vararg chars: Int): ReliableTxtCharIterator {
        val charIterator = ReliableTxtCharIterator(text)
        for (c in chars) {
            if (!charIterator.tryReadChar(c)) throw RuntimeException()
        }
        return charIterator
    }
}
