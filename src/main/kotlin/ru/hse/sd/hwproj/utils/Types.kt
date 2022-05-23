package ru.hse.sd.hwproj.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

typealias Timestamp = Instant

class TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor = buildClassSerialDescriptor("Timestamp") {
        element<Long>("epochSeconds")
        element<Long>("nanos")
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.epochSecond)
        encoder.encodeLong(value.nano.toLong())
    }

    override fun deserialize(decoder: Decoder): Instant {
        val epochSeconds = decoder.decodeLong()
        val nanos = decoder.decodeLong()
        return Instant.ofEpochSecond(epochSeconds, nanos)
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
