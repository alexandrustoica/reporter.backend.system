package taskly.system.notification

import org.junit.Test
import taskly.system.report.Report

import org.hamcrest.Matchers.`is`
import org.junit.Assert.*

class NotificationMessageFromObjectTest {

    @Test
    fun whenConvertingReportToNotificationMessage_ExpectCorrectNotificationMessage() {
        // given:
        val report = Report()
        // when:
        val message = NotificationMessageFromObject(report)
        // then:
        assertThat(message.toString(), `is`("default "))
    }
}