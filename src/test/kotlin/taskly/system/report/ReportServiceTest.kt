package taskly.system.report

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInAnyOrder
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
import taskly.system.domain.User
import taskly.system.repository.UserRepository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system.report", "taskly.system.repository")
@ComponentScan("taskly.system.report")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = arrayOf("classpath:integrationtests.properties"))
class ReportServiceTest {

    @Autowired
    private lateinit var service: ReportService

    @Autowired
    private lateinit var reportRepository: ReportRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `when saving report and report valid expect report saved`() {
        // given:
        val report = Report()
        // when:
        val actual = service.save(report)
        // then:
        assertThat(actual, `is`(equalTo(report.copy(id = 1))))
    }

    @Test
    fun `when deleting report and report exists expect report deleted`() {
        // given:
        val report = service.save(Report())
        // when:
        report?.let { service.delete(it) }
        // then:
        assertThat(reportRepository.findAll().count(), `is`(0))
    }

    @Test
    fun `when deleting report by id and report exists expect report deleted`() {
        // given:
        val report = service.save(Report())
        // when:
        report?.id?.let { service.delete(it) }
        // then:
        assertThat(reportRepository.findAll().count(), `is`(0))
    }

    @Test
    fun `when getting report by id and report exists expect report`() {
        // given:
        val report = service.save(Report())
        // when:
        val actual = report?.id?.let { service.findById(it) }
        // then:
        assertThat(actual, `is`(equalTo(report)))
    }

    @Test
    fun `when getting report by id and report doesn't exist expect ReportNotFound`() {
        // when:
        assertThrows(ReportNotFound::class.java, { service.findById(1) })
    }

    @Test
    fun `when getting all reports and database has more than one report expect reports`() {
        // given:
        listOf(Report(), Report()).forEach { service.save(it) }
        // when:
        val reports = service.all(PageRequest(0, 10))
        // then:
        assertThat(reports.count(), `is`(2))
    }

    @Test
    fun `when inserting report with user and user exists expect user owns report`() {
        // given:
        val user =  userRepository.save(User())
        val report = Report().copy(user = user)
        // when:
        val expected = service.save(report)
        // then:
        assertThat(reportRepository.findReportsByUser(user, PageRequest(0, 10)).content,
                IsIterableContainingInAnyOrder.containsInAnyOrder(expected))
    }

    @Test
    fun `when getting all reports from user and user exists expect user's reports`() {
        // given:
        val user =  userRepository.save(User())
        listOf(Report().copy(user = user), Report().copy(user = user)).forEach { service.save(it) }
        // when:
        val reports = service.findByUser(user, PageRequest(0, 10))
        // then:
        assertThat(reports.numberOfElements, `is`(2))
    }
}