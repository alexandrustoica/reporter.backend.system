package taskly.system.notification

import com.nhaarman.mockito_kotlin.notNull
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan

import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.report.*
import taskly.system.section.CriticalSection
import taskly.system.section.CriticalSectionRepository
import taskly.system.section.CriticalSectionSensor
import taskly.system.user.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken


@DataJpaTest
@EnableAspectJAutoProxy
@ExtendWith(SpringExtension::class)
@ComponentScan("taskly.system.section", "taskly.system.report", "taskly.system.notification")
@EnableJpaRepositories("taskly.system.section", "taskly.system.notification",
        "taskly.system.report", "taskly.system.user", "taskly.system.section")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
public class NotificationAspectTest {

    @Autowired
    private lateinit var sensor: CriticalSectionSensor

    @Autowired
    private lateinit var reportsRepository: ReportRepository

    @Autowired
    private lateinit var reportService: ReportService

    @Autowired
    private lateinit var criticalSectionRepository: CriticalSectionRepository

    fun loginWith(user: User) {
        SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(user, user.authorities)
    }

    @Test
    fun whenMarkingReportAsSolved_WithValidReport_ExpectNotificationSendToUser() {
        // given:
        User().copy(expoNotificationToken = "ExponentPushToken[hp-mLhDdLUJi_1ztLaWkhH]")
                .let { loginWith(it) }
        val subject = Report(Location(0.0, 0.0)).let { reportsRepository.save(it) }
        // when:
        val result = reportService.markReportAsSolved(subject.id)
        // then:
        assertThat(result, `is`(notNullValue()))
    }

    @Test
    fun whenDetectingCriticalSection_WithValidUser_ExpectNotificationSendToUser() {
        // given:
        val origin = Location(0.0, 0.0)
        val reports = listOf(Report(Location(0.0, 0.0)),
                Report(Location(0.01, 0.01)),
                Report(Location(0.02, 0.02)))
                .map { reportsRepository.save(it) }
        CriticalSection(
                origin = origin, radius = 3000.0,
                reports = reports.subList(0, 2))
        // when:
        val result = sensor.criticalSectionFormedAt(origin, 3000.0, 2)
        // then:
        assertThat(result, `is`(notNullValue()))
    }
}