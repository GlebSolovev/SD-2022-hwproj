package ru.hse.sd.hwproj.utils

import java.time.ZonedDateTime

typealias Timestamp = ZonedDateTime

data class CheckerProgram(val bytes: ByteArray) {

    // auto-generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CheckerProgram

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

}
