package reliableKt.wsv


internal object WsvParser {
    private const val MULTIPLE_WSV_LINES_NOT_ALLOWED = "Multiple WSV lines not allowed"
    private const val UNEXPECTED_PARSER_ERROR = "Unexpected parser error"

    private fun parseLine(
        iterator: WsvCharIterator,
        values: ArrayList<String?>,
        whitespaces: ArrayList<String?>
    ): WsvLine {
        values.clear()
        whitespaces.clear()
        var whitespace = iterator.readWhitespaceOrNull()
        whitespaces.add(whitespace)
        while (!iterator.isChar('\n'.code) && !iterator.isEndOfText()) {
            var value: String?
            when {
                iterator.isChar('#'.code) ->  break
                iterator.tryReadChar('"'.code) -> value = iterator.readString()
                else -> {
                    value = iterator.readValue()
                    if (value == "-") {
                        value = null
                    }
                }
            }
            values.add(value)
            whitespace = iterator.readWhitespaceOrNull()
            if (whitespace == null) {
                break
            }
            whitespaces.add(whitespace)
        }
        var comment: String? = null
        if (iterator.tryReadChar('#'.code)) {
            comment = iterator.readCommentText()
            if (whitespace == null) {
                whitespaces.add(null)
            }
        }
        val valueArray = arrayOfNulls<String>(values.size)
        val whitespaceArray = arrayOfNulls<String>(whitespaces.size)
        values.toArray(valueArray)
        whitespaces.toArray(whitespaceArray)
        val newLine = WsvLine()
        newLine.set(valueArray, whitespaceArray, comment)
        return newLine
    }

    fun parseLine(content: String?): WsvLine {
        val iterator = WsvCharIterator(content)
        val values = ArrayList<String?>()
        val whitespaces = ArrayList<String?>()
        val newLine = parseLine(iterator, values, whitespaces)
        when {
            iterator.isChar('\n'.code) -> throw iterator.getException(MULTIPLE_WSV_LINES_NOT_ALLOWED)
            !iterator.isEndOfText() -> throw iterator.getException(UNEXPECTED_PARSER_ERROR)
        }
        return newLine
    }

    fun parseDocument(content: String?): WsvDocument {
        val document = WsvDocument()
        val iterator = WsvCharIterator(content)
        val values = ArrayList<String?>()
        val whitespaces = ArrayList<String?>()
        while (true) {
            val newLine = parseLine(iterator, values, whitespaces)
            document.addLine(newLine)
            when {
                iterator.isEndOfText() -> break
                !iterator.tryReadChar('\n'.code) -> throw iterator.getException(UNEXPECTED_PARSER_ERROR)
            }
        }
        if (!iterator.isEndOfText()) {
            throw iterator.getException(UNEXPECTED_PARSER_ERROR)
        }
        return document
    }

    fun parseLineNonPreserving(content: String?): WsvLine {
        val values = parseLineAsArray(content)
        return WsvLine(*values)
    }

    fun parseDocumentNonPreserving(content: String?): WsvDocument {
        val document = WsvDocument()
        val iterator = WsvCharIterator(content)
        val values = ArrayList<String?>()
        while (true) {
            val lineValues = parseLineAsArray(iterator, values)
            val newLine = WsvLine(*lineValues)
            document.addLine(newLine)
            when {
                iterator.isEndOfText() -> break
                !iterator.tryReadChar('\n'.code) -> throw iterator.getException(UNEXPECTED_PARSER_ERROR)
            }
        }
        if (!iterator.isEndOfText()) {
            throw iterator.getException(UNEXPECTED_PARSER_ERROR)
        }
        return document
    }

    fun parseDocumentAsJaggedArray(content: String?): Array<Array<String?>> {
        val iterator = WsvCharIterator(content)
        val values = ArrayList<String?>()
        val lines = ArrayList<Array<String?>>()
        while (true) {
            val newLine = parseLineAsArray(iterator, values)
            lines.add(newLine)
            when {
                iterator.isEndOfText() -> break
                !iterator.tryReadChar('\n'.code) -> throw iterator.getException(UNEXPECTED_PARSER_ERROR)
            }
        }
        if (!iterator.isEndOfText()) {
            throw iterator.getException(UNEXPECTED_PARSER_ERROR)
        }
        return lines.toTypedArray()
    }

    fun parseLineAsArray(content: String?): Array<String?> {
        val iterator = WsvCharIterator(content)
        val values = ArrayList<String?>()
        val result = parseLineAsArray(iterator, values)
        when {
            iterator.isChar('\n'.code) -> throw iterator.getException(MULTIPLE_WSV_LINES_NOT_ALLOWED)
            !iterator.isEndOfText() -> throw iterator.getException(UNEXPECTED_PARSER_ERROR)
        }
        return result
    }

    private fun parseLineAsArray(iterator: WsvCharIterator, values: ArrayList<String?>): Array<String?> {
        values.clear()
        iterator.skipWhitespace()
        while (!iterator.isChar('\n'.code) && !iterator.isEndOfText()) {
            var value: String?
            when {
                iterator.isChar('#'.code) -> break
                iterator.tryReadChar('"'.code) -> value = iterator.readString()
                else -> {
                    value = iterator.readValue()
                    if (value == "-") {
                        value = null
                    }
                }
            }
            values.add(value)
            if (!iterator.skipWhitespace()) {
                break
            }
        }
        if (iterator.tryReadChar('#'.code)) {
            iterator.skipCommentText()
        }
        return values.toTypedArray()
    }
}