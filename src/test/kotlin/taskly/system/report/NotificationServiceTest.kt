package taskly.system.report

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
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
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
class NotificationServiceTest {

    @Autowired
    private lateinit var service: NotificationService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @Test
    fun whenSavingNotification_ValidNotification_ExpectNotification() {
        // given:
        val user = User()
                .withExpoNotificationToken("test")
                .let { userRepository.save(it) }
        val subject = Notification("Title", "message", user)
        // when:
        val result = service.save(subject)
        // then:
        assertThat(result, both(`is`(equalTo(subject)))
                .and(`is`(equalTo(notificationRepository
                        .findNotificationsByUser(user).first()))))
    }

    @Test
    fun whenGettingUserNotifications_ValidUser_ExpectUserNotifications() {
        // given:
        val user = User()
                .withExpoNotificationToken("token")
                .let { userRepository.save(it) }
        val notification = Notification("Title", "message", user)
        val subject = listOf(service.save(notification),
                service.save(notification.copy(id = 0)))
        // when:
        val result = service.getAllNotificationsFrom(user)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenUpdatingUserNotificationToken_ValidToken_ExpectTokenUpdated() {
        // given:
        val subject = "token"
        val user = User().let { userRepository.save(it) }
        // when:
        val result = service.saveExpoNotificationTokenFor(user, subject)
        // then:
        assertThat(result.expoNotificationToken, both(`is`(equalTo(subject)))
                .and(`is`(equalTo(userRepository.findUserById(user.id)?.expoNotificationToken))))
    }


}