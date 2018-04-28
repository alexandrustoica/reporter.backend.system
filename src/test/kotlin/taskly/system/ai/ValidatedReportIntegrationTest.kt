package taskly.system.ai

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.report.Photo
import taskly.system.report.PhotoAsBytes
import taskly.system.report.Report
import taskly.system.report.ValidatedReport
import java.io.File

@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system.section",
        "taskly.system.report", "taskly.system.user")
@ComponentScan("taskly.system.section", "taskly.system.report",
        "taskly.system.user", "taskly.system.security")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
class ValidatedReportIntegrationTest {

    @Test
    fun whenCheckingReport_WithInvalidParkingImage_ExpectReportMarkedAsSpam() {
        // given:
        val photos = listOf(Photo(PhotoAsBytes(File("invalid_parking.png"), "png").value()))
        val subject = Report(photos = photos)
        // when:
        val result = ValidatedReport(subject).value().isSpam
        // then:
        assertThat(result, `is`(true))
    }

    @Test
    fun whenCheckingReport_WithValidParkingImage_ExpectReportMarkedAsSpam() {
        // given:
        val photos = listOf(Photo(PhotoAsBytes(File("valid_parking.png"), "png").value()))
        val subject = Report(photos = photos)
        // when:
        val result = ValidatedReport(subject).value().isSpam
        // then:
        assertThat(result, `is`(false))
    }
}