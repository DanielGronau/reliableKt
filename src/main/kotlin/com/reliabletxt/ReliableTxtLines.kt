package com.reliabletxt

object ReliableTxtLines {
    fun join(vararg lines: CharSequence): String =
        lines.joinToString("\n")

    fun join(lines: Iterable<CharSequence>): String =
        lines.joinToString("\n")

    fun split(text: String): Array<String> =
        text.lines().toTypedArray()
}
