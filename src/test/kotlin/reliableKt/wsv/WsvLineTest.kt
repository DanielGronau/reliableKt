package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import reliableKt.wsv.WsvBasedFormat.getWhitespaces


class WsvLineTest {
    @Test
    fun setComment_LineFeedGiven_ShouldThrowException() {
        setComment_LineFeedGiven_ShouldThrowException("\n")
    }

    private fun setComment_LineFeedGiven_ShouldThrowException(comment: String) {
        assertThatThrownBy {
            val wsvLine = WsvLine()
            wsvLine.comment = comment
        }.isInstanceOf(Exception::class.java)
    }

    @Test
    fun setWhitespaces_LineFeedGiven_ShouldThrowException() {
        setWhitespaces_InvalidWhitespaceGiven_ShouldThrowException("\n")
    }

    private fun setWhitespaces_InvalidWhitespaceGiven_ShouldThrowException(vararg whitespaces: String?) {
        assertThatThrownBy {
            val wsvLine = WsvLine()
            wsvLine.whitespaces = arrayOf(*whitespaces)
        }.isInstanceOf(Exception::class.java)
    }

    @Test
    fun parse_InvalidTextGiven_ShouldThrowException() {
        parse_InvalidTextGiven_ShouldThrowException("a b c\n", "Multiple WSV lines not allowed (1, 6)")
        parse_InvalidTextGiven_ShouldThrowException("a b c \"hello world", "String not closed (1, 19)")
        parse_InvalidTextGiven_ShouldThrowException("a b c \"hello world\n", "String not closed (1, 19)")
        parse_InvalidTextGiven_ShouldThrowException("a b\"hello world\"", "Invalid double quote after value (1, 4)")
        parse_InvalidTextGiven_ShouldThrowException("\"hello world\"a b c", "Invalid character after string (1, 14)")
        parse_InvalidTextGiven_ShouldThrowException("\"Line1\"/ \"Line2\"", "Invalid string line break (1, 9)")
    }

    private fun parse_InvalidTextGiven_ShouldThrowException(line: String, expectedExceptionMessage: String) {
        assertThatThrownBy { WsvLine.parse(line) }
            .isInstanceOf(WsvParserException::class.java)
            .hasMessage(expectedExceptionMessage)
    }

    @Test
    fun failing() {
        //val nows = WsvLine(arrayOf("xy"), arrayOf(null), null)
        //System.out.println(nows)
        val ws = WsvLine(arrayOf("x y"), arrayOf(null), null)
        System.out.println(ws)
        assertThat(ws.toString()).isEqualTo("\"x y\"")
    }

    @Test
    fun parse() {
        parse_equals_toString("")
        parse_equals_toString("a")
        parse_equals_toString("a b")
        parse_equals_toString("a b c")
        parse_equals_toString(" ")
        parse_equals_toString(" a")
        parse_equals_toString(" a b")
        parse_equals_toString(" a b c")
        parse_equals_toString("  ")
        parse_equals_toString(" a ")
        parse_equals_toString(" a  b ")
        parse_equals_toString(" a  b  c ")
        parse_equals_toString("\t")
        parse_equals_toString("\ta\t")
        parse_equals_toString("\ta\tb\t")
        parse_equals_toString("\ta\tb\tc\t")
        parse_equals_toString("#c")
        parse_equals_toString("a#c")
        parse_equals_toString("a b#c")
        parse_equals_toString("a b c#c")
        parse_equals_toString(" #c")
        parse_equals_toString(" a#c")
        parse_equals_toString(" a b#c")
        parse_equals_toString(" a b c#c")
        parse_equals_toString("  #c")
        parse_equals_toString(" a #c")
        parse_equals_toString(" a  b #c")
        parse_equals_toString(" a  b  c #c")
        parse_equals_toString("\t#c")
        parse_equals_toString("\ta\t#c")
        parse_equals_toString("\ta\tb\t#c")
        parse_equals_toString("\ta\tb\tc\t#c")
        parse_equals_toString("\"Hello world\"")
        parse_equals_toString("\"Hello world\" \"Hello world\"")
        parse_equals_toString("\"Hello world\" \"Hello world\" ")
        parse_equals_toString("- -")
        parse_equals_toString("\"-\" \"-\"")
        parse_equals_toString("\"\" \"\"")
        parse_equals_toString("\"\"\"\" \"\"\"\"")
        parse_equals_toString("\"\"/\"\" \"\"/\"\"")
        parse_equals_toString("\"Line1\"/\"Line2\" \"Line1\"/\"Line2\"")
    }

