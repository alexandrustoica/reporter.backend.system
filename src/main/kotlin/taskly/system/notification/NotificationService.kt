package taskly.system.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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

    fun markNotificationAsRead(id: Int): Notification? =
        notificationRepository.findById(id)?.copy(isRead = true)
                    ?.let { notificationRepository.save(it) }

    fun getAllNotificationsFrom(user: User): List<Notification> =
            notificationRepository.findNotificationsByUser(user)

    fun findLatestNotificationsFor(user: User, page: PageRequest): Page<Notification> =
            notificationRepository.findNotificationsByUserOrderByDateDesc(user, page)

    fun getNotificationById(id: Int): Notification? =
            notificationRepository.findById(id)

    fun saveExpoNotificationTokenFor(user: User, token: String): User =
            userRepository.save(user.copy(expoNotificationToken = token))
}