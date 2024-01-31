package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import kotlin.test.Test

class WsvParserTest {
    @Test
    fun parseDocumentAsJaggedArray_InvalidTextGive_ShouldThrowExceptions() {
        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "a b c \"hello world",
            "String not closed (1, 19)"
        )
        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "a b c \"hello world\n",
            "String not closed (1, 19)"
        )

        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "a b\"hello world\"",
            "Invalid double quote after value (1, 4)"
        )

        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "\"hello world\"a b c",
            "Invalid character after string (1, 14)"
        )

        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "\"Line1\"/ \"Line2\"",
            "Invalid string line break (1, 9)"
        )

        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "Line1\na b c \"hello world",
            "String not closed (2, 19)"
        )
        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "Line1\na b c \"hello world\n",
            "String not closed (2, 19)"
        )

        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "Line1\na b\"hello world\"",
            "Invalid double quote after value (2, 4)"
        )

        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "Line1\n\"hello world\"a b c",
            "Invalid character after string (2, 14)"
        )

        parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
            "Line1\n\"Line1\"/ \"Line2\"",
            "Invalid string line break (2, 9)"
        )
    }

    private fun parseDocumentAsJaggedArray_InvalidTextGiven_ShouldThrowException(
        text: String,
        expectedExceptionMessage: String
    ) {
        assertThatThrownBy { WsvParser.parseDocumentAsJaggedArray(text) }
            .isInstanceOf(WsvParserException::class.java)
            .hasMessage(expectedExceptionMessage)
    }

    @Test
    fun parseDocumentAsJaggedArray() {
        val lineValues_empty = arrayOf<String>()
        val lineValues_a = arrayOf("a")
        val lineValues_a_b = arrayOf("a", "b")
        val lineValues_a_b_c = arrayOf("a", "b", "c")

        parseDocumentAsJaggedArray_equals("", lineValues_empty)
        parseDocumentAsJaggedArray_equals("  ", lineValues_empty)
        parseDocumentAsJaggedArray_equals("\t \t", lineValues_empty)
        parseDocumentAsJaggedArray_equals("#", lineValues_empty)
        parseDocumentAsJaggedArray_equals("#c", lineValues_empty)
        parseDocumentAsJaggedArray_equals(" #", lineValues_empty)
        parseDocumentAsJaggedArray_equals(" #c", lineValues_empty)
        parseDocumentAsJaggedArray_equals("\t#c", lineValues_empty)
        parseDocumentAsJaggedArray_equals(
            "\u0009\u000B\u000C\u000C\u0020\u0085\u00A0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u2028\u2029\u202F\u205F\u3000",
            lineValues_empty
        )

        parseDocumentAsJaggedArray_equals("a", lineValues_a)
        parseDocumentAsJaggedArray_equals(" a", lineValues_a)
        parseDocumentAsJaggedArray_equals("a ", lineValues_a)
        parseDocumentAsJaggedArray_equals(" a ", lineValues_a)
        parseDocumentAsJaggedArray_equals("a#", lineValues_a)
        parseDocumentAsJaggedArray_equals("a#c", lineValues_a)
        parseDocumentAsJaggedArray_equals("a #c", lineValues_a)

        parseDocumentAsJaggedArray_equals("a b", lineValues_a_b)
        parseDocumentAsJaggedArray_equals(" a b", lineValues_a_b)
        parseDocumentAsJaggedArray_equals("a b ", lineValues_a_b)
        parseDocumentAsJaggedArray_equals(" a b ", lineValues_a_b)
        parseDocumentAsJaggedArray_equals("  a   b  ", lineValues_a_b)
        parseDocumentAsJaggedArray_equals("a b#", lineValues_a_b)
        parseDocumentAsJaggedArray_equals(" a b#", lineValues_a_b)
        parseDocumentAsJaggedArray_equals("a b #", lineValues_a_b)
        parseDocumentAsJaggedArray_equals(" a b #", lineValues_a_b)
        parseDocumentAsJaggedArray_equals("  a   b  #", lineValues_a_b)

        parseDocumentAsJaggedArray_equals("a b c", lineValues_a_b_c)

        parseDocumentAsJaggedArray_equals_serialized("\"Hello world\"")
        parseDocumentAsJaggedArray_equals_serialized("\"Hello world\" \"Hello world\"")
        parseDocumentAsJaggedArray_equals_serialized("- -")
        parseDocumentAsJaggedArray_equals_serialized("\"-\" \"-\"")
        parseDocumentAsJaggedArray_equals_serialized("\"\" \"\"")
        parseDocumentAsJaggedArray_equals_serialized("\"\"\"\" \"\"\"\"")
        parseDocumentAsJaggedArray_equals_serialized("\"\"/\"\" \"\"/\"\"")
        parseDocumentAsJaggedArray_equals_serialized("\"Line1\"/\"Line2\" \"Line1\"/\"Line2\"")
    }

    private fun parseDocumentAsJaggedArray_equals(text: String, expectedLineValues: Array<String>) {
        val actualLines = WsvParser.parseDocumentAsJaggedArray(text)
        assertThat(actualLines.size).isEqualTo(1)
        assertThat(actualLines[0]).isEqualTo(expectedLineValues)
    }

    private fun parseDocumentAsJaggedArray_equals_serialized(textAndExpected: String) {
        assertThat(
            WsvSerializer.serializeDocument(
                WsvParser.parseDocumentAsJaggedArray(textAndExpected)
            )
        ).isEqualTo(textAndExpected)
    }

    @Test
    fun parseLineAsArray_MultiLineGiven_ShouldThrowException() {
        assertThatThrownBy { WsvParser.parseLineAsArray("V11 V12\nV21") }
            .isInstanceOf(WsvParserException::class.java)
            .hasMessage("Multiple WSV lines not allowed (1, 8)")
    }
}