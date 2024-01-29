package com.stenway.reliabletxt

data class LineInfo(val lineIndex: Int, val linePosition: Int)

class ReliableTxtCharIterator(text: String) {
    val chars = text.codePoints().toArray()
    var index = 0

    fun getText(): String = String(chars, 0, chars.size)

    fun getLineInfo(): LineInfo {
        var lineIndex = 0
        var linePosition = 0
        for (i in 0..<index) {
            if (chars[i] == '\n'.code) {
                lineIndex++
                linePosition = 0
            } else {
                linePosition++
            }
        }
        return LineInfo(lineIndex, linePosition)
    }

    fun isEndOfText(): Boolean = index >= chars.size

    fun isChar(c: Int): Boolean = when {
        isEndOfText() -> false
        else -> chars[index] == c
    }

    fun tryReadChar(c: Int): Boolean = when {
        !isChar(c) -> false
        else -> {
            index++
            true
        }
    }
}
