package taskly.system.report

import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class PhotoAsBytes(
        private val file: File,
        private val extension: String) {

    fun value(): ByteArray {
        val buffer = ImageIO.read(file)
        val result = ByteArrayOutputStream()
        ImageIO.write(buffer, extension, result)
        return result.toByteArray()
    }
}