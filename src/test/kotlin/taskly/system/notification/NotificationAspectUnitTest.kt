package taskly.system.notification

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.report.Location
import taskly.system.report.Report
import taskly.system.report.ReportRepository
import taskly.system.report.ReportService
import taskly.system.section.CriticalSection
import taskly.system.section.CriticalSectionSensor
import taskly.system.user.User


@Suppress("UNCHECKED_CAST")
@DataJpaTest
@EnableAspectJAutoProxy
@ExtendWith(SpringExtension::class)
@ComponentScan("taskly.system.section", "taskly.system.security", "taskly.system.user",
        "taskly.system.report", "taskly.system.notification")
@EnableJpaRepositories("taskly.system.section", "taskly.system.notification",
        "taskly.system.report", "taskly.system.user", "taskly.system.section")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
class NotificationAspectUnitTest {

    @Autowired
    private lateinit var sensor: CriticalSectionSensor

    @Autowired
    private lateinit var reportRepository: ReportRepository

    @MockBean
    private lateinit var expoNotificationService: ExpoNotificationService

    @Autowired
    private lateinit var reportService: ReportService

    // This function is needed in order for Mockito.any to work in Kotlin
    private fun <T> any(): T = Mockito.any<T>().let { null as T }

    fun loginWith(user: User) {
        SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(user, user.authorities)
    }

    @Test
    fun whenMarkingReportAsSolved_WithValidReport_ExpectNotificationSendToUser() {
        // given:
        User().copy(expoNotificationToken = "ExponentPushToken[hp-mLhDdLUJi_1ztLaWkhH]")
                .let { loginWith(it) }
        val subject = Report(Location(0.0, 0.0)).let { reportRepository.save(it) }
        // when:
        reportService.markReportAsSolved(subject.id)
        // then:
        verify(expoNotificationService, times(1)).send(any())
    }


    @Test
    fun whenNotMarkingReportAsSolved_WithInvalidReport_ExpectNotificationIgnored() {
        // given:
        User().copy(expoNotificationToken = "ExponentPushToken[hp-mLhDdLUJi_1ztLaWkhH]")
                .let { loginWith(it) }
        val subject = Report(Location(0.0, 0.0))
        // when:
        reportService.markReportAsSolved(subject.id)
        // then:
        verify(expoNotificationService, times(0)).send(any())
    }

    @Test
    fun whenReportIsMarkedAsSpam_ByThePolice_ExpectNotificationSendToUser() {
        // given:
        User().copy(expoNotificationToken = "ExponentPushToken[hp-mLhDdLUJi_1ztLaWkhH]")
                .let { loginWith(it) }
        val subject = Report(Location(0.0, 0.0))
                .let { reportRepository.save(it) }
        // when:
        reportService.markReportAsSolved(subject.id)
        // then:
        verify(expoNotificationService, times(1)).send(any())
    }

    @Test
    fun whenNotMarkingReportAsSpam_WithInvalidReportId_ExpectNotificationNotSendToUser() {
        // given:
        User().copy(expoNotificationToken = "ExponentPushToken[hp-mLhDdLUJi_1ztLaWkhH]")
                .let { loginWith(it) }
        val subject = Report(Location(0.0, 0.0))
        // when:
        reportService.markReportAsSolved(subject.id)
        // then:
        verify(expoNotificationService, times(0)).send(any())
    }

    @Test
    fun whenDetectingCriticalSection_ExpectNotificationSendToUser() {
        // given:
        User().copy(expoNotificationToken = "ExponentPushToken[hp-mLhDdLUJi_1ztLaWkhH]")
                .let { loginWith(it) }
        val origin = Location(0.0, 0.0)
        val reports = listOf(Report(origin),
                Report(Location(0.01, 0.01)),
                Report(Location(0.02, 0.02)))
                .map { reportRepository.save(it) }
        CriticalSection(
                origin = origin, radius = 3000.0,
                reports = reports.subList(0, 2))
        // when:
        sensor.criticalSectionFormedAt(origin, 3000.0, 2)
        // then:
        verify(expoNotificationService, times(1)).send(any())
    }
}