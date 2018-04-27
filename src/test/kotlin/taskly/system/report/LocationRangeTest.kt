package taskly.system.report

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.jupiter.api.Test

class LocationTest {

    @Test
    fun whenCalculatingDistanceBetweenLocations_ValidLocations_ExpectCorrectDistance() {
        // given:
        val subject = 3145.0
        // when:
        val result = Location(0.0, 0.0).distanceFrom(Location(0.02, 0.02))
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenCalculatingDestination_WithValidLocationAndDistance_ExpectCorrectDistance() {
        // given:
        val distance = 3145.0
        val location = Location(0.0, 0.0)
        // when:
        val result = location.destinationWith(distance, angle = 0.0)
        // then:
        assertThat(location.distanceFrom(result), `is`(equalTo(distance)))
    }
}