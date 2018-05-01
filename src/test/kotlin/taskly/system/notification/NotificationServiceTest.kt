package taskly.system.notification

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
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
import taskly.system.user.User
import taskly.system.user.UserRepository
import java.util.*


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
    fun whenMarkingNotificationAsRead_WithValidNotification_ExpectNotificationMarkedAsRead() {
        // given:
        val user = User()
                .withExpoNotificationToken("token")
                .let { userRepository.save(it) }
        val subject = Notification("Title", "message", user)
                .let { notificationRepository.save(it) }
        // when:
        val result = service.markNotificationAsRead(subject.id)?.isRead
        // then:
        assertThat(result, `is`(true))
    }

    @Test
    fun whenGettingAllNotifications_FromUser_ExpectNotificationsInOrder() {
        // given:
        val user = User().let { userRepository.save(it) }
        val yesterday = Calendar.getInstance()
                .also { it.roll(Calendar.DAY_OF_MONTH, -1) }
        val twoDaysAgo = Calendar.getInstance()
                .also { it.roll(Calendar.DAY_OF_MONTH, -2) }
        val notifications = listOf(
                Notification("Title", "message", user).copy(date = yesterday),
                Notification("Title", "message", user).copy(date = twoDaysAgo),
                Notification("Title", "message", user))
                .map { notificationRepository.save(it) }
        val subject = notifications.sortedByDescending { it.date }
        // when:
        val result = service.findLatestNotificationsFor(user, PageRequest(0, 4)).content
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }


    @Test
    fun whenMarkingNotificationAsRead_WithInvalidNotification_ExpectNotificationNotMarkedAsRead() {
        // given:
        val user = User()
                .withExpoNotificationToken("token")
                .let { userRepository.save(it) }
        val subject = Notification("Title", "message", user)
        // when:
        val result = service.markNotificationAsRead(subject.id)?.isRead ?: false
        // then:
        assertThat(result, `is`(false))
    }

    @Test
    fun whenSavingNotification_WithValidNotification_ExpectNotificationDateCorrect() {
        // given:
        val user = User()
                .withExpoNotificationToken("token")
                .let { userRepository.save(it) }
        val subject = Calendar.getInstance()
        val notification = Notification("Title", "message", user)
                .let { notificationRepository.save(it) }
        // when:
        val result = notification.date
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