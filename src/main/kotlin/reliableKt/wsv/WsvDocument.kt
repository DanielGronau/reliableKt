package reliableKt.wsv

import reliableKt.reliableTXT.ReliableTxtDocument
import reliableKt.reliableTXT.ReliableTxtEncoding
import reliableKt.wsv.WsvParser.parseDocument
import reliableKt.wsv.WsvParser.parseDocumentAsJaggedArray
import reliableKt.wsv.WsvParser.parseDocumentNonPreserving
import reliableKt.wsv.WsvSerializer.serializeDocument
import reliableKt.wsv.WsvSerializer.serializeDocumentNonPreserving
import java.io.IOException
import java.util.*


class WsvDocument @JvmOverloads constructor(var encoding: ReliableTxtEncoding = ReliableTxtEncoding.UTF_8) {

    val lines = ArrayList<WsvLine>()

    fun addLine(vararg values: String?) {
        addLine(WsvLine(*values))
    }

    fun addLine(values: Array<String?>, whitespaces: Array<String?>, comment: String?) {
        addLine(WsvLine(values, whitespaces, comment))
    }

    fun addLine(line: WsvLine) {
        lines.add(line)
    }

    fun getLine(index: Int): WsvLine =
        lines[index]

    fun toArray(): Array<Array<String?>> =
        Array(lines.size) { i ->
            lines[i].values!!
        }

    override fun toString(): String = toString(true)

    fun toString(preserveWhitespaceAndComments: Boolean): String = when {
        preserveWhitespaceAndComments -> serializeDocument(this)
        else -> serializeDocumentNonPreserving(this)
    }

    @Throws(IOException::class)
    fun save(filePath: String) {
        ReliableTxtDocument.save(toString(), encoding, filePath)
    }

    companion object {
        @JvmOverloads
        @Throws(IOException::class)
        fun load(filePath: String, preserveWhitespaceAndComments: Boolean = true): WsvDocument {
            val (text, encoding) = ReliableTxtDocument.load(filePath)
            return parse(text, preserveWhitespaceAndComments).also { document ->
                document.encoding = encoding
            }
        }

        @JvmOverloads
        fun parse(content: String?, preserveWhitespaceAndComments: Boolean = true): WsvDocument =
            when {
                preserveWhitespaceAndComments -> parseDocument(content)
                else -> parseDocumentNonPreserving(content)
            }

        fun parseAsJaggedArray(content: String?): Array<Array<String?>> =
            parseDocumentAsJaggedArray(content)
    }
}