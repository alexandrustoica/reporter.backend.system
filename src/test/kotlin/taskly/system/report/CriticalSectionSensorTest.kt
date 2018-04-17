package taskly.system.report

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.section.CriticalSection
import taskly.system.section.CriticalSectionRepository
import taskly.system.section.CriticalSectionSensor

@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system.section", "taskly.system.report",
        "taskly.system.user", "taskly.system.notification")
@ComponentScan("taskly.system.section", "taskly.system.report", "taskly.system.notification")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
class CriticalSectionSensorTest {

    @Autowired
    private lateinit var sensor: CriticalSectionSensor

    @Autowired
    private lateinit var reportsRepository: ReportRepository

    @Autowired
    private lateinit var criticalSectionRepository: CriticalSectionRepository

    @Test
    fun whenDetectingCriticalSection_WithValidContext_ExpectCriticalSectionDetected() {
        // given:
        val origin = Location(0.0, 0.0)
        val reports = listOf(Report(Location(0.0, 0.0)),
                Report(Location(0.01, 0.01)),
                Report(Location(0.02, 0.02)))
                .map { reportsRepository.save(it) }
        val subject = CriticalSection(
                origin = origin, radius = 3000.0,
                reports = reports.subList(0, 2))
        // when:
        val result = sensor.criticalSectionFormedAt(origin, 3000.0, 2)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenDetectingCriticalSections_CriticalSectionInRadius_ExpectNullOptional() {
        // given:
        val origin = Location(0.0, 0.0)
        val reports = listOf(Report(Location(0.0, 0.0)),
                Report(Location(0.01, 0.01)),
                Report(Location(0.02, 0.02)))
                .map { reportsRepository.save(it) }
        CriticalSection(origin = origin, radius = 3000.0,
                reports = reports.subList(0, 2))
                .let { criticalSectionRepository.save(it) }
        // when:
        val result = sensor.criticalSectionFormedAt(origin, 3000.0, 2)
        // then:
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun whenDetectingCriticalSection_WithCriticalSectionOutsideRadius_ExpectCriticalSection() {
        // given:
        val originSavedCriticalSection = Location(0.2, 0.2)
        val origin = Location(0.0, 0.0)
        val reports = listOf(Report(Location(0.0, 0.0)),
                Report(Location(0.01, 0.01)),
                Report(Location(0.02, 0.02)),
                Report(Location(0.03, 0.03)))
                .map { reportsRepository.save(it) }
        CriticalSection(origin = originSavedCriticalSection,
                radius = 3000.0, reports = reports.subList(2, 4))
                .let { criticalSectionRepository.save(it) }
        val subject = CriticalSection(origin = origin,
                radius = 3000.0, reports = reports.subList(0, 2))
        // when:
        val result = sensor.criticalSectionFormedAt(origin, 3000.0, 2)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenDeletingCriticalSection_AtValidLocation_ExpectCriticalSectionDeleted() {
        // given:
        val origin = Location(0.0, 0.0)
        val reports = listOf(Report(Location(0.0, 0.0)),
                Report(Location(0.01, 0.01)))
                .map { reportsRepository.save(it) }
        val subject =CriticalSection(origin = origin, radius = 3000.0,
                reports = reports)
                .let { criticalSectionRepository.save(it) }
        // when:
        val result = sensor.deleteIfNeedCriticalSectionFrom(origin, 3000.0, 3)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }
}