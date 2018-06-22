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
import taskly.system.report.*
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
    fun whenCheckingReport_WithInvalidParkingImage_ExpectReportNotMarkedAsSpam() {
        // given:
        val folder = File("images/invalid")
        val photos = folder.listFiles().filter { it.extension == "png" }
                .map { Photo(PhotoAsBytes(it, "png").value()) }
        val subjects = photos.map { Report(photos = listOf(it), type = ReportType.PARKING) }
        // when:
        val results = subjects.map { ValidatedReport().filter(it).isSpam }
        // then:
        results.forEach { assertThat(it, `is`(false)) }
    }

    @Test
    fun whenCheckingReport_WithValidParkingImage_ExpectReportMarkedAsSpam() {
        // given:
        val folder = File("images/valid")
        val photos = folder.listFiles().filter { it.extension == "png" }
                .map { Photo(PhotoAsBytes(it, "png").value()) }
        val subjects = photos.map { Report(photos = listOf(it), type = ReportType.PARKING) }
        // when:
        val results = subjects.map { ValidatedReport().filter(it).isSpam }
        results.forEach { println(it) }
        // then:
        results.forEach { assertThat(it, `is`(true)) }
    }


    @Test
    fun whenCheckingReport_WithTrashInImage_ExpectReportNotMarkedAsSpam() {
        // given:
        val folder = File("images/dump")
        val photos = folder.listFiles().filter { it.extension == "jpg" }
                .map { Photo(PhotoAsBytes(it, "jpg").value()) }
        val subjects = photos.map { Report(photos = listOf(it), type = ReportType.DUMP) }
        // when:
        val results = subjects.map { ValidatedReport().filter(it).isSpam }
        // then:
        results.forEach { assertThat(it, `is`(false)) }
    }

    @Test
    fun whenCheckingReport_WithGraffitiInImage_ExpectReportNonMarkedAsSpam() {
        // given:
        val folder = File("images/graffiti")
        val photos = folder.listFiles().filter { it.extension == "jpg" }
                .map { Photo(PhotoAsBytes(it, "jpg").value()) }
        val subjects = photos.map { Report(photos = listOf(it), type = ReportType.GRAFFITI) }
        // when:
        val results = subjects.map { ValidatedReport().filter(it).isSpam }
        // then:
        results.forEach { assertThat(it, `is`(false)) }

    }
}