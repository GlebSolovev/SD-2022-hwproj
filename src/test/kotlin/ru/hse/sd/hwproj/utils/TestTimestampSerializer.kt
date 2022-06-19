package ru.hse.sd.hwproj.utils

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class TestTimestampSerializer {

    private val serializer = TimestampSerializer()

    @Test
    fun test() {
        fun check(t: Timestamp) {
            assertEquals(
                t,
                Json.decodeFromString(
                    serializer,
                    Json.encodeToString(serializer, t)
                )
            )
        }
        check(Timestamp.now())
        check(Timestamp.ofEpochSecond(1607505416, 124000))
        check(Timestamp.ofEpochSecond(-1607505416, -124000))
        check(Timestamp.ofEpochSecond(987654321, 123456789))
        check(Timestamp.MAX)
        check(Timestamp.MIN)
    }
}
