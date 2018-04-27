package taskly.system.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import taskly.system.report.Report

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody user: User): User? =
            userService.save(user)

    @ResponseBody
    @PostMapping("/police")
    fun registerAsPoliceMan(@RequestBody user: User): ResponseEntity<User> =
            userService.save(user.copy(role = UserRole.POLICE))?.let {
                ResponseEntity(it, HttpStatus.ACCEPTED)
            } ?: ResponseEntity(HttpStatus.NOT_ACCEPTABLE)

    @ResponseBody
    @GetMapping("/current")
    fun getCurrentLoggedUser(@ApiIgnore @AuthenticationPrincipal user: User): User =
            userRepository.findUserById(user.id) ?: throw UserNotFound()
}
