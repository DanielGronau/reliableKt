package com.stenway.reliabletxt

import java.io.BufferedWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class ReliableTxtStreamWriter @JvmOverloads constructor(
    filePath: String,
    encoding: ReliableTxtEncoding? = null,
    append: Boolean = false
) : AutoCloseable {
    private val encoding: ReliableTxtEncoding
    private var writer: BufferedWriter
    private var isFirstLine: Boolean = true
    private val appendMode: Boolean

    constructor(filePath: String, append: Boolean) : this(filePath, null, append)

    init {
        var _encoding = encoding
        if (_encoding == null) {
            _encoding = ReliableTxtEncoding.UTF_8
        }
        val path = Paths.get(filePath)
        var options = arrayOf<OpenOption>()
        if (append && Files.exists(path) && Files.size(path) > 0) {
            _encoding = ReliableTxtDecoder.getEncodingFromFile(filePath)
            isFirstLine = false
            options = arrayOf(StandardOpenOption.APPEND)
        }
        appendMode = !isFirstLine
        this.encoding = _encoding
        val charset = this.encoding.charset
        writer = Files.newBufferedWriter(path, charset, *options)
        if (isFirstLine) {
            writer.write(0xFEFF)
        }
    }

    @Throws(IOException::class)
    fun writeLine(line: String) {
        when {
            !isFirstLine -> writer.append('\n')
            else -> isFirstLine = false
        }
        writer.write(line)
    }

    @Throws(IOException::class)
    fun writeLines(vararg lines: String) {
        lines.forEach { writeLine(it) }
    }

    @Throws(Exception::class)
    override fun close() {
        writer.close()
    }
}
