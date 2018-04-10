package taskly.system.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import taskly.system.user.User
import taskly.system.user.UserRepository

@Service
class NotificationService {

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    fun save(notification: Notification): Notification =
            notificationRepository.save(notification)

    fun getAllNotificationsFrom(user: User): List<Notification> =
            notificationRepository.findNotificationsByUser(user)

    fun saveExpoNotificationTokenFor(user: User, token: String): User =
            userRepository.save(user.copy(expoNotificationToken = token))
}