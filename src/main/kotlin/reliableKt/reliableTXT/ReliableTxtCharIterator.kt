package reliableKt.reliableTXT

data class LineInfo(val lineIndex: Int, val linePosition: Int)

open class ReliableTxtCharIterator(text: String) {
    protected val chars = text.codePoints().toArray()
    protected var index = 0

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

    fun isChar(c: Int): Boolean =
        !isEndOfText() && chars[index] == c

    fun tryReadChar(c: Int): Boolean = when {
        !isChar(c) -> false
        else -> {
            ++index
            true
        }
    }
}
