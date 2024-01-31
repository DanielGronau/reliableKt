package reliableKt.wsv

import reliableKt.reliableTXT.ReliableTxtCharIterator
import reliableKt.wsv.WsvChar.isWhitespace


class WsvTokenizer(text: String) : ReliableTxtCharIterator(text) {

    private val tokens = ArrayList<Int>()

    private var lastTokenEndIndex = 0

    private fun addToken(tokenType: Int, length: Int) {
        tokens.add(tokenType)
        tokens.add(length)
        lastTokenEndIndex += length
    }

    private fun readComment() {
        val startIndex = index
        while (!isChar('\n'.code) && !isEndOfText()) {
            index++
        }
        addToken(TokenTypeComment, index - startIndex)
    }

    private fun readWhitespace(): Boolean {
        val startIndex = index
        while (true) {
            if (isEndOfText()) break
            val c = chars[index]
            if (c == '\n'.code) break
            if (!isWhitespace(c)) break
            index++
        }
        if (index == startIndex) return false
        addToken(TokenTypeWhitespace, index - startIndex)
        return true
    }

    private val isWhitespace: Boolean
        get() = if (isEndOfText()) false else isWhitespace(chars[index])

    private fun readString(): Boolean {
        val stringStartIndex = index - 1
        val tokenStartCount = tokens.size
        addToken(TokenTypeStringStart, 1)
        var partLength = 0
        while (true) {
            if (isEndOfText() || isChar('\n'.code)) {
                while (tokens.size > tokenStartCount) {
                    tokens.removeAt(tokens.size - 1)
                }
                lastTokenEndIndex = stringStartIndex
                return false
            }
            val c = chars[index]
            if (c == '"'.code) {
                if (partLength > 0) {
                    addToken(TokenTypeStringText, partLength)
                    partLength = 0
                }
                index++
                if (tryReadChar('"'.code)) {
                    addToken(TokenTypeStringEscapedDoubleQuote, 2)
                } else if (tryReadChar('/'.code)) {
                    if (!tryReadChar('"'.code)) {
                        addToken(TokenTypeStringEnd, 1)
                        return false
                    }
                    addToken(TokenTypeStringStart, 1)
                    addToken(TokenTypeStringLineBreak, 1)
                    addToken(TokenTypeStringEnd, 1)
                } else if (isWhitespace || isChar('\n'.code) || isChar('#'.code) || isEndOfText()) {
                    break
                } else {
                    addToken(TokenTypeStringEnd, 1)
                    index++
                    return false
                }
            } else {
                partLength++
                index++
            }
        }
        addToken(TokenTypeStringEnd, 1)
        return true
    }

    private fun readValue(): Boolean {
        val startIndex = index
        var lastChar = 0
        var result = true
        while (true) {
            if (isEndOfText()) {
                break
            }
            val c = chars[index]
            if (isWhitespace(c) || c == '\n'.code || c == '#'.code) {
                break
            }
            if (c == '\"'.code) {
                result = false
                break
            }
            lastChar = c
            index++
        }
        if (index == startIndex) {
            return false
        }
        val length = index - startIndex
        when {
            length == 1 && lastChar == '-'.code -> addToken(TokenTypeNull, 1)
            else -> addToken(TokenTypeValue, length)
        }
        if (!result) {
            index++
        }
        return result
    }

    private fun readLine(): Boolean {
        readWhitespace()
        while (!isChar('\n'.code) && !isEndOfText()) {
            if (isChar('#'.code)) {
                readComment()
                break
            } else if (tryReadChar('"'.code)) {
                if (!readString()) {
                    return false
                }
            } else {
                if (!readValue()) {
                    return false
                }
            }
            if (!readWhitespace()) {
                break
            }
        }
        return true
    }

    fun tokenize(): IntArray {
        while (true) {
            if (!readLine()) {
                break
            }
            if (tryReadChar('\n'.code)) {
                addToken(TokenTypeLineBreak, 1)
            } else {
                break
            }
        }
        when (lastTokenEndIndex) {
            chars.size -> addToken(TokenTypeEndOfText, 0)
            else -> addToken(TokenTypeError, index - lastTokenEndIndex)
        }
        return tokens.stream().mapToInt { i: Int? -> i!! }.toArray()
    }

    companion object {
        const val TokenTypeLineBreak = 0
        const val TokenTypeWhitespace = 1
        const val TokenTypeComment = 2
        const val TokenTypeNull = 3
        const val TokenTypeValue = 4
        const val TokenTypeStringStart = 5
        const val TokenTypeStringEnd = 6
        const val TokenTypeStringText = 7
        const val TokenTypeStringEscapedDoubleQuote = 8
        const val TokenTypeStringLineBreak = 9
        const val TokenTypeEndOfText = 10
        const val TokenTypeError = 11

        fun tokenize(text: String): IntArray = WsvTokenizer(text).tokenize()
    }
}