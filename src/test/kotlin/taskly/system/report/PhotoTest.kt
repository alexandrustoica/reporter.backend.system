package taskly.system.report

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class PhotoTest {

    @Test
    fun whenCheckingIfTwoPhotos_AreEqual_ExpectTrue() {
        // given:
        val left = Photo(PhotoAsBytes(File("images/invalid/example_2.png"), "png").value())
        val right = Photo(PhotoAsBytes(File("images/invalid/example_2.png"), "png").value())
        // when:
        val result = left == right
        // then:
        assertThat(result, `is`(true))
    }

    @Test
    fun whenCheckingIfTwoPhotos_AreNotEqual_ExpectFalse() {
        // given:
        val left = Photo(PhotoAsBytes(File("images/invalid/example_2.png"), "png").value())
        val right = Photo(PhotoAsBytes(File("images/invalid/example_3.png"), "png").value())
        // when:
        val result = left == right
        // then:
        assertThat(result, `is`(false))
    }

    @Test
    fun whenCheckingIfTwoPhotos_HaveSameHashCode_ExpectTrue() {
        // given:
        val left = Photo(PhotoAsBytes(File("images/invalid/example_2.png"), "png").value())
        val right = Photo(PhotoAsBytes(File("images/invalid/example_2.png"), "png").value())
        // when:
        val result = left.hashCode() == right.hashCode()
        // then:
        assertThat(result, `is`(true))
    }
}