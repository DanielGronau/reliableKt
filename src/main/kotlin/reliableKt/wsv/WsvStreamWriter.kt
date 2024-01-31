package reliableKt.wsv

import reliableKt.reliableTXT.ReliableTxtEncoding
import reliableKt.reliableTXT.ReliableTxtStreamWriter
import reliableKt.wsv.WsvSerializer.serializeLine
import java.io.IOException


class WsvStreamWriter @JvmOverloads constructor(
    filePath: String,
    encoding: ReliableTxtEncoding? = null,
    append: Boolean = false
) : AutoCloseable {
    private var sb: StringBuilder = StringBuilder()

    private var writer = ReliableTxtStreamWriter(filePath, encoding, append)

    val encoding: ReliableTxtEncoding
        get() = writer.encoding

    val appendMode: Boolean
        get() = writer.appendMode

    constructor(filePath: String, append: Boolean) : this(filePath, null, append)

    @Throws(IOException::class)
    fun writeLine(vararg values: String?) {
        writeLine(WsvLine(*values))
    }

    @Throws(IOException::class)
    fun writeLine(values: Array<String?>, whitespaces: Array<String?>, comment: String?) {
        writeLine(WsvLine(values, whitespaces, comment))
    }

    @Throws(IOException::class)
    fun writeLine(line: WsvLine) {
        sb.setLength(0)
        serializeLine(sb, line)
        writer.writeLine(sb.toString())
    }

    @Throws(IOException::class)
    fun writeLines(vararg lines: WsvLine) {
        for (line in lines) {
            writeLine(line)
        }
    }

    @Throws(IOException::class)
    fun writeLines(document: WsvDocument) {
        writeLines(*document.lines.toTypedArray())
    }

    @Throws(Exception::class)
    override fun close() {
        writer.close()
    }
}