package reliableKt.wsv

import reliableKt.reliableTXT.ReliableTxtEncoding
import reliableKt.reliableTXT.ReliableTxtStreamReader
import java.io.IOException


class WsvStreamReader(filePath: String) : AutoCloseable {

    private val reader = ReliableTxtStreamReader(filePath)

    val encoding: ReliableTxtEncoding
        get() = reader.encoding

    @Throws(IOException::class)
    fun readLine(): WsvLine? =
        reader.readLine()?.let { str ->
            WsvLine.parse(str)
        }

    @Throws(Exception::class)
    override fun close() {
        reader.close()
    }
}