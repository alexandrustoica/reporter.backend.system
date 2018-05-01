package taskly.system.notification

import com.google.api.Http
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import taskly.system.user.User
import taskly.system.user.UserService


@RestController
@RequestMapping("/notifications")
class NotificationController {

    @Autowired
    private lateinit var service: NotificationService
    
    @Autowired
    private lateinit var userService: UserService

    @ResponseBody
    @PutMapping("/{id}")
    fun markNotificationAsRead(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable id: Int): ResponseEntity<Notification> =
            if (service.getNotificationById(id)?.user?.id == user.id)
                service.markNotificationAsRead(id)
                        ?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                        ?: ResponseEntity(HttpStatus.NOT_FOUND)
            else ResponseEntity(HttpStatus.NOT_ACCEPTABLE)


    @ResponseBody
    @GetMapping("/{page}/{size}")
    @Secured(value = ["ROLE_POLICE", "ROLE_USER"])
    fun getAllNotificationsFromCurrentUser(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable page: Int,
            @PathVariable size: Int): Page<Notification> =
            service.findLatestNotificationsFor(user, PageRequest(page, size))


    @ResponseBody
    @PutMapping("/expo/{token}")
    @Secured(value = ["ROLE_POLICE", "ROLE_USER"])
    fun saveExpoNotificationTokenFor(
            @AuthenticationPrincipal @ApiIgnore user: User,
            @PathVariable token: String): ResponseEntity<User> =
            userService.getById(user.id)?.let {
                userService.save(it.copy(expoNotificationToken = token))
                        ?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                        ?: ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
            } ?: ResponseEntity(HttpStatus.NOT_FOUND)
}