    private fun parse_equals_toString(line: String) {
        assertThat(WsvLine.parse(line)).hasToString(line)
    }

    @Test
    fun parse_nonPreserving() {
        parse_nonPreserving_equals("a b c", "a b c")
        parse_nonPreserving_equals("   a   b   c  ", "a b c")
        parse_nonPreserving_equals("a b c#", "a b c")
        parse_nonPreserving_equals("a b c#Comment", "a b c")
    }

    private fun parse_nonPreserving_equals(line: String, expected: String) {
        assertThat(WsvLine.parse(line, false)).hasToString(expected)
        assertThat(WsvLine.parse(line).toString(false)).isEqualTo(expected)
    }

    @Test
    fun toString_singleValue() {
        toString_singleValue("", "\"\"")
        toString_singleValue("a", "a")
        toString_singleValue("abc", "abc")
        toString_singleValue("-", "\"-\"")
        toString_singleValue(null, "-")
        toString_singleValue("#", "\"#\"")
        toString_singleValue("abc def", "\"abc def\"")
        toString_singleValue("\"", "\"\"\"\"")
        toString_singleValue("\n", "\"\"/\"\"")
        toString_singleValue("Line1\nLine2", "\"Line1\"/\"Line2\"")
    }

    private fun toString_singleValue(value: String?, expected: String) {
        assertThat(WsvLine(value).toString()).isEqualTo(expected)
    }

