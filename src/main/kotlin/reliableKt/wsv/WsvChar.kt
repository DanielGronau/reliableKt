package reliableKt.wsv


object WsvChar {
    fun isWhitespace(c: Int): Boolean {
        return c == 0x09
                || c in 0x0B..0x0D
                || c == 0x0020
                || c == 0x0085
                || c == 0x00A0
                || c == 0x1680
                || c in 0x2000..0x200A
                || c == 0x2028
                || c == 0x2029
                || c == 0x202F
                || c == 0x205F
                || c == 0x3000
    }

    val whitespaceCodePoints: IntArray
        get() = intArrayOf(
            0x0009,
            0x000B,
            0x000C,
            0x000D,
            0x0020,
            0x0085,
            0x00A0,
            0x1680,
            0x2000,
            0x2001,
            0x2002,
            0x2003,
            0x2004,
            0x2005,
            0x2006,
            0x2007,
            0x2008,
            0x2009,
            0x200A,
            0x2028,
            0x2029,
            0x202F,
            0x205F,
            0x3000
        )
}