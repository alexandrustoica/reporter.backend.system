package taskly.system.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import taskly.system.user.User

@Service
class NotificationService {

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    fun findNotificationsByUser(user: User): List<Notification> =
            notificationRepository.findNotificationsByUser(user)

    fun send(notification: Notification): Notification =
            notificationRepository.save(notification)
}