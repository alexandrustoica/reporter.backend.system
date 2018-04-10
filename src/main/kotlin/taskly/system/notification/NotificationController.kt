package taskly.system.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import taskly.system.user.User
import taskly.system.user.UserNotFound
import taskly.system.user.UserRepository
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService



@Controller
class NotificationController {

    @Autowired
    private lateinit var service: NotificationService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    constructor(service: NotificationService, userRepository: UserRepository) {
        this.service = service
        this.userRepository = userRepository
        val executor = Executors.newScheduledThreadPool(1)
        executor.scheduleAtFixedRate({getNotifications("ana")}, 0, 3, TimeUnit.SECONDS)
    }

    @MessageMapping("/send/message")
    fun getNotifications(message: String) {
        println(message)
    }

    private fun getUserById(id: Int): User =
            userRepository.findUserById(id) ?: throw UserNotFound()
}