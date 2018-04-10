package taskly.system.report

import java.io.Serializable
import java.util.*

data class Photo
constructor(val bytes: ByteArray = byteArrayOf()) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Photo) return false
        if (!Arrays.equals(bytes, other.bytes)) return false
        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(bytes)
    }
}