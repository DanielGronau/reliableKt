package reliableKt.wsv

import org.assertj.core.api.Assertions.assertThat
import reliableKt.wsv.WsvSerializer.serializeLine
import kotlin.test.Test

class WsvSerializerTest {
    @Test
    fun serializeDocument() {
        serializeDocument_JaggedArrayGiven("  a  b  c  #c", "a b c")
        serializeDocument_JaggedArrayGiven("  a  b  c  #c\n", "a b c\n")
        serializeDocument_JaggedArrayGiven("  a  b  c  #c\nd", "a b c\nd")
    }

    private fun serializeDocument_JaggedArrayGiven(content: String, expectedResult: String) {
        assertThat(WsvSerializer.serializeDocument(WsvParser.parseDocumentAsJaggedArray(content)))
            .isEqualTo(expectedResult)
    }

    @Test
    fun serializeLine_StringsGiven() {
        assertThat(serializeLine("V11", "V12")).isEqualTo("V11 V12")
    }

    @Test
    fun serializeLine() {
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf("V11", "V12", "V13"),
                    arrayOf(" "),
                    "Comment"
                )
            )
        ).isEqualTo(" V11 V12 V13 #Comment")
        assertThat(serializeLine(WsvLine(arrayOf("V11", "V12", "V13"), arrayOf(" "), null)))
            .isEqualTo(" V11 V12 V13")
        assertThat(
            serializeLine(WsvLine(arrayOf("V11", "V12"), arrayOf(" "), "Comment"))
        ).isEqualTo(" V11 V12 #Comment")
        assertThat(
            serializeLine(WsvLine(arrayOf("V11", "V12"), arrayOf(" "), null))
        ).isEqualTo(" V11 V12")
        assertThat(
            serializeLine(WsvLine(arrayOf("V11", "V12"), arrayOf(" ", " "), "Comment"))
        ).isEqualTo(" V11 V12 #Comment")
        assertThat(
            serializeLine(WsvLine(arrayOf("V11", "V12"), arrayOf(" ", " "), null))
        ).isEqualTo(" V11 V12")
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf("V11", "V12"),
                    arrayOf(" ", " ", " "),
                    "Comment"
                )
            )
        ).isEqualTo(" V11 V12 #Comment")
        assertThat(
            serializeLine(WsvLine(arrayOf("V11", "V12"), arrayOf(" ", " ", " "), null))
        ).isEqualTo(" V11 V12 ")
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf("V11", "V12"),
                    arrayOf(" ", " ", " ", " "),
                    "Comment"
                )
            )
        ).isEqualTo(" V11 V12 #Comment")
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf("V11", "V12"),
                    arrayOf(" ", " ", " ", " "),
                    null
                )
            )
        ).isEqualTo(" V11 V12 ")
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf("V11", "V12"),
                    arrayOf(" ", " ", " ", " ", " "),
                    "Comment"
                )
            )
        ).isEqualTo(" V11 V12 #Comment")
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf("V11", "V12"),
                    arrayOf(" ", " ", " ", " ", " "),
                    null
                )
            )
        ).isEqualTo(" V11 V12 ")
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf(),
                    arrayOf(" ", " ", " ", " ", " "),
                    "Comment"
                )
            )
        ).isEqualTo(" #Comment")
        assertThat(
            serializeLine(WsvLine(arrayOf(), arrayOf(" ", " ", " ", " ", " "), null))
        ).isEqualTo(" ")
        assertThat(serializeLine(WsvLine(arrayOf(), arrayOf(), "Comment"))).isEqualTo("#Comment")
        assertThat(serializeLine(WsvLine(arrayOf(), arrayOf(), null))).isEqualTo("")

        assertThat(
            serializeLine(WsvLine(arrayOf("V11", "V12", "V13"), arrayOf(), "Comment"))
        ).isEqualTo("V11 V12 V13 #Comment")
        assertThat(
            serializeLine(
                WsvLine(
                    arrayOf("V11", "V12", "V13"),
                    arrayOf(),
                    null
                )
            )
        ).isEqualTo("V11 V12 V13")
        assertThat(
            serializeLine(WsvLine(arrayOf("V11", "V12"), arrayOf(), "Comment"))
        ).isEqualTo("V11 V12 #Comment")
        assertThat(serializeLine(WsvLine(arrayOf("V11", "V12"), arrayOf(), null))).isEqualTo("V11 V12")
        assertThat(serializeLine(WsvLine(arrayOf(), arrayOf(), "Comment"))).isEqualTo("#Comment")
        assertThat(serializeLine(WsvLine(arrayOf(), arrayOf(), null))).isEqualTo("")
    }

}
