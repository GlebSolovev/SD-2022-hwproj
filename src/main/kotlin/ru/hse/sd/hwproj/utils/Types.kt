package ru.hse.sd.hwproj.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

typealias Timestamp = Instant

private val timestampFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy HH:mm:ss")
    .withZone(TimeZone.getDefault().toZoneId())

fun Timestamp.formatToString(): String = timestampFormatter.format(this)

@Serializable
@SerialName("Timestamp")
private data class TimestampSurrogate(val epochSecond: Long, val nano: Int)

class TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor = TimestampSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Instant) {
        val surrogate = TimestampSurrogate(value.epochSecond, value.nano)
        encoder.encodeSerializableValue(TimestampSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val surrogate = decoder.decodeSerializableValue(TimestampSurrogate.serializer())
        return Instant.ofEpochSecond(surrogate.epochSecond, surrogate.nano.toLong())
    }
}

@Serializable
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
