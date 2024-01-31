package reliableKt.wsv


class WsvParserException(
    val index: Int,
    val lineIndex: Int,
    val linePosition: Int,
    message: String
) : RuntimeException(String.format("%s (%d, %d)", message, lineIndex + 1, linePosition + 1))