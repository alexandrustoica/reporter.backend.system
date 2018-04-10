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
import taskly.system.notification.ExpoNotificationService
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
class ExpoNotificationServiceTest {

    @Autowired
    private lateinit var service: ExpoNotificationService

    @Test
    fun whenSendingNotification_ValidNotification_ExpectNotification() {
        // given:
        val user = User().withExpoNotificationToken("ExponentPushToken[hp-mLhDdLUJi_1ztLaWkhH]")
        val subject = Notification("Test", "Message", user)
        // when:
        val result = service.send(subject)
        // then:
        assertThat(result.data.status, `is`(equalTo("ok")))
    }
}