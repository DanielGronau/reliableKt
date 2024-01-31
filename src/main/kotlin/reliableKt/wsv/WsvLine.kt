package reliableKt.wsv


class WsvLine(
    var values: Array<String?> = arrayOf(),
    whitespaces: Array<String?> = arrayOf(),
    comment: String? = null
) {

    var whitespaces: Array<String?> = whitespaces
        set(value) {
            validateWhitespaces(*value)
            field = value
        }
        get() = field.clone()

    var comment: String? = comment
        set(value) {
            validateComment(value)
            field = value
        }

    constructor(vararg values: String?) : this(arrayOf(*values))

    fun hasValues(): Boolean = values.isNotEmpty()

    fun set(values: Array<String?>, whitespaces: Array<String?>, comment: String?) {
        this.values = values
        this.whitespaces = whitespaces
        this.comment = comment
    }

    override fun toString(): String {
        return toString(true)
    }

    fun toString(preserveWhitespaceAndComment: Boolean): String = when {
        preserveWhitespaceAndComment -> WsvSerializer.serializeLine(this)
        else -> WsvSerializer.serializeLineNonPreserving(this)
    }

    companion object {

        private fun validateWhitespaces(vararg whitespaces: String?) {
            for (whitespace in whitespaces) {
                require(whitespace.isNullOrEmpty() || WsvString.isWhitespace(whitespace)) {
                    "Whitespace value contains non whitespace character or line feed" }
            }
        }

        private fun validateComment(comment: String?) {
            require(comment == null || comment.indexOf('\n') < 0) {
                "Line feed in comment is not allowed"
            }
        }

        @JvmOverloads
        fun parse(content: String?, preserveWhitespaceAndComment: Boolean = true): WsvLine = when {
            preserveWhitespaceAndComment -> WsvParser.parseLine(content)
            else -> WsvParser.parseLineNonPreserving(content)
        }

        fun parseAsArray(content: String?): Array<String?> = WsvParser.parseLineAsArray(content)
    }
}