package reliableKt.wsv

import reliableKt.wsv.WsvChar.isWhitespace

fun String?.isWhitespace(): Boolean =
     !isNullOrEmpty() && all { isWhitespace(it.code) }
