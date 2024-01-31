package reliableKt.wsv

import reliableKt.wsv.WsvChar.isWhitespace

object WsvString {
    fun isWhitespace(str: String?): Boolean = when {
        str.isNullOrEmpty() -> false
        else -> str.all { isWhitespace(it.code) }
    }
}