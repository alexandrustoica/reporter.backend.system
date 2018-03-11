package taskly.system.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import taskly.system.user.User
import taskly.system.user.UserNotFound
import taskly.system.user.UserRepository

@Controller
class NotificationController {

    @Autowired
    private lateinit var service: NotificationService

    @Autowired
    private lateinit var userRepository: UserRepository

    @MessageMapping("/notifications")
    @SendTo("/notifications/notifications")
    fun getNotifications(@Payload idUser: Int):
            List<Notification> {
        return service.findNotificationsByUser(getUserById(idUser))
    }

    private fun getUserById(id: Int): User =
            userRepository.findUserById(id) ?: throw UserNotFound()
}