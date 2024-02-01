package reliableKt.wsv

import reliableKt.reliableTXT.ReliableTxtCharIterator
import reliableKt.wsv.WsvChar.isWhitespace


internal class WsvCharIterator(text: String?) : ReliableTxtCharIterator(text!!) {
    private val sb = StringBuilder()

    private fun isWhitespace() = !isEndOfText() && isWhitespace(chars[index])

    fun readCommentText(): String {
        val startIndex: Int = index
        while (true) {
            if (isEndOfText()) break
            if (chars[index] == '\n'.code) break
            index++
        }
        return String(chars, startIndex, index - startIndex)
    }

    fun skipCommentText() {
        while (true) {
            if (isEndOfText()) break
            if (chars[index] == '\n'.code) break
            index++
        }
    }

    fun readWhitespaceOrNull(): String? {
        val startIndex: Int = index
        while (true) {
            if (isEndOfText()) break
            val c: Int = chars[index]
            if (c == '\n'.code) break
            if (!isWhitespace(c)) break
            index++
        }
        return if (index == startIndex) null else String(chars, startIndex, index - startIndex)
    }

    fun skipWhitespace(): Boolean {
        val startIndex: Int = index
        while (true) {
            if (isEndOfText()) break
            val c: Int = chars[index]
            if (c == '\n'.code) break
            if (!isWhitespace(c)) break
            index++
        }
        return index > startIndex
    }

    fun readString(): String {
        sb.setLength(0)
        while (true) {
            if (isEndOfText() || isChar('\n'.code)) {
                throw getException("String not closed")
            }
            val c: Int = chars[index]
            if (c == '"'.code) {
                index++
                when {
                    tryReadChar('"'.code) -> sb.append('"')
                    tryReadChar('/'.code) -> if (!tryReadChar('"'.code)) {
                        throw getException("Invalid string line break")
                    } else {
                        sb.append('\n')
                    }

                    isWhitespace() || isChar('\n'.code) || isChar('#'.code) || isEndOfText() -> break
                    else -> throw getException("Invalid character after string")
                }
            } else {
                sb.appendCodePoint(c)
                index++
            }
        }
        return sb.toString()
    }

    fun readValue(): String {
        val startIndex: Int = index
        while (true) {
            if (isEndOfText()) {
                break
            }
            val c: Int = chars[index]
            if (isWhitespace(c) || c == '\n'.code || c == '#'.code) {
                break
            }
            if (c == '\"'.code) {
                throw getException("Invalid double quote after value")
            }
            index++
        }
        if (index == startIndex) {
            throw getException("Invalid value")
        }
        return String(chars, startIndex, index - startIndex)
    }

    fun getException(message: String): WsvParserException =
        getLineInfo()
            .let { (lineIndex, linePosition) ->
                WsvParserException(index, lineIndex, linePosition, message)
            }
}