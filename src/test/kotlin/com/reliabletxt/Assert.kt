package com.reliabletxt

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

object Assert {
    fun equals(actual: ByteArray?, expected: ByteArray?) {
        assertArrayEquals(expected, actual)
    }

    fun equals(actual: IntArray?, expected: IntArray?) {
        assertArrayEquals(expected, actual)
    }

    fun <T> equals(actual: Array<T>?, expected: Array<T>?) {
        assertArrayEquals(expected, actual)
    }

    fun equals(actual: Any?, expected: Any?) {
        assertEquals(expected, actual)
    }

    fun equals(actual: String?, expected: String?) {
        assertEquals(expected, actual)
    }

    fun equals(actual: Long, expected: Long) {
        assertEquals(expected, actual)
    }

    fun isTrue(actual: Boolean) {
        assertTrue(actual)
    }

    fun isFalse(actual: Boolean) {
        assertFalse(actual)
    }
}
