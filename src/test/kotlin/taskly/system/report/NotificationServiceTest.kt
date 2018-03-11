package taskly.system.report
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
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
import taskly.system.notification.Notification
import taskly.system.notification.NotificationRepository
import taskly.system.notification.NotificationService
import taskly.system.user.User
import taskly.system.user.UserRepository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system.notification", "taskly.system.user")
@ComponentScan("taskly.system.notification", "taskly.system.user", "taskly.system.security")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = arrayOf("classpath:integrationtests.properties"))
class NotificationServiceTest {

    @Autowired
    private lateinit var service: NotificationService

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `when getting notifications for user expect notifications`() {
        // given:
        val user = User().let { userRepository.save(it) }
        val notifications = listOf(Notification(user), Notification(user), Notification(user))
                .map { notificationRepository.save(it) }
        // when:
        val actual = service.findNotificationsByUser(user)
        // then:
        assertThat(notifications[0], `is`(equalTo(actual[0])))
    }
}