package taskly.system.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/notifications")
class NotificationController {

    @Autowired
    private lateinit var service: NotificationService

    @Autowired
    private lateinit var expoService: ExpoNotificationService

}