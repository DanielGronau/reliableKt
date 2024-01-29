package reliableKt.reliableTXT

import java.io.BufferedReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class ReliableTxtStreamReader(filePath: String) : AutoCloseable {
    val encoding = ReliableTxtDecoder.getEncodingFromFile(filePath)
    private val sb: StringBuilder = StringBuilder()
    private var endReached = false

    private val reader: BufferedReader = Files.newBufferedReader(
        Paths.get(filePath),
        encoding.charset
    )

    init {
        if (encoding !== ReliableTxtEncoding.UTF_32) {
            reader.read()
        }
    }

    @Throws(IOException::class)
    fun readLine(): String? {
        if (endReached) {
            return null
        }
        sb.setLength(0)
        while (true) {
            val c = reader.read()
            when {
                c == '\n'.code -> break
                c < 0 -> {
                    endReached = true
                    break
                }
            }
            sb.append(c.toChar())
        }
        return sb.toString()
    }

    @Throws(Exception::class)
    override fun close() {
        reader.close()
    }
}