    @Test
    fun test_toString() {
        val comment_null: String? = null
        val comment_empty = ""
        val comment = "c"
        val values_null: Array<String?> = arrayOf()
        val values_a = arrayOf<String?>("a")
        val values_a_b = arrayOf<String?>("a", "b")
        val values_a_b_c = arrayOf<String?>("a", "b", "c")
        val ws_null: Array<String?> = arrayOf()
        val ws_0 = arrayOf<String?>()
        val ws_1 = arrayOf<String?>("\t")
        val ws_2 = arrayOf<String?>("\t", "\t")
        val ws_3 = arrayOf<String?>("\t", "\t", "\t")
        val ws_e1 = arrayOf<String?>("")
        val ws_e2 = arrayOf<String?>("", "")
        val ws_e3 = arrayOf<String?>("", "", "")
        val ws_n1 = arrayOf<String?>(null)
        val ws_n2 = arrayOf<String?>(null, null)
        val ws_n3 = arrayOf<String?>(null, null, null)
        toString_equals(values_null, ws_null, comment_null, "")
        toString_equals(values_null, ws_0, comment_null, "")
        toString_equals(values_null, ws_1, comment_null, "\t")
        toString_equals(values_null, ws_e1, comment_null, "")
        toString_equals(values_null, ws_n1, comment_null, "")
        toString_equals(values_null, ws_null, comment, "#c")
        toString_equals(values_null, ws_0, comment, "#c")
        toString_equals(values_null, ws_1, comment, "\t#c")
        toString_equals(values_null, ws_e1, comment, "#c")
        toString_equals(values_null, ws_n1, comment, "#c")
        toString_equals(values_null, ws_null, comment_empty, "#")
        toString_equals(values_a, ws_null, comment_null, "a")
        toString_equals(values_a, ws_0, comment_null, "a")
        toString_equals(values_a, ws_1, comment_null, "\ta")
        toString_equals(values_a, ws_2, comment_null, "\ta\t")
        toString_equals(values_a, ws_3, comment_null, "\ta\t")
        toString_equals(values_a, ws_e1, comment_null, "a")
        toString_equals(values_a, ws_e2, comment_null, "a")
        toString_equals(values_a, ws_n1, comment_null, "a")
        toString_equals(values_a, ws_n2, comment_null, "a")
        toString_equals(values_a, ws_null, comment, "a #c")
        toString_equals(values_a, ws_0, comment, "a #c")
        toString_equals(values_a, ws_1, comment, "\ta #c")
        toString_equals(values_a, ws_2, comment, "\ta\t#c")
        toString_equals(values_a, ws_3, comment, "\ta\t#c")
        toString_equals(values_a, ws_e1, comment, "a #c")
        toString_equals(values_a, ws_e2, comment, "a#c")
        toString_equals(values_a, ws_n1, comment, "a #c")
        toString_equals(values_a, ws_n2, comment, "a#c")
        toString_equals(values_a_b, ws_null, comment_null, "a b")
        toString_equals(values_a_b, ws_0, comment_null, "a b")
        toString_equals(values_a_b, ws_1, comment_null, "\ta b")
        toString_equals(values_a_b, ws_2, comment_null, "\ta\tb")
        toString_equals(values_a_b, ws_3, comment_null, "\ta\tb\t")
        toString_equals(values_a_b, ws_e1, comment_null, "a b")
        toString_equals(values_a_b, ws_e2, comment_null, "a b")
        toString_equals(values_a_b, ws_e3, comment_null, "a b")
        toString_equals(values_a_b, ws_n1, comment_null, "a b")
        toString_equals(values_a_b, ws_n2, comment_null, "a b")
        toString_equals(values_a_b, ws_n3, comment_null, "a b")
        toString_equals(values_a_b, ws_null, comment, "a b #c")
        toString_equals(values_a_b, ws_0, comment, "a b #c")
        toString_equals(values_a_b, ws_1, comment, "\ta b #c")
        toString_equals(values_a_b, ws_2, comment, "\ta\tb #c")
        toString_equals(values_a_b, ws_3, comment, "\ta\tb\t#c")
        toString_equals(values_a_b, ws_e1, comment, "a b #c")
        toString_equals(values_a_b, ws_e2, comment, "a b #c")
        toString_equals(values_a_b, ws_e3, comment, "a b#c")
        toString_equals(values_a_b, ws_n1, comment, "a b #c")
        toString_equals(values_a_b, ws_n2, comment, "a b #c")
        toString_equals(values_a_b, ws_n3, comment, "a b#c")
        toString_equals(values_a_b_c, ws_null, comment_null, "a b c")
        toString_equals(values_a_b_c, ws_0, comment_null, "a b c")
        toString_equals(values_a_b_c, ws_null, comment, "a b c #c")
    }

    private fun toString_equals(
        values: Array<String?>,
        whitespaces: Array<String?>,
        comment: String?,
        expected: String
    ) {
        assertThat(WsvLine(values, whitespaces, comment)).hasToString(expected)
    }

    @Test
    fun hasValues() {
        checkHasValues(null, false)
        checkHasValues(arrayOf(), false)
        checkHasValues(arrayOf("Value"), true)
    }

    private fun checkHasValues(values: Array<String>?, expectedResult: Boolean) {
        assertThat(WsvLine(*values.orEmpty()).hasValues()).isEqualTo(expectedResult)
    }

    @Test
    fun setValues() {
        val line = WsvLine()
        line.values = arrayOf("Value1", "Value2")
        assertThat(line.values).containsExactly("Value1", "Value2")
    }

    @Test
    fun whitespaces() {
        val line = WsvLine.parse("a  b  c #dfd")
        val clonedWhitespaces = line.whitespaces
        assertThat(clonedWhitespaces).containsExactly(null, "  ", "  ", " ")
        val internalWhitespaces = getWhitespaces(line)
        assertThat(clonedWhitespaces).isNotSameAs(internalWhitespaces)
    }

    @Test
    fun comment() {
        assertThat(WsvLine.parse("a  b  c #comment").comment).isEqualTo("comment")
    }

    @Test
    fun parseAsArray() {
        assertThat(WsvLine.parseAsArray("a  b  c #comment")).containsExactly("a", "b", "c")
    }

    @Test
    fun whitespaces_Null() {
        val line = WsvLine()
        assertThat(line.whitespaces).isEmpty()
    }
}