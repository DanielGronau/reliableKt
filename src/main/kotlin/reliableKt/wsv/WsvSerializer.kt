package reliableKt.wsv

import reliableKt.wsv.WsvChar.isWhitespace


object WsvSerializer {

    private fun containsSpecialChar(value: String): Boolean =
        value.any { c ->
            c == '\n' || isWhitespace(c.code) || c == '"' || c == '#'
        }

    private fun serializeValue(sb: StringBuilder, value: String?) {
        when {
            value == null -> sb.append('-')
            value.isEmpty() -> sb.append("\"\"")
            value == "-" -> sb.append("\"-\"")
            containsSpecialChar(value) -> {
                sb.append('"')
                for (c in value) {
                    when (c) {
                        '\n' -> sb.append("\"/\"")
                        '"' -> sb.append("\"\"")
                        else -> sb.append(c)
                    }
                }
                sb.append('"')
            }

            else -> sb.append(value)
        }
    }

    private fun serializeWhitespace(
        sb: StringBuilder, whitespace: String?,
        isRequired: Boolean
    ) {
        when {
            !whitespace.isNullOrEmpty() -> sb.append(whitespace)
            isRequired -> sb.append(" ")
        }
    }

    private fun serializeValuesWithWhitespace(
        sb: StringBuilder,
        line: WsvLine
    ) {
        val values = line.values
        line.values.forEachIndexed { i, value ->
            var whitespace: String? = null
            if (i < line.whitespaces.size) {
                whitespace = line.whitespaces[i]
            }
            when (i) {
                0 -> serializeWhitespace(sb, whitespace, false)
                else -> serializeWhitespace(sb, whitespace, true)
            }
            serializeValue(sb, value)
        }
        when {
            line.whitespaces.size >= values.size + 1 -> {
                val whitespace = line.whitespaces[values.size]
                serializeWhitespace(sb, whitespace, false)
            }

            line.comment != null && values.isNotEmpty() -> sb.append(' ')
        }
    }

    private fun serializeValuesWithoutWhitespace(
        sb: StringBuilder,
        line: WsvLine
    ) {
        var isFollowingValue = false
        line.values.forEach { value ->
            when {
                isFollowingValue -> sb.append(' ')
                else -> isFollowingValue = true
            }
            serializeValue(sb, value)
        }
        if (line.comment != null && line.values.isNotEmpty()) {
            sb.append(' ')
        }
    }

    fun serializeLine(sb: StringBuilder, line: WsvLine) {
        when {
            line.whitespaces.isNotEmpty() -> serializeValuesWithWhitespace(sb, line)
            else -> serializeValuesWithoutWhitespace(sb, line)

        }
        if (line.comment != null) {
            sb.append('#')
            sb.append(line.comment)
        }
    }

    fun serializeLine(line: WsvLine): String =
        with(StringBuilder()) {
            serializeLine(this, line)
            toString()
        }

    fun serializeDocument(document: WsvDocument): String =
        with(StringBuilder()) {
            var isFirstLine = true
            for (line in document.lines) {
                when {
                    !isFirstLine -> append('\n')
                    else -> isFirstLine = false
                }
                serializeLine(this, line)
            }
            toString()
        }


    fun serializeLineNonPreserving(line: WsvLine): String =
        with(StringBuilder()) {
            serializeLine(this, line.values)
            toString()
        }

    fun serializeDocumentNonPreserving(document: WsvDocument): String =
        with(StringBuilder()) {
            var isFirstLine = true
            for (line in document.lines) {
                when {
                    !isFirstLine -> append('\n')
                    else -> isFirstLine = false
                }
                serializeLine(this, line.values)
            }
            toString()
        }

    fun serializeLine(sb: StringBuilder, line: Array<String?>) {
        line.forEachIndexed { index, value ->
            if (index > 0) {
                sb.append(' ')
            }
            serializeValue(sb, value)
        }
    }

    fun serializeLine(vararg line: String?): String =
        with(StringBuilder()) {
            serializeLine(this, arrayOf(*line))
            toString()
        }

    fun serializeDocument(lines: Array<Array<String?>>): String =
        with(StringBuilder()) {
            lines.forEachIndexed { index, line ->
                if (index > 0) {
                    append('\n')
                }
                serializeLine(this, line)
            }
            toString()
        }

}