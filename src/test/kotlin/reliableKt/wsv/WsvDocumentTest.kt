package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import reliableKt.reliableTXT.ReliableTxtDocument
import reliableKt.reliableTXT.ReliableTxtEncoding
import java.nio.file.Files
import java.nio.file.Path


class WsvDocumentTest {
    @Test
    fun parse() {
        checkParse("a  b  c  #comment\nd  e  f")
        checkParse("\" a \" \" b \" \" c \"#comment\n\" d \"\n\" e \"")
    }

    private fun checkParse(text: String) {
        assertThat(WsvDocument.parse(text).toString()).isEqualTo(text)
    }

    @Test
    fun parse_NonPreserving() {
        parse_NonPreserving("a  b  c  #comment\nd  e  f", "a b c\nd e f")
    }

    private fun parse_NonPreserving(text: String, expected: String) {
        assertThat(WsvDocument.parse(text, false).toString()).isEqualTo(expected)
    }

    @Test
    fun parse_InvalidTextGiven_ShouldThrowException() {
        parse_InvalidTextGiven_ShouldThrowException("a b c \"hello world", "String not closed (1, 19)")
        parse_InvalidTextGiven_ShouldThrowException("a b c \"hello world\n", "String not closed (1, 19)")
        parse_InvalidTextGiven_ShouldThrowException("a b\"hello world\"", "Invalid double quote after value (1, 4)")
        parse_InvalidTextGiven_ShouldThrowException("\"hello world\"a b c", "Invalid character after string (1, 14)")
        parse_InvalidTextGiven_ShouldThrowException("\"Line1\"/ \"Line2\"", "Invalid string line break (1, 9)")
        parse_InvalidTextGiven_ShouldThrowException("Line1\na b c \"hello world", "String not closed (2, 19)")
        parse_InvalidTextGiven_ShouldThrowException("Line1\na b c \"hello world\n", "String not closed (2, 19)")
        parse_InvalidTextGiven_ShouldThrowException(
            "Line1\na b\"hello world\"",
            "Invalid double quote after value (2, 4)"
        )
        parse_InvalidTextGiven_ShouldThrowException(
            "Line1\n\"hello world\"a b c",
            "Invalid character after string (2, 14)"
        )
        parse_InvalidTextGiven_ShouldThrowException("Line1\n\"Line1\"/ \"Line2\"", "Invalid string line break (2, 9)")
    }

    private fun parse_InvalidTextGiven_ShouldThrowException(text: String, expectedExceptionMessage: String) {
        assertThatThrownBy { WsvDocument.parse(text) }
            .isInstanceOf(WsvParserException::class.java)
            .hasMessage(expectedExceptionMessage)
    }

    @Test
    fun encoding() {
        checkGetEncoding(WsvDocument(), ReliableTxtEncoding.UTF_8)
        checkGetEncoding(WsvDocument(ReliableTxtEncoding.UTF_16), ReliableTxtEncoding.UTF_16)
        checkGetEncoding(WsvDocument(ReliableTxtEncoding.UTF_16_REVERSE), ReliableTxtEncoding.UTF_16_REVERSE)
        checkGetEncoding(WsvDocument(ReliableTxtEncoding.UTF_32), ReliableTxtEncoding.UTF_32)
    }

    private fun checkGetEncoding(document: WsvDocument, expectedEncoding: ReliableTxtEncoding) {
        assertThat(document.encoding).isEqualTo(expectedEncoding)
    }

    @Test
    fun addLine() {
        val document = WsvDocument()
        document.addLine("V11", "V12")
        document.addLine(arrayOf("V21", "V22"), arrayOf("  ", "   ", ""), "comment")
        assertThat(document.toString()).isEqualTo("V11 V12\n  V21   V22#comment")
    }

    @Test
    fun line() {
        val document = WsvDocument.parse("V11 V12\n  V21   V22#comment")
        val line = document.getLine(1)
        assertThat(line.toString()).isEqualTo("  V21   V22#comment")
    }

    @Test
    fun toArray() {
        val document = WsvDocument.parse("V11 V12\n  V21   V22#comment")
        val jaggedArray = document.toArray()
        assertThat(jaggedArray[0]).containsExactly("V11", "V12")
        assertThat(jaggedArray[1]).containsExactly("V21", "V22")
        assertThat(jaggedArray.size).isEqualTo(2)
    }

    @Test
    fun toString_NonPreserving() {
        val document = WsvDocument.parse("V11 V12\n  V21   V22#comment")
        assertThat(document.toString(false)).isEqualTo("V11 V12\nV21 V22")
    }

    @Test
    fun save() {
        val text = "V11 V12\n  V21   V22#comment"
        checkSave(text, ReliableTxtEncoding.UTF_8)
        checkSave(text, ReliableTxtEncoding.UTF_16)
        checkSave(text, ReliableTxtEncoding.UTF_16_REVERSE)
        checkSave(text, ReliableTxtEncoding.UTF_32)
    }

    private fun checkSave(text: String, encoding: ReliableTxtEncoding) {
        val document = WsvDocument.parse(text)
        document.encoding = encoding
        val filePath = "Test.wsv"
        document.save(filePath)
        checkLoad(filePath, text, encoding)
    }

    private fun checkLoad(filePath: String, expectedText: String, expectedEncoding: ReliableTxtEncoding) {
        val (text, encoding) = ReliableTxtDocument.load(filePath)
        assertThat(encoding).isEqualTo(expectedEncoding)
        assertThat(text).isEqualTo(expectedText)
    }

    @Test
    fun load() {
        val text = "V11 V12\n  V21   V22#comment"
        checkLoad(text, ReliableTxtEncoding.UTF_8)
        checkLoad(text, ReliableTxtEncoding.UTF_16)
        checkLoad(text, ReliableTxtEncoding.UTF_16_REVERSE)
        checkLoad(text, ReliableTxtEncoding.UTF_32)
    }

    private fun checkLoad(text: String, encoding: ReliableTxtEncoding) {
        val filePath = "Test.wsv"
        ReliableTxtDocument.save(text, encoding, filePath)
        val document = WsvDocument.load(filePath)
        assertThat(document.toString()).isEqualTo(text)
        assertThat(document.encoding).isEqualTo(encoding)
    }

    @Test
    fun parseAsJaggedArray() {
        val jaggedArray = WsvDocument.parseAsJaggedArray("V11 V12\n  V21   V22#comment")
        assertThat(jaggedArray[0]).containsExactly("V11", "V12")
        assertThat(jaggedArray[1]).containsExactly("V21", "V22")
        assertThat(jaggedArray.size).isEqualTo(2)
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun cleanup() {
            Files.deleteIfExists(Path.of("Test.wsv"))
        }
    }
}