package taskly.system.report

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.specification.AllReportsInSectorOf
import taskly.system.specification.SectorCriteria
import taskly.system.user.User
import taskly.system.user.UserRepository
import java.io.File
import java.util.*


@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system.section",
        "taskly.system.report", "taskly.system.user")
@ComponentScan("taskly.system.section", "taskly.system.report",
        "taskly.system.user", "taskly.system.security")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
class ReportServiceTest {

    @Autowired
    private lateinit var service: ReportService

    @Autowired
    private lateinit var reportRepository: ReportRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun whenSavingReport_WithValidReport_ExpectReportSaved() {
        // given:
        val report = Report()
        // when:
        val actual = service.save(report)
        // then:
        assertThat(actual, `is`(equalTo(report.copy(id = 1))))
    }

    @Test
    fun whenDeletingReport_ReportExists_ExpectReportDeleted() {
        // given:
        val report = service.save(Report())
        // when:
        report?.let { service.delete(it) }
        // then:
        assertThat(reportRepository.findAll().count(), `is`(0))
    }

    @Test
    fun whenDeletingReportById_ReportExists_ExpectReportDeleted() {
        // given:
        val report = service.save(Report())
        // when:
        report?.id?.let { service.delete(it) }
        // then:
        assertThat(reportRepository.findAll().count(), `is`(0))
    }

    @Test
    fun whenGettingReportById_ReportExists_ExpectReport() {
        // given:
        val report = service.save(Report())
        // when:
        val actual = report?.id?.let { service.findById(it) }
        // then:
        assertThat(actual, `is`(equalTo(report)))
    }

    @Test
    fun whenGettingReportById_WithInvalidReport_ExpectReportNotFound() {
        // when:
        assertThrows(ReportNotFound::class.java, { service.findById(1) })
    }

    @Test
    fun whenGettingPhotos_WithValidReport_ExpectValidPhoto() {
        // given:
        val subject = listOf(Photo(PhotoAsBytes(File("images/invalid/example_2.png"), "png").value()))
        val report = Report(photos = subject)
        // when:
        val result = reportRepository.save(report)
                .let { service.getPhotosFromPhoto(it.id) }
        // then:
        assertThat(subject, `is`(equalTo(result)))
    }

    @Test
    fun whenGettingAllReports_DatabaseWithMultipleReports_ExpectReports() {
        // given:
        listOf(Report(), Report()).forEach { service.save(it) }
        // when:
        val reports = service.all(PageRequest(0, 10))
        // then:
        assertThat(reports.count(), `is`(2))
    }

    @Test
    fun whenInsertingReportWithUser_UserExists_ExpectUserOwnsReport() {
        // given:
        val user = userRepository.save(User())
        val report = Report().copy(user = user)
        // when:
        val expected = service.save(report)
        // then:
        assertThat(reportRepository.findReportsByUserOrderByDateDesc(user,
                PageRequest(0, 10)).content,
                IsIterableContainingInAnyOrder.containsInAnyOrder(expected))
    }

    @Test
    fun whenGettingAllReportsFromUser_UserExists_ExpectReportsOwnedByUser() {
        // given:
        val user = userRepository.save(User())
        listOf(Report().copy(user = user), Report().copy(user = user))
                .forEach { service.save(it) }
        // when:
        val reports = service.findByUser(user, PageRequest(0, 10))
        // then:
        assertThat(reports.numberOfElements, `is`(2))
    }

    @Test
    fun whenGettingAllReportsFromUserAfterDate_UserExists_ExpectCorrectReports() {
        // given:
        val user = userRepository.save(User())
        val reports = listOf(Report().copy(user = user),
                Report().copy(user = user, date = Calendar.getInstance()
                        .apply { roll(Calendar.DAY_OF_MONTH, 1) }),
                Report().copy(user = user, date = Calendar.getInstance()
                        .apply { roll(Calendar.DAY_OF_MONTH, -1) }))
                .map { service.save(it) }
        // when:
        val result = service.findByUserAndDateAfter(
                user, Calendar.getInstance(), PageRequest(0, 10))
        // then:
        assertThat(result, `is`(reports.subList(0, 2)))
    }

    @Test
    fun whenGettingReportsInSector_WithLocation_ExpectCorrectReports() {
        // given:
        val specification = AllReportsInSectorOf(
                SectorCriteria(origin = Location(0.0, 0.0), radiusOfSector = 3000.0))
        val reports = listOf(Report(Location(0.0, 0.0)),
                Report(Location(0.01, 0.01)),
                Report(Location(0.03, 0.03)))
                .map { reportRepository.save(it) }
        val subject = reports.subList(0, 2)
        // when:
        val result = reportRepository.findAll(specification)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenGettingAllReportsNearLocation_WithValidLocation_ExpectCorrectResult() {
        // given:
        val reports = listOf(Report(Location(0.0, 0.0)),
                Report(Location(0.01, 0.01)),
                Report(Location(0.02, 0.02)))
                .map { reportRepository.save(it) }
        val subject = reports.subList(0, 2)
        // when:
        val result = service.findAllReportsNear(Location(0.0, 0.0), byDistance = 3000.0).sortedBy { it.id }
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenMarkingReportAsSolved_WithValidReport_ExpectReportSolved() {
        // given:
        val subject = Report().let { reportRepository.save(it) }
        // when:
        val result = service.markReportAsSolved(subject.id)
        // then:
        assertThat(result?.isSolved, `is`(equalTo(true)))
    }

    @Test
    fun whenMarkingReportAsSpam_WithValidReport_ExpectReportSpammed() {
        // given:
        val subject = Report().let { reportRepository.save(it) }
        // when:
        val result = service.markReportAsSpam(subject.id)
        // then:
        assertThat(result?.isSpam, `is`(equalTo(true)))
    }
}