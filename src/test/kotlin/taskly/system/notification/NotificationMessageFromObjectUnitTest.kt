package taskly.system.notification

import org.junit.Test
import taskly.system.report.Report

import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import taskly.system.report.Location
import taskly.system.section.CriticalSection

class NotificationMessageFromObjectUnitTest {

    @Test
    fun whenConvertingReportToNotificationMessage_ExpectCorrectNotificationMessage() {
        // given:
        val subject = Report()
        // when:
        val result = NotificationMessageFromObject(subject)
        // then:
        assertThat(result.toString(), `is`(subject.title))
    }

    @Test
    fun whenConvertingCriticalSectionToNotificationMessage_ExpectCorrectNotificationMessage() {
        // given:
        val subject = CriticalSection(Location(0.0, 0.0))
        // when:
        val result = NotificationMessageFromObject(subject)
        // then:
        assertThat(result.toString(), `is`(subject.origin.toString()))
    }
}